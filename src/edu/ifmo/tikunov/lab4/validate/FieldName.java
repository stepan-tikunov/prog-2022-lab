package edu.ifmo.tikunov.lab4.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to link {@code @CompositeConstructor}'s parameters with composite class fields.
 * Should annotate both parameter and field with the same value.
 *
 * @see edu.ifmo.tikunov.lab4.composite.CompositeConstructor
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
public @interface FieldName {
	/**
	 * @return field name
	 */
	String value();
}
