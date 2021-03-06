package edu.ifmo.tikunov.lab5.common.composite;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Composite type default constructor.
 * Parameters must be annotated with {@code @FieldName}.
 *
 * @see edu.ifmo.tikunov.lab5.common.validate.FieldName
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.CONSTRUCTOR)
public @interface CompositeConstructor {
}
