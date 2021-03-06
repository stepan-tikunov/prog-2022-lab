package edu.ifmo.tikunov.lab5.server.storage.file;

import java.io.IOException;

/**
 * Simple interface to read/write text to file.
 *
 * @see BufferedFileManager
 */
public interface FileManager {
	void write(String text) throws IOException;

	String read() throws IOException;
}
