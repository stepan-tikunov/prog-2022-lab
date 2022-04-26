package edu.ifmo.tikunov.lab4.storage;

import edu.ifmo.tikunov.lab4.collection.CollectionManager;
import edu.ifmo.tikunov.lab4.collection.CreationDateSpecifiable;
import edu.ifmo.tikunov.lab4.collection.Identifiable;

/**
 * Interface that declares general contract for saving
 * collection to storage.
 *
 * @param 	<E> element type
 * @param 	<K> element id type
 * @see		edu.ifmo.tikunov.lab4.storage.json.JsonStorageManager
 */
public interface StorageManager<E extends Comparable<E> & Identifiable<K> & CreationDateSpecifiable, K extends Comparable<K>> {
	void save(CollectionManager<E, K> collection);

	CollectionManager<E, K> read(CollectionManager<E, K> defaultCollection);
}
