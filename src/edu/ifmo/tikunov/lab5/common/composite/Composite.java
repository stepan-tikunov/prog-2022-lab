package edu.ifmo.tikunov.lab5.common.composite;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Composite type is a type that contains simple fields
 * or fields of other composite types.
 *
 * @see edu.ifmo.tikunov.lab5.common.model.SpaceMarine
 * @see edu.ifmo.tikunov.lab5.common.model.Chapter
 * @see edu.ifmo.tikunov.lab5.common.model.Coordinates
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Composite {
}
