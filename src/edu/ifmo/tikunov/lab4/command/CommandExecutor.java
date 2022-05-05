package edu.ifmo.tikunov.lab4.command;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;

/**
 * Listens to user input and executes commands.
 */
public class CommandExecutor {
	private Map<String, Command> commands;
	private Deque<ExecutionQuery> queries;
	private QueryGenerator in;

	/**
	 * Executes every command from the buffer one by one.
	 *
	 * @throws ExitSignal if command sends exit signal during execution
	 */
	private void executeAll() throws ExitSignal {
		while (!queries.isEmpty()) {
			ExecutionQuery query = queries.pop();
			execute(query);
		}
	}

	/**
	 * Executes the command.
	 *
	 * @param query execution query
	 * @throws ExitSignal if command sends exit signal during execution
	 */
	protected void execute(ExecutionQuery query) throws ExitSignal {
		Command command = commands.get(query.command);
		if (command == null) {
			System.err.println("Command not found: " + query.command);
		} else {
			try {
				command.execute(query.params);
			} catch (OutOfMemoryError e) {
				queries.clear();
				System.err.println("Command buffer was cleared because program ran out of memory");
			}
		}
	}


	/**
	 * Adds new command
	 *
	 * @param command new command
	 */
	protected void addCommand(Command command) {
		commands.put(command.name, command);
	}

	/**
	 * Listens to user commands from standard input and executes them.
	 */
	public void listen() {
		while (true) {
			System.out.print("> ");
			try {
				queries.addAll(in.get(commands));
				executeAll();
			} catch (ExitSignal e) {
				if (e.getCode() == 0)
					System.out.println(e.getMessage());
				else
					System.err.println(e.getMessage());
				System.exit(e.getCode());
			} catch (Exception e) {
				System.out.println("Unhandled exception " + e.getClass().toString() + ": " + e.getMessage());
				System.exit(1);
			}
		}
	}

	public CommandExecutor(QueryGenerator in) {
		this.in = in;

		commands = new HashMap<>();
		queries = new ArrayDeque<>();

		addCommand(new Command("help", "prints information about all available commands", new ParameterList()) {
			@Override
			public void execute(Object[] args) {
				System.out.println("Available commands:");

				commands.entrySet().stream()
						.map(e -> e.getValue())
						.forEach(c -> {
							System.out.println("  " + c.name + " - " + c.description);
						});

				System.out.println("\nTo get information about command usage, use \"help [command]\"");
			}
		});

		addCommand(new Command("exit", "stops the program", new ParameterList()) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				throw new ExitSignal("Bye", 0);
			}
		});

		addCommand(new Command("execute_script", "executes the script from some file",
				new ParameterList(new CommandParameter("filename", String.class))) {
			@Override
			public void execute(Object[] args) {
				try {
					FileQueryGenerator file;
					if (String.class.isAssignableFrom(args[0].getClass())) {
						String filename = (String) args[0];
						file = new FileQueryGenerator(filename);
					} else {
						file = (FileQueryGenerator) args[0];
						file.resume();
					}
					try {
						List<ExecutionQuery> queriesFromFile = file.get(commands);
						Collections.reverse(queriesFromFile);
						file.suspend();

						queries.addFirst(new ExecutionQuery("execute_script", new Object[] {file}));
						queriesFromFile.stream()
							.forEach(queries::addFirst);
					} catch (ExitSignal e) {
						// do nothing
					}
				} catch (IOException e) {
					System.err.println("execute_script: " + e.getMessage());
				}
			}
		});
	}
}
