package edu.ifmo.tikunov.lab5.common.command;

/**
 * Thrown when user types bad parameters for command.
 */
public class BadParametersException extends Exception {
	public BadParametersException(String message) {
		super(message);
	}
}
