package edu.ifmo.tikunov.lab5.server;

import edu.ifmo.tikunov.lab5.common.command.Command;
import edu.ifmo.tikunov.lab5.common.command.CommandSignature;
import edu.ifmo.tikunov.lab5.common.command.CompositionCommand;
import edu.ifmo.tikunov.lab5.common.command.ExecutionQuery;
import edu.ifmo.tikunov.lab5.common.command.ExitSignal;
import edu.ifmo.tikunov.lab5.common.model.SpaceMarine;
import edu.ifmo.tikunov.lab5.server.collection.CollectionCommandExecutor;
import edu.ifmo.tikunov.lab5.server.collection.MyArrayDequeManager;
import edu.ifmo.tikunov.lab5.server.network.NetworkQueryGenerator;
import edu.ifmo.tikunov.lab5.server.storage.StorageManager;

/**
 * {@code CollectionCommandExecutor} with some extra commands that are
 * only possible to implement if element type is {@code SpaceMarine}.
 */
public class ServerCommandExecutor extends CollectionCommandExecutor<SpaceMarine, Long> {
	protected MyArrayDequeManager collection;

	public ServerCommandExecutor(MyArrayDequeManager empty, StorageManager<SpaceMarine, Long> storage) {
		super(
			new ServerQueryGenerator(new NetworkQueryGenerator(1234)),
			empty,
			storage,
			SpaceMarine.class,
			Long.class
		);

		try {
			this.collection = (MyArrayDequeManager) super.collection;
		} catch (ClassCastException e) {
			System.err.println("Couldn't parse collection. Created new empty collection");
			this.collection = empty;
			super.collection = empty;
		}

		Command newExitCommand = new CompositionCommand(
			CommandSignature.exit(),
			commands.get("save").get(),
			commands.get("exit").get()
		);

		setCommand(newExitCommand);

		setCommand(new Command(CommandSignature.printFieldAscendingWeaponTypes()) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				query.response().info(collection.printFieldAscendingWeaponType());
			}
		});
	}
}
