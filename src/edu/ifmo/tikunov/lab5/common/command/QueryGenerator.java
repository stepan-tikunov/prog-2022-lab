package edu.ifmo.tikunov.lab5.common.command;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class QueryGenerator {
	protected Map<String, Commands> allCommands;
	public abstract List<ExecutionQuery> get() throws IOException;

	public QueryGenerator(Map<String, Commands> allCommands) {
		this.allCommands = allCommands;
	}
}
