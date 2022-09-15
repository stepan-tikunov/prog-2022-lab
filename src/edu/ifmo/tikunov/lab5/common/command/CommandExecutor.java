package edu.ifmo.tikunov.lab5.common.command;

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
	private Deque<ExecutionQuery> queries;
	private QueryGenerator generator;
	protected int exitCode = 0;
	protected Map<String, Commands> commands;

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
		Commands commands = this.commands.get(query.command);
		Command command = commands.get(query.params);
		if (command == null) {
			if (query.command.equals("execute_script")) {
				FileQueryGenerator file = (FileQueryGenerator) query.params[0];
				try {
					file.resume();
					executeScript(file);
				} catch (IOException e) {
					query.response().error("execute_script: " + e.getMessage());
				}
			} else {
				query.response().error("Command not found: " + query.command);
				query.response().close();
			}
		} else {
			try {
				command.execute(query);
				query.response().close();
			} catch (OutOfMemoryError e) {
				queries.clear();
				queries.add(query);
			}
		}
	}

	/**
	 * Adds new command
	 *
	 * @param command new command
	 */
	protected void setCommand(Command command) {
		if (commands.containsKey(command.signature.name)) {
			commands.get(command.signature.name).add(command);
		} else {
			Commands newCommands = new Commands();
			newCommands.add(command);
			commands.put(command.signature.name, newCommands);
		}
	}

	protected void setQueryGenerator(QueryGenerator generator) {
		this.generator = generator;
	}

	protected void executeScript(FileQueryGenerator file) throws IOException {
		try {
			List<ExecutionQuery> queriesFromFile = file.get();
			Collections.reverse(queriesFromFile);
			file.suspend();

			queries.addFirst(new ExecutionQuery("execute_script", new Object[] {file}));
			queriesFromFile.stream()
				.forEach(queries::addFirst);
		} catch (ExitSignal e) {
			//do notning
		}
	}

	protected void resetCommands() {
		commands.clear();

		setCommand(new Command(CommandSignature.help()) {
			@Override
			public void execute(ExecutionQuery query) {
				query.response().info("Available commands:");

				commands.values().stream()
						.flatMap(m -> m.stream())
						.forEach(c -> {
							query.response().info("  " + c.signature.name + c.signature.parameters.description());
						});

				query.response().info("\nTo get more information about command, use 'help [command]'");
			}
		});

		setCommand(new Command(CommandSignature.usageHelp()) {
			@Override
			public void execute(ExecutionQuery query) {
				if (commands.containsKey(query.params[0])) {
					commands.get(query.params[0]).stream()
						.forEach(c -> {
							query.response().info(c.signature.name + c.signature.parameters.description() + " - " + c.signature.description);
							if (!c.signature.parameters.equals(c.signature.parameters.onlySimple())) {
								query.response().info(c.usage());
							}
						});
				} else {
					query.response().error("Command '" + query.params[0] + "' not found.");
				}
			}
		});

		setCommand(new Command(CommandSignature.exit()) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				throw new ExitSignal("Bye", 0);
			}
		});

		setCommand(new Command(CommandSignature.executeScript()) {
			@Override
			public void execute(ExecutionQuery query) {
				try {
					String filename = (String) query.params[0];
					FileQueryGenerator file = new FileQueryGenerator(filename, commands);
					executeScript(file);
				} catch (IOException e) {
					query.response().error("execute_script: " + e.getMessage());
				}
			}
		});
	}

	protected void removeCommand(String commandName) {
		commands.remove(commandName);
	}

	public void onExit() {}

	/**
	 * Listens to user commands from standard input and executes them.
	 */
	public void listen() {
		while (true) {
			try {
				List<ExecutionQuery> queriesToAdd = generator.get();
				if (queriesToAdd != null) {
					queries.addAll(queriesToAdd);
					executeAll();
				}
			} catch (ExitSignal e) {
				exitCode = e.getCode();
				if (exitCode == 0)
					System.out.println(e.getMessage());
				else
					System.err.println(e.getMessage());
				System.exit(exitCode);
			} catch (Exception e) {
				System.err.println("Unhandled exception " + e.getClass().toString() + ": " + e.getMessage());
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	public CommandExecutor(QueryGenerator in) {
		this.generator = in;

		commands = new HashMap<>();
		queries = new ArrayDeque<>();

		resetCommands();
	}
}
