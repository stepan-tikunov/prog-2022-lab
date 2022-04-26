package edu.ifmo.tikunov.lab4.console;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Provides a set of functions work with simple types
 * (enums, {@code LocalDateTime}, primitive wrappers such as
 * {@code Integer}, {@code Float}, etc.) from user input.
 */
public final class SimpleParser {

    /**
     * Parses {@code Boolean} from string.
     *
     * @param param string
     * @return {@code Boolean} value of {@code param}
     */
    private static Boolean parseBoolean(String param) {
        if (param == "true")
            return Boolean.valueOf(true);
        if (param == "false")
            return Boolean.valueOf(false);

        throw new RuntimeException();
    }

    
    /** 
     * Parses {@code Byte} from string.
     * 
     * @param param string
     * @return {@code Byte} value of {@code param}
     */
    private static Byte parseByte(String param) {
        return Byte.valueOf(Byte.parseByte(param));
    }

    
    /** 
     * Parses {@code Character} from string.
     * 
     * @param param string
     * @return {@code Character} value of {@code param}
     */
    private static Character parseCharacter(String param) {
        if (param.length() == 1)
            return Character.valueOf(param.charAt(0));

        throw new RuntimeException();
    }

    
    /** 
     * Parses {@code Double} from string.
     * 
     * @param param string
     * @return {@code Double} value of {@code param}
     */
    private static Double parseDouble(String param) {
        return Double.valueOf(Double.parseDouble(param));
    }

    
    /** 
     * Parses {@code Float} from string.
     * 
     * @param param string
     * @return {@code Float} value of {@code param}
     */
    private static Float parseFloat(String param) {
        return Float.valueOf(Float.parseFloat(param));
    }

    
    /** 
     * Parses {@code Integer} from string.
     * 
     * @param param string
     * @return {@code Integer} value of {@code param}
     */
    private static Integer parseInteger(String param) {
        return Integer.valueOf(Integer.parseInt(param));
    }

    
    /** 
     * Parses {@code LocalDateTime} from string.
     * 
     * @param param string in a format like {@code 2007-12-03T10:15:30}
     * @return {@code LocalDateTime} value of {@code param}
     */
    private static LocalDateTime parseLocalDateTime(String param) {
        return LocalDateTime.parse(param);
    }

    
    /** 
     * Parses {@code Long} from string
     * 
     * @param param string
     * @return {@code Long} value of {@code param}
     */
    private static Long parseLong(String param) {
        return Long.valueOf(Long.parseLong(param));
    }

    
    /** 
     * Parses {@code Short} from string
     * 
     * @param param string
     * @return {@code Short} value of {@code param}
     */
    private static Short parseShort(String param) {
        return Short.valueOf(Short.parseShort(param));
    }

    
    /** 
     * Does nothing.
     * 
     * @param param some string
     * @return {@code param}
     */
    private static String parseString(String param) {
        return param;
    }

    
    /** 
     * Parses enum constant from a string.
     * 
     * @param param string
     * @param enumClass class that extends {@code java.lang.Enum}
     * @return enum constant or null
     * @throws BadParametersException if the specified enum class has no constants with such name
     */
    private static Object parseEnum(String param, Class<?> enumClass) throws BadParametersException {
        Object constant = Stream.of(enumClass.getEnumConstants())
                .map(o -> (Enum<?>) o)
                .filter(e -> e.name().equalsIgnoreCase(param))
                .findFirst()
                .orElse(null);

        if (constant == null)
            throw new BadParametersException("The specified string is not among enum constants.");
        return constant;
    }

    
    /** 
     * Returns whether the specified class extends {@code java.lang.Enum}.
     * 
     * @param cls some class
     * @return {@code true} if {@code cls} extends {@code java.lang.Enum}
     */
    public static boolean isEnum(Class<?> cls) {
        return Enum.class.isAssignableFrom(cls);
    }

    
    /** 
     * Returns whether the specified class is simple.
     * 
     * @param cls some class
     * @return {@code true} if {@code cls} is simple
     */
    public static boolean isSimple(Class<?> cls) {
        Set<Class<?>> simpleClasses = new HashSet<>();
        simpleClasses.add(Boolean.class);
        simpleClasses.add(Byte.class);
        simpleClasses.add(Character.class);
        simpleClasses.add(Double.class);
        simpleClasses.add(Float.class);
        simpleClasses.add(Integer.class);
        simpleClasses.add(LocalDateTime.class);
        simpleClasses.add(Long.class);
        simpleClasses.add(Short.class);
        simpleClasses.add(String.class);

        return isEnum(cls) || simpleClasses.contains(cls);
    }

    
    /** 
     * Parses simple type from a string.
     *
     * @param param string
     * @param type  simple type
     * @param <T>   simple type parameter
     * @return parsed object
     * @throws BadParametersException if string is not of the specified {@code type}
     */
    @SuppressWarnings("unchecked")
    public static <T> T parse(String param, Class<T> type) throws BadParametersException {
        if (param == null)
            return null;
        if (!type.equals(String.class) && (param.equals("null") || param.equals("")))
            return null;
        if (!isSimple(type)) {
            throw new IllegalArgumentException(
                    "The specified type is not \"simple\". Simple types: "
                            + "Boolean, Byte, Character, Double, Float, Integer, LocalDateTime, Long, Short, String "
                            + "or enum.");
        }

        Map<Class<?>, Function<String, Object>> parsers = new HashMap<>();

        parsers.put(Boolean.class, SimpleParser::parseBoolean);
        parsers.put(Byte.class, SimpleParser::parseByte);
        parsers.put(Character.class, SimpleParser::parseCharacter);
        parsers.put(Double.class, SimpleParser::parseDouble);
        parsers.put(Float.class, SimpleParser::parseFloat);
        parsers.put(Integer.class, SimpleParser::parseInteger);
        parsers.put(LocalDateTime.class, SimpleParser::parseLocalDateTime);
        parsers.put(Long.class, SimpleParser::parseLong);
        parsers.put(Short.class, SimpleParser::parseShort);
        parsers.put(String.class, SimpleParser::parseString);

        Function<String, Object> parser = parsers.get(type);

        try {
            if (isEnum(type)) {
                return (T) parseEnum(param, type);
            }
            return (T) parser.apply(param);
        } catch (Exception e) {
            throw new BadParametersException(
                    "Couldn't parse " + type.getName() + ", caught " + e.getClass().getName() + " in parse function.");
        }
    }

    
    /** 
     * Returns string value of simple object.
     * 
     * @param simple object
     * @param type simple type
     * @return string value of {@code simple}
     */
    public static String stringValue(Object simple, Class<?> type) {
        if (!isSimple(type)) {
            throw new IllegalArgumentException(
                    "The specified type is not \"simple\". Simple types: "
                            + "Boolean, Byte, Character, Double, Float, Integer, LocalDateTime, Long, Short, String "
                            + "or any class that extends Enum.");
        }

        if (isEnum(type)) {
            return Optional.ofNullable((Enum<?>) simple)
                    .map(e -> e.name())
                    .orElse(null);
        }

        return simple.toString();
    }
}
