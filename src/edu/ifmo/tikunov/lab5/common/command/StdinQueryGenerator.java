package edu.ifmo.tikunov.lab5.common.command;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StdinQueryGenerator extends InputStreamQueryGenerator {
	private boolean prefix;
	@Override
	public List<ExecutionQuery> get() throws IOException {
		if (prefix) System.out.print("> ");
		prefix = false;
		List<ExecutionQuery> result = super.get();
		prefix = true;
		return result;
	}

	public StdinQueryGenerator(Map<String, Commands> allCommands) {
		super(System.in, allCommands);
		prefix = true;
		interactive = true;
	}
}
