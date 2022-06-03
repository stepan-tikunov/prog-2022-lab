package edu.ifmo.tikunov.lab5.server.storage.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;

import edu.ifmo.tikunov.lab5.common.CreationDateSpecifiable;
import edu.ifmo.tikunov.lab5.common.Identifiable;
import edu.ifmo.tikunov.lab5.server.collection.CollectionManager;
import edu.ifmo.tikunov.lab5.server.storage.StorageManager;
import edu.ifmo.tikunov.lab5.server.storage.file.FileManager;

/**
 * Implementation of {@code StorageManager} that saves collections
 * to JSON files via {@code JsonConverter}
 *
 * @param	<E>	element type
 * @param	<K> element id type
 * @see 	StorageManager
 * @see 	JsonConverter
 */
public class JsonStorageManager<E extends Comparable<E> & Identifiable<K> & CreationDateSpecifiable, K extends Comparable<K>>
		implements StorageManager<E, K> {
	private FileManager file;
	private JsonConverter<CollectionManager<E, K>> converter;

	/**
	 * Save collection to JSON file.
	 *
	 * @param collection collection to save
	 */
	@Override
	public void save(CollectionManager<E, K> collection) {
		try {
			String json = converter.serialize(collection);
			file.write(json);
			System.out.println("The collection was saved to " + toString());
		} catch (JacksonException e) {
			System.err.println("Couldn't convert collection to json (probably some mistakes in parser?)");
			System.err.println("Parser message: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Couldn't save collection to file.");
		}
	}

	/**
	 * Read collection from JSON file or return {@code defaultCollection}
	 * if file is empty or there are some syntax mistakes.
	 *
	 * @param	defaultCollection default collection in case of errors
	 * @return	deserialized collection
	 */
	@Override
	public CollectionManager<E, K> read(CollectionManager<E, K> defaultCollection) {
		try {
			String json = file.read();
			CollectionManager<E, K> result = converter.deserialize(json);
			result.validate();

			return result;
		} catch (JacksonException e) {
			System.err.println(
					"Couldn't parse json file (probably some mistakes in syntax?). Created new empty collection.");
		} catch (IOException e) {
			System.err.println("Couldn't read collection from file, created new empty collection.");
		} catch (Exception e) {
			System.err.println("Couldn't parse json file. Created new empty collection");
			e.printStackTrace();
		}
		return defaultCollection;
	}

	@Override
	public String toString() {
		return "JSON file (" + file.toString() + ")";
	}

	@SuppressWarnings("rawtypes")
	public JsonStorageManager(FileManager file, Class<? extends CollectionManager> collectionClass, Class<E> eClass,
			Class<K> kClass) {
		this.file = file;
		this.converter = new JsonConverter<>(collectionClass, eClass, kClass);
	}
}
