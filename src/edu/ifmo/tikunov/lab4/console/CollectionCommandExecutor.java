package edu.ifmo.tikunov.lab4.console;

import edu.ifmo.tikunov.lab4.collection.CollectionManager;
import edu.ifmo.tikunov.lab4.collection.CreationDateSpecifiable;
import edu.ifmo.tikunov.lab4.collection.Identifiable;
import edu.ifmo.tikunov.lab4.storage.StorageManager;

/**
 * {@code CommandExecutor} that manipulates collection
 * @param <E> collection element type
 * @param <K> element id type
 */
public class CollectionCommandExecutor<E extends Comparable<E> & Identifiable<K> & CreationDateSpecifiable, K extends Comparable<K>>
		extends CommandExecutor {
	final protected Class<E> elementType;
	final protected Class<K> idType;
	protected CollectionManager<E, K> collection;
	protected StorageManager<E, K> storage;

	@SuppressWarnings("unchecked")
	public CollectionCommandExecutor(CollectionManager<E, K> empty, StorageManager<E, K> storage, Class<E> elementType,
			Class<K> idType) {
		super();
		this.elementType = elementType;
		this.idType = idType;
		this.storage = storage;
		collection = storage.read(empty);

		addCommand(new Command("add", "adds new " + elementType.getSimpleName() + " to the collection",
				new ParameterList(this::input, new CommandParameter("new element", elementType))) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				E newElement = (E) args[0];
				collection.add(newElement);
				System.out.println("Element was added to the collection.");
			}
		});

		addCommand(new Command("add_if_min",
				"adds new " + elementType.getSimpleName() + " to the collection if it is less than other elements",
				new ParameterList(this::input, new CommandParameter("new element", elementType))) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				E newElement = (E) args[0];
				boolean result = collection.addIfMin(newElement);
				if (result) {
					System.out.println("Element was added to the collection.");
				} else {
					System.err.println("Element was not added to the collection because it is not less than others.");
				}
			}
		});

		addCommand(new Command("add_if_max",
				"adds new " + elementType.getSimpleName() + " to the collection if it is more than other elements",
				new ParameterList(this::input, new CommandParameter("new element", elementType))) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				E newElement = (E) args[0];
				boolean result = collection.addIfMax(newElement);
				if (result) {
					System.out.println("Element was added to the collection.");
				} else {
					System.err.println("Element was not added to the collection because it is not more than others.");
				}
			}
		});

		addCommand(new Command("update", "updates the element with specified id", new ParameterList(this::input,
				new CommandParameter("id", idType), new CommandParameter("new element", elementType))) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				K id = (K) args[0];
				E newElement = (E) args[1];
				collection.update(id, newElement);
			}
		});

		addCommand(new Command("remove", "removes the element with specified id",
				new ParameterList(this::input, new CommandParameter("id", idType))) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				K id = (K) args[0];
				if (collection.remove(id)) {
					System.out.println("Element was removed from the collection.");
				} else {
					System.err.println("Element with such id was not found in the collection.");
				}
			}
		});

		addCommand(new Command("remove_lower",
				"removes all elements that are less than the specified " + elementType.getSimpleName(),
				new ParameterList(this::input, new CommandParameter("new element", elementType))) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				E element = (E) args[0];
				int count = collection.removeLower(element);
				if (count == 1) {
					System.out.println("1 element was removed from the collection.");
				} else if (count > 1) {
					System.out.println(String.valueOf(count) + " elements were removed from the collection.");
				} else {
					System.err.println("There were no elements that are less than this " + elementType.getSimpleName());
				}
			}
		});

		addCommand(
				new Command("info", "shows the information about current collection", new ParameterList(this::input)) {
					@Override
					public void execute(Object[] args) throws ExitSignal {
						System.out.println(collection.toString() + ":");
						System.out.println("  Element type: " + elementType.getSimpleName());
						System.out.println("  When created: " + collection.getCreationDate().toString());
						System.out.println("  Element count: " + collection.count());
					}
				});

		addCommand(new Command("show", "prints info about elements in collection", new ParameterList(this::input)) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				System.out.println(collection.show());
			}
		});

		addCommand(new Command("min_by_id", "prints info about an element with the smallest id",
				new ParameterList(this::input)) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				E minById = collection.minById();
				if (minById == null) {
					System.out.println("The collection is empty.");
				} else {
					System.out.println(
							"An element with the smallest id in the collection (" + minById.getId().toString() + "):");
					System.out.println(minById.toString());
				}
			}
		});

		addCommand(
				new Command("save", "saves the collection to " + storage.toString(), new ParameterList(this::input)) {
					@Override
					public void execute(Object[] args) throws ExitSignal {
						storage.save(collection);
						System.out.println("The collection was saved to " + storage.toString() + ".");
					}
				});

		addCommand(new Command("clear", "clears the collection", new ParameterList(this::input)) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				collection.clear();
				System.out.println("The collection was cleared.");
			}
		});

		addCommand(new Command("group_counting_by_id",
				"groups all elements by the value of \"id\" field and prints the number of elements in each group",
				new ParameterList(this::input)) {
			@Override
			public void execute(Object[] args) throws ExitSignal {
				System.out.println(collection.groupCountingById());
			}
		});
	}
}
