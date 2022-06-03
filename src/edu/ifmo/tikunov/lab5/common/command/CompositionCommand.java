package edu.ifmo.tikunov.lab5.common.command;

public class CompositionCommand extends Command {
	private Command[] commands;

	public CompositionCommand(CommandSignature signature, Command... commands) {
		super(signature);
		this.commands = commands;
	}

	@Override
	public void execute(ExecutionQuery query) throws ExitSignal {
		for (Command command : commands) {
			command.execute(new ExecutionQuery(command.signature.name, query.params));
		}
	}
}
