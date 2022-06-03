package edu.ifmo.tikunov.lab5.common.command;

import java.io.Serializable;
import java.util.stream.IntStream;

public class ServerResponse implements Serializable {
	public final boolean isError;
	public final Object[] body;
	public final ResponseFormat format;

	public static ServerResponse info(ResponseFormat format, Object... body) {
		return new ServerResponse(false, format, body);
	}

	public static ServerResponse info(String message) {
		return info(ResponseFormat.STRING, message);
	}

	public static ServerResponse error(ResponseFormat format, Object... body) {
		return new ServerResponse(true, format, body);
	}

	public static ServerResponse error(String message) {
		return error(ResponseFormat.STRING, message);
	}


	public boolean matches(Class<?>... bodyTypes) {
		if (bodyTypes.length != body.length) return false;

		return IntStream.range(0, body.length)
			.allMatch(i -> bodyTypes[i].isAssignableFrom(body[i].getClass()));
	}

	public String toString() {
		return String.format(format.format, body);
	}

	public ServerResponse(boolean isError, ResponseFormat format, Object... body) {
		this.isError = isError;
		this.format = format;
		this.body = body;
	}
}
