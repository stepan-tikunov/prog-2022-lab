package edu.ifmo.tikunov.lab4.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * {@code @Composite} field's description, value used to ask for user input.
 *
 * @see edu.ifmo.tikunov.lab4.composite.Composite
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.TYPE })
public @interface Description {
	String value();
}
