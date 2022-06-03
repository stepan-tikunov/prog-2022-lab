package edu.ifmo.tikunov.lab5.common.validate;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Information about composite type's field.
 */
public class FieldInfo {
	public final Field field;
	public final String name;
	public final String description;
	public final Constraint[] constraints;

	public FieldInfo(Field field, String name, String description, Constraint[] constraints) {
		this.field = field;
		this.name = name;
		this.description = description;
		this.constraints = constraints;
	}

	/**
	 * Returns {@code FieldInfo} about each field of {@code @Composite} type.
	 *
	 * @param	type {@code @Composite} type
	 * @return	map with field names as keys and {@code FieldInfo} as values
	 * @see		FieldInfo
	 * @see		edu.ifmo.tikunov.lab5.common.composite.Composite
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
}
