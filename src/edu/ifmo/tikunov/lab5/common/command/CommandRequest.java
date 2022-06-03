package edu.ifmo.tikunov.lab5.common.command;

import edu.ifmo.tikunov.lab5.common.ServerRequest;

public class CommandRequest implements ServerRequest {
	public final ExecutionQuery query;
	// public final Client from;

	public CommandRequest(ExecutionQuery query) { //, Client from) {
		this.query = query;
		// this.from = from;
	}
}
