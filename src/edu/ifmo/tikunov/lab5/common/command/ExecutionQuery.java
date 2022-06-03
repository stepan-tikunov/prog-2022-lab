package edu.ifmo.tikunov.lab5.common.command;

import java.io.Serializable;

public class ExecutionQuery implements Serializable {
	public final String command;
	public final Object[] params;
	protected transient ResponseManager responseManager;

	public void setResponseManager(ResponseManager responseManager) {
		this.responseManager = responseManager;
	}

	public ResponseManager response() {
		return responseManager;
	}

	public ExecutionQuery(String command, Object[] args, ResponseManager responseManager) {
		this.command = command;
		this.params = args;
		this.responseManager = responseManager;
	}

	public ExecutionQuery(String command, Object[] args) {
		this(command, args, StdoutResponseManager.get());
	}
}
