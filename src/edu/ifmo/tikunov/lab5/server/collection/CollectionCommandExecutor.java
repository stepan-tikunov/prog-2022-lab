package edu.ifmo.tikunov.lab5.server.collection;

import edu.ifmo.tikunov.lab5.common.CreationDateSpecifiable;
import edu.ifmo.tikunov.lab5.common.Identifiable;
import edu.ifmo.tikunov.lab5.common.command.Command;
import edu.ifmo.tikunov.lab5.common.command.CommandExecutor;
import edu.ifmo.tikunov.lab5.common.command.CommandSignature;
import edu.ifmo.tikunov.lab5.common.command.ExecutionQuery;
import edu.ifmo.tikunov.lab5.common.command.ExitSignal;
import edu.ifmo.tikunov.lab5.common.command.QueryGenerator;
import edu.ifmo.tikunov.lab5.common.command.ResponseFormat;
import edu.ifmo.tikunov.lab5.server.storage.StorageManager;

/**
 * {@code CommandExecutor} that manipulates collection
 * @param <E> collection element type
 * @param <K> element id type
 */
public class CollectionCommandExecutor<
		E extends	Comparable<E> &
					Identifiable<K> &
					CreationDateSpecifiable,
		K extends 	Comparable<K>>
	extends CommandExecutor {

	final protected Class<E> elementType;
	final protected Class<K> idType;
	protected CollectionManager<E, K> collection;
	protected StorageManager<E, K> storage;

	@SuppressWarnings("unchecked")
	public CollectionCommandExecutor(
		QueryGenerator in,
		CollectionManager<E, K> empty,
		StorageManager<E, K> storage, Class<E> elementType,
		Class<K> idType
	) {
		super(in);
		this.elementType = elementType;
		this.idType = idType;
		this.storage = storage;
		collection = storage.read(empty);

		setCommand(new Command(CommandSignature.add(elementType)) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				E newElement = (E) query.params[0];
				collection.add(newElement);
				query.response().info(ResponseFormat.ADDED);
			}
		});

		setCommand(new Command(CommandSignature.addIfMin(elementType)) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				E newElement = (E) query.params[0];
				boolean result = collection.addIfMin(newElement);
				if (result) {
					query.response().info(ResponseFormat.ADDED);
				} else {
					query.response().error(ResponseFormat.NOT_MIN);
				}
			}
		});

		setCommand(new Command(CommandSignature.addIfMax(elementType)) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				E newElement = (E) query.params[0];
				boolean result = collection.addIfMax(newElement);
				if (result) {
					query.response().info(ResponseFormat.ADDED);
				} else {
					query.response().error(ResponseFormat.NOT_MAX);
				}
			}
		});

		setCommand(new Command(CommandSignature.update(elementType, idType)) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				K id = (K) query.params[0];
				E newElement = (E) query.params[1];
				if (collection.update(id, newElement)) {
					query.response().info(ResponseFormat.UPDATED);
				} else {
					query.response().error(ResponseFormat.NOT_UPDATED);
				}
			}
		});

		setCommand(new Command(CommandSignature.remove(idType)) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				K id = (K) query.params[0];
				if (collection.remove(id)) {
					query.response().info(ResponseFormat.REMOVED);
				} else {
					query.response().error(ResponseFormat.NOT_REMOVED);
				}
			}
		});

		setCommand(new Command(CommandSignature.removeLower(elementType)) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				E element = (E) query.params[0];
				int count = collection.removeLower(element);
				if (count == 1) {
					query.response().info(ResponseFormat.ONE_REMOVED);
				} else if (count > 1) {
					query.response().info(ResponseFormat.REMOVE_COUNT, count);
				} else {
					query.response().error(ResponseFormat.ZERO_REMOVED, elementType.getSimpleName());
				}
			}
		});

		setCommand(new Command(CommandSignature.info()) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				query.response().info(
					ResponseFormat.INFO,
					collection.toString(),
					elementType.getSimpleName(),
					collection.getCreationDate(),
					collection.count()
				);
			}
		});

		setCommand(new Command(CommandSignature.show()) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				if(collection.count() == 0) {
					query.response().info(ResponseFormat.EMPTY);
				} else {
					query.response().info(ResponseFormat.SHOW, collection.get());
				}
			}
		});

		setCommand(new Command(CommandSignature.minById()) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				E minById = collection.minById();
				if (minById == null) {
					query.response().info(ResponseFormat.EMPTY);
				} else {
					query.response().info(ResponseFormat.MIN_BY_ID, minById.getId(), minById);
				}
			}
		});

		setCommand(new Command(CommandSignature.save(storage.toString())) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				storage.save(collection);
			}
		});

		setCommand(new Command(CommandSignature.clear()) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				collection.clear();
				query.response().info(ResponseFormat.CLEARED);
			}
		});

		setCommand(new Command(CommandSignature.groupCountingById()) {
			@Override
			public void execute(ExecutionQuery query) throws ExitSignal {
				query.response().info(collection.groupCountingById());
			}
		});
	}
}
