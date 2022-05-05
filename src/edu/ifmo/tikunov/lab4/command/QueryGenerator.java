package edu.ifmo.tikunov.lab4.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class QueryGenerator {
	protected Scanner scanner;

	public List<ExecutionQuery> get(Map<String, Command> allCommands) throws IOException {
		if (scanner.hasNextLine()) {
			String[] rawQueries = scanner.nextLine().split(";+");
			List<ExecutionQuery> queries = new ArrayList<>();
			for (String raw : rawQueries) {
				String[] parts = raw.split("\\s+");
				String commandName = "", paramsRaw = "";

				switch (parts.length) {
					case 2:
						paramsRaw = parts[1];
					case 1:
						commandName = parts[0];
						break;
				}

				if (commandName.trim().equals("")) {
					continue;
				}

				Command command = allCommands.get(commandName);
				if (command == null) {
					System.err.println("Command not found: " + commandName);
					continue;
				}

				try {
					Object[] params = command.parameterList.parse(paramsRaw);
					ExecutionQuery query = new ExecutionQuery(commandName, params);
					queries.add(query);
				} catch (BadParametersException e) {
					System.err.println(command.usage());
				}
			}
			return queries;
		} else {
			throw new ExitSignal("Got ^D. Exiting", 0);
		}
	}

	public QueryGenerator(InputStream in) {
		this.scanner = new Scanner(in);
	}

}
