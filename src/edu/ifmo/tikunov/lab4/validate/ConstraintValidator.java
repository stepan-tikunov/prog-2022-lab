package edu.ifmo.tikunov.lab4.validate;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.ifmo.tikunov.lab4.command.BadParametersException;
import edu.ifmo.tikunov.lab4.command.SimpleParser;
import edu.ifmo.tikunov.lab4.composite.CompositeParser;

/**
 * Provides a set of functions to validate {@code @Composite}
 * types' fields with {@code @Constraints}.
 *
 * @see Constraints
 * @see Constraint
 * @see edu.ifmo.tikunov.lab4.composite.Composite
 */
public class ConstraintValidator {

	/**
	 * Returns {@code FieldInfo} about each field of {@code @Composite} type.
	 *
	 * @param	type {@code @Composite} type
	 * @return	map with field names as keys and {@code FieldInfo} as values
	 * @see		FieldInfo
	 * @see		edu.ifmo.tikunov.lab4.composite.Composite
	 */
	public static Map<String, FieldInfo> getFieldMap(Class<?> type) {
		Map<String, FieldInfo> map = new HashMap<>();

		for (Field f : type.getDeclaredFields()) {
			f.setAccessible(true);
			FieldName fieldNameAnnotation = f.getAnnotation(FieldName.class);
			if (fieldNameAnnotation == null)
				continue;
			String name = fieldNameAnnotation.value();
			String description = Optional.ofNullable(f.getAnnotation(Description.class))
					.map(Description::value)
					.orElse(name);
			Constraint[] constraints = Optional.ofNullable(f.getAnnotation(Constraints.class))
					.map(Constraints::value)
					.orElse(
							Optional.ofNullable(f.getAnnotation(Constraint.class))
									.map(c -> new Constraint[] { c })
									.orElse(null));
			FieldInfo info = new FieldInfo(f, name, description, constraints);
			map.put(name, info);
		}

		return map;
	}

	/**
	 * Gathers field name of parameter from {@code @CompositeConstructor}'s parameter
	 * annotated with {@code @FieldName}
	 *
	 * @param	param {@code @CompositeConstructor}'s parameter
	 * @return	field name
	 */
	public static String getFieldName(Parameter param) {
		return Optional.ofNullable(param.getAnnotation(FieldName.class))
				.map(FieldName::value)
				.orElseThrow(
						() -> new RuntimeException("Constructor parameter is not marked with FieldName annotation."));
	}

	/**
	 * Returns short information about constraints for
	 * {@code @CompositeConstructor}'s parameter
	 *
	 * @param	param {@code @CompositeConstructor}'s parameter
	 * @return	information about constraints
	 */
	public static String constraintsToString(Parameter param) {
		Map<String, FieldInfo> fields = getFieldMap(param.getDeclaringExecutable().getDeclaringClass());
		String fieldName = getFieldName(param);

		FieldInfo info = fields.get(fieldName);
		Constraint[] constraints = info.constraints;
		if (constraints == null)
			return "";

		return Stream.of(constraints)
				.filter(c -> c != null)
				.map(c -> {
					try {
						Object parsed = SimpleParser.parse(c.value(), info.field.getType());
						String strVal = SimpleParser.stringValue(parsed);
						if (String.class.isAssignableFrom(info.field.getType()) && c.value().equals(" ")) {
							strVal = "null";
						}
						return c.type().shortValue + strVal;
					} catch (BadParametersException e) {
						// should never happen
						return "";
					}
				})
				.collect(Collectors.joining(",", " (", ")"));
	}

	/**
	 * Checks if object fits {@code Constraint}.
	 *
	 * @param	type	type of {@code @Constraint}
	 * @param	obj		object
	 * @param	value	value of {@code @Constraint}
	 * @param	<T>		type of {@code obj}
	 * @return	{@code true} if {@code obj} fits {@code @Constraint}
	 * @see 	ConstraintType
	 * @see 	Constraint
	 */
	private static <T> boolean checkConstraint(ConstraintType type, Comparable<T> obj, T value) {
		try {
			switch (type) {
				case MORE_THAN:
					return obj.compareTo(value) > 0;
				case NOT_LESS_THAN:
					return obj.compareTo(value) >= 0;
				case LESS_THAN:
					return obj.compareTo(value) < 0;
				case NOT_MORE_THAN:
					return obj.compareTo(value) <= 0;
				case EQUAL_TO: {
					if (obj == null)
						return obj == value;
					return obj.equals(value);
				}
				case NOT_EQUAL_TO: {
					if (obj == null)
						return obj != value;
					return !obj.equals(value);
				}
				default:
					return false;
			}
		} catch (ClassCastException e) {
			return false;
		}
	}

