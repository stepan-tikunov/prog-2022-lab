package edu.ifmo.tikunov.lab4.composite;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
/**
 * Composite type default constructor.
 * Parameters must be annotated with {@code @FieldName}.
 *
 * @see edu.ifmo.tikunov.lab4.validate.FieldName
 */
public @interface CompositeConstructor {
}
