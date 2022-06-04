package edu.ifmo.tikunov.lab5.common.composite;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.ifmo.tikunov.lab5.common.command.BadParametersException;
import edu.ifmo.tikunov.lab5.common.command.SimpleParser;
import edu.ifmo.tikunov.lab5.common.validate.ConstraintValidator;
import edu.ifmo.tikunov.lab5.common.validate.Description;
import edu.ifmo.tikunov.lab5.common.validate.FieldInfo;

/**
 * Provides a set of functions that are required to parse composite classes.
 */
public final class CompositeParser {
    /**
     * Returns constructor annotated with {@code @CompositeConstructor}
     * of a class annotated with {@code @Composite}. If class is not annotated
     * with {@code @Composite} or there is no constructor annnotated with 
     * {@code @CompositeConstructor}, throws {@code ClassNotCompositeException}.
     *
     * @param   composite   composite type
     * @return  composite constructor
     * @see     Composite
     */
    public static Constructor<?> getConstructor(Class<?> composite) {
        if (!composite.isAnnotationPresent(Composite.class)) {
            throw new ClassNotCompositeException(
                    "Class " + composite.getName() + " is not composite or not annotated as composite.");
        }
        try {
            Constructor<?> ctor = Stream.of(composite.getDeclaredConstructors())
                    .filter(c -> c.isAnnotationPresent(CompositeConstructor.class))
                    .findFirst()
                    .orElse(null);
            return ctor;
        } catch (NoSuchElementException e) {
            throw new ClassNotCompositeException(
                    "Class " + composite.getName() + " is not composite or has no constructor annotated as composite.");
        }
    }

    /**
     * Returns the description of composite constructor's parameter.
     *
     * @param   param           composite constructor's parameter
     * @param   compositeClass  composite class
     * @return  description of composite constructor's parameter
     * @see     Composite
     */
    public static String getDescription(Parameter param, Class<?> compositeClass) {
        Map<String, FieldInfo> fields = FieldInfo
                .getFieldMap(compositeClass);
        String fieldName = FieldInfo.getFieldName(param);

        Description description = compositeClass.getAnnotation(Description.class);
        String prefix = description == null ? "" : description.value() + " ";

        String enumPostfix = "";
        if (SimpleParser.isEnum(param.getType())) {
            Object[] constants = param.getType().getEnumConstants();
            enumPostfix = Stream.of(constants)
                    .map(c -> c.toString())
                    .collect(Collectors.joining("|", " (", ")"));
        }
        return prefix + fields.get(fieldName).description
                + enumPostfix
                + ConstraintValidator.constraintsToString(param);
    }

    /**
     * Expands composite type into list of simple.
     *
     * @param   composite   composite type to be expanded
     * @return  list of simple types
     * @see     Composite
     * @see     SimpleParser
     */
    public static List<Class<?>> expandComposite(Class<?> composite) {
        Class<?>[] params = getConstructor(composite).getParameterTypes();

        List<Class<?>> result = Stream.of(params)
                .flatMap(cls -> SimpleParser.isSimple(cls) ? Stream.of(cls) : expandComposite(cls).stream())
                .collect(Collectors.toList());

        return result;
    }

    /**
     * Returns list of simple parameters for composite constructor.
     *
     * @param   composite       composite type
     * @return  list of simple parameters
     * @see     Composite
     */
    public static List<Parameter> getCtorParameters(Class<?> composite) {
        Parameter[] parameters = getConstructor(composite).getParameters();

        List<Parameter> result = Stream.of(parameters)
                .flatMap(param -> SimpleParser.isSimple(param.getType()) ? Stream.of(param)
                        : getCtorParameters(param.getType()).stream())
                .collect(Collectors.toList());

        return result;
    }

    /**
     * Returns list of descriptions for each of composite constructor's simple parameter.
     *
     * @param   composite   composite type
     * @return  list of descriptions
     * @see     Composite
     */
    public static List<String> getDescriptions(Class<?> composite) {
        Parameter[] parameters = getConstructor(composite).getParameters();

        List<String> result = Stream.of(parameters)
                .flatMap(param -> SimpleParser.isSimple(param.getType()) ? Stream.of(getDescription(param, composite))
                        : getDescriptions(param.getType()).stream())
                .collect(Collectors.toList());

        return result;
    }

    /**
     * Parses composite class from multiple strings that contain simple parameters.
     *
     * @param   params  strings with simple parameters. Queue is used for recursion
     * @param   cls     composite class
     * @return  object of a composite class
     * @throws  BadParametersException   if there is not enough parameters or if some parameter doesn't meet the requirements
     * @see Composite
     */
    public static Object parse(Queue<String> params, Class<?> cls) throws BadParametersException {
        Constructor<?> ctor = getConstructor(cls);
        Class<?>[] types = ctor.getParameterTypes();
        Object[] initargs = new Object[types.length];

        for (int i = 0; i < types.length; ++i) {
            if (SimpleParser.isSimple(types[i])) {
                String param = params.remove();
                initargs[i] = SimpleParser.parse(param, types[i]);
            } else {
                initargs[i] = parse(params, types[i]);
            }
        }

        try {
            return ctor.newInstance(initargs);
        } catch (Exception e) {
            throw new BadParametersException("");
        }
    }

    private static String indent(String s) {
        return Stream.of(s.split("\\r?\\n")).findFirst().get() + "\n" +
            Stream.of(s.split("\\r?\\n")).skip(1)
                .map(l -> "\t" + l)
                .collect(Collectors.joining("\n"));
    }

    public static String stringValue(Object obj) {
        if (obj == null) return "null";

        if (SimpleParser.isSimple(obj.getClass())) {
            return SimpleParser.stringValue(obj);
        }

        return indent(FieldInfo.getFieldMap(obj.getClass()).values().stream()
            .map(f -> {
                    try {
                        return SimpleParser.isSimple(f.field.getType()) ?
                            f.description + ": " + SimpleParser.stringValue(f.field.get(obj))
                        :
                            f.description + ": " + stringValue(f.field.get(obj));
                    } catch (IllegalAccessException e) {
                        // should never happen
                        return "";
                    }
                }
            ).collect(Collectors.joining("\n", obj.getClass().getSimpleName() + "(\n", ")")));
    }
}
