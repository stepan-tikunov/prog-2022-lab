package edu.ifmo.tikunov.lab5.common.command;

import java.io.Closeable;

public abstract class ResponseManager implements Closeable {
	public abstract void send(ServerResponse response);
	public abstract void close();

	public void info(ResponseFormat format, Object... body) {
		ServerResponse response = ServerResponse.info(format, body);
		send(response);
	}

	public void info(String message) {
		ServerResponse response = ServerResponse.info(message);
		send(response);
	}

	public void error(ResponseFormat format, Object... body) {
		ServerResponse response = ServerResponse.error(format, body);
		send(response);
	}

	public void error(String message) {
		ServerResponse response = ServerResponse.error(message);
		send(response);
	}
}
