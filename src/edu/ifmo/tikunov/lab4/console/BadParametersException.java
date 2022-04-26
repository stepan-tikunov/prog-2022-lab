package edu.ifmo.tikunov.lab4.console;

/**
 * Thrown when user types bad parameters for command.
 */
public class BadParametersException extends Exception {
	public BadParametersException(String message) {
		super(message);
	}
}