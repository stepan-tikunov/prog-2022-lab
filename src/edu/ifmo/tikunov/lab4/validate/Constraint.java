package edu.ifmo.tikunov.lab4.validate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * Field constraint.
 *
 * @see ConstraintType
 */
public @interface Constraint {
	ConstraintType type();

	String value();
}
