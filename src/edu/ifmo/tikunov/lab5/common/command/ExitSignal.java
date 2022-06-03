package edu.ifmo.tikunov.lab5.common.command;

import java.io.IOException;

/**
 * Thrown when program needs to exit.
 */
public class ExitSignal extends IOException {
	private final int code;

	public int getCode() {
		return code;
	}

	public ExitSignal(String message, int code) {
		super(message);
		this.code = code;
	}
}
