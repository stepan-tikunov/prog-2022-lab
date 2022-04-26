package edu.ifmo.tikunov.lab4.console;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Listens to user input and executes commands.
 */
public class CommandExecutor {
	private Map<String, Command> commands;
	private Deque<String> commandBuffer;
	private Scanner in;

	/**
	 * Takes command from standard input and puts it in the command buffer.
	 *
	 * @throws ExitSignal if user sends delimiter
	 */
	private void fromStdin() throws ExitSignal {
		if (in.hasNextLine()) {
			for (String command : in.nextLine().split(";")) {
				commandBuffer.add(command);
			}
		} else {
			throw new ExitSignal("Got delimiter. Exiting", 0);
		}
	}

	/**
	 * Executes every command from the buffer one by one.
	 *
	 * @throws ExitSignal if command sends exit signal during execution
	 */
	private void executeAll() throws ExitSignal {
		while (!commandBuffer.isEmpty()) {
			String currentCommand = commandBuffer.pop();
			execute(currentCommand);
		}
	}

	
	/**
	 * Executes the command from a string.
	 *
	 * @param commandAndParams string that contains information about command and parameters
	 * @throws ExitSignal if command sends exit signal during execution
	 */
	protected void execute(String commandAndParams) throws ExitSignal {
		String[] parts = commandAndParams.trim().split(" ", 2);
		String commandName = "";
		String params = "";

		switch (parts.length) {
			case 2:
				params = parts[1];
			case 1:
				commandName = parts[0];
		}

		if (commandName.equals(""))
			return;

		Command command = commands.get(commandName);
		if (command == null) {
			System.err.println("Command not found: " + commandName);
		} else {
			try {
				command.executeIfValid(params);
			} catch (BadParametersException e) {
				System.err.println(command.usage());
			} catch (OutOfMemoryError e) {
				commandBuffer.clear();
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
	 * An input function that is passed to {@code ParameterList}
	 * 
	 * @param description description of the field
	 * @return user input
	 * @throws ExitSignal if user sends delimiter
	 * @see ParameterList
	 */
	protected String input(String description) throws ExitSignal {
		System.out.print("Enter " + description + ": ");
		if (in.hasNextLine()) {
			return in.nextLine();
		} else {
			throw new ExitSignal("Command interrupted (^D). Exiting", 1);
		}
	}

	/**
	 * Listens to user commands from standard input and executes them.
	 */
	public void listen() {
		while (true) {
			System.out.print("> ");
			try {
				fromStdin();
				executeAll();
			} catch (ExitSignal e) {
				in.close();
				if (e.getCode() == 0)
					System.out.println(e.getMessage());
				else
					System.err.println(e.getMessage());
				System.exit(e.getCode());
			} catch (Exception e) {
				in.close();
				System.out.println("Unhandled exception " + e.getClass().toString() + ": " + e.getMessage());
				System.exit(1);
			}
		}
	}

	public CommandExecutor() {
		in = new Scanner(System.in);
		commands = new TreeMap<>();
		commandBuffer = new ArrayDeque<>();

		addCommand(
				new Command("help", "prints information about all available commands", new ParameterList(this::input)) {
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
		addCommand(new Command("exit", "stops the program", new ParameterList(this::input)) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				throw new ExitSignal("Bye", 0);
			}
		});
		addCommand(new Command("execute_script", "executes the script from some file",
				new ParameterList(this::input, new CommandParameter("filename", String.class))) {
			@Override
			public void execute(Object[] args) {
				String filename = (String) args[0];
				Deque<String> tempCommandBuffer = new ArrayDeque<>();
				try {
					BufferedInputStream in = new BufferedInputStream(new FileInputStream(filename));
					Scanner scanner = new Scanner(in, "UTF-8");
					scanner.useDelimiter(Pattern.compile(";+|(\\r?\\n)+"));
					while (scanner.hasNext()) {
						tempCommandBuffer.add(scanner.next());
					}
					scanner.close();

					while (!tempCommandBuffer.isEmpty()) {
						commandBuffer.addFirst(tempCommandBuffer.removeLast());
					}
				} catch (IOException e) {
					System.err.println("execute_script: " + e.getMessage());
				}
			}
		});
	}
}
