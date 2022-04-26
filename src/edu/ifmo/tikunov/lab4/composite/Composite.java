package edu.ifmo.tikunov.lab4.composite;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
/**
 * Composite type is a type that contains simple fields
 * or fields of other composite types.
 *
 * @see edu.ifmo.tikunov.lab4.model.SpaceMarine
 * @see edu.ifmo.tikunov.lab4.model.Chapter
 * @see edu.ifmo.tikunov.lab4.model.Coordinates
 */
public @interface Composite {
}
