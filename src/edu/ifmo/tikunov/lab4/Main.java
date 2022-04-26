package edu.ifmo.tikunov.lab4;

import java.util.ArrayDeque;
import java.util.Map;

import edu.ifmo.tikunov.lab4.collection.ArrayDequeManager;
import edu.ifmo.tikunov.lab4.collection.LongIdSupplier;
import edu.ifmo.tikunov.lab4.collection.MyArrayDequeManager;
import edu.ifmo.tikunov.lab4.console.MyCollectionCommandExecutor;
import edu.ifmo.tikunov.lab4.model.SpaceMarine;
import edu.ifmo.tikunov.lab4.storage.file.BufferedFileManager;
import edu.ifmo.tikunov.lab4.storage.file.FileManager;
import edu.ifmo.tikunov.lab4.storage.json.JsonStorageManager;

public class Main {
	public static void main(String... args) {
		Map<String, String> env = System.getenv();
		String filename = env.get("COLLECTION_JSON_FILE");
		if (filename == null) {
			System.err.println(
					"You didn't specify collection's file name in COLLECTION_JSON_FILE environmental variable, using collection.json");
			filename = "collection.json";
		}

		FileManager file = new BufferedFileManager(filename);
		JsonStorageManager<SpaceMarine, Long> storage = new JsonStorageManager<SpaceMarine, Long>(file,
				ArrayDequeManager.class, SpaceMarine.class, Long.class);
		LongIdSupplier idSupplier = new LongIdSupplier();
		MyArrayDequeManager arrayDequeManager = new MyArrayDequeManager(new ArrayDeque<SpaceMarine>(), idSupplier);
		MyCollectionCommandExecutor executor = new MyCollectionCommandExecutor(arrayDequeManager, storage);

		executor.listen();
	}
}
