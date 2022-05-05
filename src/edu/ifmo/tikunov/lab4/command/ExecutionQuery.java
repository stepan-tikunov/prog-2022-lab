package edu.ifmo.tikunov.lab4.command;

public class ExecutionQuery {
	public final String command;
	public final Object[] params;

	public ExecutionQuery(String command, Object[] args, QueryGenerator getNextFrom) {
		this.command = command;
		this.params = args;
	}

	public ExecutionQuery(String command, Object[] args) {
		this(command, args, null);
	}
}
