package edu.ifmo.tikunov.lab5.common.command;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StdinQueryGenerator extends InputStreamQueryGenerator {
	private boolean prefix;

	@Override
	public List<ExecutionQuery> get(Map<String, Commands> allCommands) throws IOException {
		if (prefix) {
			System.out.print("> ");
		}

		if (in.available() == 0) {
			prefix = false;
			return null;
		}

		prefix = true;
		return super.get(allCommands);
	}

	public StdinQueryGenerator() {
		super(System.in);
		this.prefix = true;
	}
}
