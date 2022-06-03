package edu.ifmo.tikunov.lab5.server;

import java.util.ArrayDeque;
import java.util.Map;

import edu.ifmo.tikunov.lab5.common.model.SpaceMarine;
import edu.ifmo.tikunov.lab5.server.collection.ArrayDequeManager;
import edu.ifmo.tikunov.lab5.server.collection.LongIdSupplier;
import edu.ifmo.tikunov.lab5.server.collection.MyArrayDequeManager;
import edu.ifmo.tikunov.lab5.server.storage.file.BufferedFileManager;
import edu.ifmo.tikunov.lab5.server.storage.file.FileManager;
import edu.ifmo.tikunov.lab5.server.storage.json.JsonStorageManager;

public class Main {
	public static void main(String... args) {
		Map<String, String> env = System.getenv();
		String filename = env.get("COLLECTION_JSON_FILE");
		if (filename == null) {
			System.err.println(
					"You didn't specify collection's file name in COLLECTION_JSON_FILE environmental variable, using collection.json");
			filename = "collection.json";
		}

		FileManager file;
		file = new BufferedFileManager(filename);
		JsonStorageManager<SpaceMarine, Long> storage =
			new JsonStorageManager<SpaceMarine, Long>(file,
				ArrayDequeManager.class,
				SpaceMarine.class,
				Long.class
			);
		LongIdSupplier idSupplier = new LongIdSupplier();
		MyArrayDequeManager arrayDequeManager = new MyArrayDequeManager(new ArrayDeque<SpaceMarine>(), idSupplier);
		ServerCommandExecutor executor = new ServerCommandExecutor(arrayDequeManager, storage);

		executor.listen();
	}
}
