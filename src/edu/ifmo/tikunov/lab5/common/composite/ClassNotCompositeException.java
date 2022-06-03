package edu.ifmo.tikunov.lab5.common.composite;

/**
 * Thrown if attempting to parse an instance of a class that
 * is not marked as {@link Composite} or has no constructor
 * marked as {@link CompositeConstructor}.
 */
public class ClassNotCompositeException extends RuntimeException {
	public ClassNotCompositeException(String message) {
		super(message);
	}
}