	/**
	 * Validates field against all {@code @Constraints}
	 * that were declared.
	 *
	 * @param	objString	string value of field
	 * @param	info		information about field
	 * @param	paramType	field type
	 * @return	empty string if field fits all {@code @Constraints},
	 * 			otherwise message about all failed constraints
	 */
	@SuppressWarnings("unchecked")
	public static <T> String validateSimple(Object obj, Constraint[] constraints, Class<T> paramType) {
		try {
			Comparable<T> comparable = (Comparable<T>) obj;
			String objString = CompositeParser.stringValue(obj);
			if (constraints == null)
				return "";

			for (Constraint constraint : constraints) {
				try {
					T constraintValue = SimpleParser.parse(constraint.value(), paramType);
					if (String.class.isAssignableFrom(paramType) && constraint.value().equals(" ")) {
						constraintValue = null;
					}
					String constraintStringValue = SimpleParser.stringValue(constraintValue);
					if (!checkConstraint(constraint.type(), comparable, constraintValue)) {
						return "Constraint failed: the value must be " + constraint.type().toString() + " "
								+ constraintStringValue + ". The specified value is " + objString + "\n";
					}
				} catch (BadParametersException e) {
					throw new RuntimeException(
							"Bad value in constraint annotation: " + constraint.value());
				}
			}

			return "";
		} catch (ClassCastException e) {
			throw new RuntimeException("Class " + paramType.getSimpleName() + " is not comparable.");
		}

	}

	public static <T> String validateNull(Constraint[] constraints, Class<T> paramType) {
		String result = "";
		for (Constraint constraint : constraints) {
			try {
				T constraintValue = SimpleParser.parse(constraint.value(), paramType);
				String constraintStringValue = SimpleParser.stringValue(constraintValue);
				if (!checkConstraint(constraint.type(), null, constraintValue)) {
					result += "Constraint failed: the value must be " + constraint.type().toString() + " "
							+ constraintStringValue + ". The specified value is null\n";
				}
			} catch (BadParametersException e) {
				throw new RuntimeException(
						"Bad value in constraint annotation: " + constraint.value());
			}
		}
		return result;
	}

	/**
	 * Validates every field of object against all its {@code @Constraints}
	 *
	 * @param	obj		object
	 * @param	type	object type
	 * @return	empty string if object fits all {@code @Constraints},
	 * 			otherwise message about all failed constraints
	 */
	public static String validate(Object obj, Class<?> type) {
		Map<String, FieldInfo> fields = getFieldMap(type);
		for (FieldInfo info : fields.values()) {
			try {
				if (SimpleParser.isSimple(info.field.getType())) {
					String validationResult = validateSimple(info.field.get(obj), info.constraints, info.field.getType());
					if (!validationResult.equals("")) {
						return "Field \"" + info.name + "\": " + validationResult;
					}
				} else {
					String validationResult;
					if (info.field.get(obj) == null) {
						validationResult = validateNull(info.constraints, info.field.getType());
					} else {
						validationResult = validate(info.field.get(obj), info.field.getType());
					}
					if (!validationResult.equals("")) {
						return "Field \"" + info.name + "\": " + validationResult;
					}
				}
			} catch (IllegalAccessException e) {
				// should never happen
				return null;
			}
		}
		return "";
	}

	/**
	 * Validates {@code @CompositeConstructor}'s parameter against all its {@code @Constraints}
	 *
	 * @param 	objString	string value of object
	 * @param 	param		{@code @CompositeConstructor}'s parameter
	 * @param 	paramType	parameter type
	 * @return 	empty string if parameter fits all {@code @Constraints},
	 * 			otherwise message about all failed constraints
	 */
	public static String validateParameter(String objString, Parameter param, Class<?> paramType) {
		Map<String, FieldInfo> fields = getFieldMap(param.getDeclaringExecutable().getDeclaringClass());
		String fieldName = getFieldName(param);

		try {
			Object obj = SimpleParser.parse(objString, paramType);
			return validateSimple(obj, fields.get(fieldName).constraints, paramType);
		} catch (BadParametersException e) {
			return "Constraint failed: the value must be " + paramType.getSimpleName() + "\n";
		}
	}
}
