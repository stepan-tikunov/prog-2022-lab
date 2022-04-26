package edu.ifmo.tikunov.lab4.console;

/**
 * Thrown when program needs to exit.
 */
public class ExitSignal extends Exception {
	private final int code;

	public int getCode() {
		return code;
	}

	public ExitSignal(String message, int code) {
		super(message);
		this.code = code;
	}
}
