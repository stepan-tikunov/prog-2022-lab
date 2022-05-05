package edu.ifmo.tikunov.lab4.command;

import edu.ifmo.tikunov.lab4.collection.MyArrayDequeManager;
import edu.ifmo.tikunov.lab4.model.SpaceMarine;
import edu.ifmo.tikunov.lab4.storage.StorageManager;

/**
 * {@code CollectionCommandExecutor} with some extra commands that are
 * only possible to implement if element type is {@code SpaceMarine}.
 */
public class MyCollectionCommandExecutor extends CollectionCommandExecutor<SpaceMarine, Long> {
	protected MyArrayDequeManager collection;

	public MyCollectionCommandExecutor(MyArrayDequeManager empty, StorageManager<SpaceMarine, Long> storage) {
		super(new QueryGenerator(System.in), empty, storage, SpaceMarine.class, Long.class);
		try {
			this.collection = (MyArrayDequeManager) super.collection;
		} catch (ClassCastException e) {
			System.err.println("Couldn't parse collection. Created new empty collection");
			this.collection = empty;
			super.collection = empty;
		}

		addCommand(new Command("print_field_ascending_weapon_type",
				"prints sorted values of \"weapon_type\" of each element in the collection",
				new ParameterList()) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				System.out.println(collection.printFieldAscendingWeaponType());
			}
		});
	}
}
