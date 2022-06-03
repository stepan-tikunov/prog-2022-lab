package edu.ifmo.tikunov.lab5.common.command;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface QueryGenerator {
	List<ExecutionQuery> get(Map<String, Commands> allCommands) throws IOException;
}
