package edu.ifmo.tikunov.lab5.common.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class InputStreamQueryGenerator implements QueryGenerator {
	protected Scanner scanner;
	protected InputStream in;

	@Override
	public List<ExecutionQuery> get(Map<String, Commands> allCommands) throws IOException {
		if (scanner.hasNextLine()) {
			String[] rawQueries = scanner.nextLine().split(";+");
			List<ExecutionQuery> queries = new ArrayList<>();
			for (String raw : rawQueries) {
				raw = raw.trim();
				String[] parts = raw.split("\\s+", 2);
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

				if (!allCommands.containsKey(commandName)) {
					System.err.println("Command not found: " + commandName);
					continue;
				}

				Command command = allCommands.get(commandName).get(paramsRaw);

				if (command == null) {
					System.err.println("Bad parameters. Try 'help " + commandName + "' for more information.");
					continue;
				}

				try {
					Object[] params = command.signature.parameters.parse(paramsRaw);
					ExecutionQuery query = new ExecutionQuery(commandName, params);
					queries.add(query);
				} catch (BadParametersException e) {
					System.err.println("Bad parameters. Try 'help " + commandName + "' for more information.");
				}
			}
			return queries;
		} else {
			throw new ExitSignal("Got ^D. Exiting", 0);
		}
	}

	public InputStreamQueryGenerator(InputStream in) {
		this.in = in;
		this.scanner = new Scanner(in);
	}

}
