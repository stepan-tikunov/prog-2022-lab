package edu.ifmo.tikunov.lab5.server.storage.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Implementation of {@code FileManager} interface based on
 * {@code BufferedOutputStream} and {@code BufferedInputStream}.
 *
 * @see FileManager
 * @see BufferedOutputStream
 * @see BufferedInputStream
 */
public class BufferedFileManager implements FileManager {
	private File file;

	/**
	 * Writes text to file.
	 *
	 * @param text text to write
	 * @throws IOException if permission denied, output stream closed, etc.
	 */
	@Override
	public void write(String text) throws IOException {
		FileOutputStream fout = new FileOutputStream(file);
		BufferedOutputStream out = new BufferedOutputStream(fout);
		out.write(text.getBytes("UTF-8"));
		out.close();
	}

	/**
	 * Could use {@code InputStream.readAllBytes()} instead, but it is not available in java 8.
	 *
	 * @param	in input stream
	 * @return	all bytes
	 * @throws	IOException if permission denied, input stream closed, etc.
	 */
	protected byte[] readAllBytes(InputStream in) throws IOException {
		final int bufLen = 1024;
		byte[] buf = new byte[bufLen];
		int readLen;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		while ((readLen = in.read(buf, 0, bufLen)) != -1)
			outputStream.write(buf, 0, readLen);

		return outputStream.toByteArray();
	}

	/**
	 * Reads all text from file.
	 *
	 * @return file contents
	 * @throws IOException if permission denied, input stream closed, etc.
	 */
	@Override
	public String read() throws IOException {
		FileInputStream fin = new FileInputStream(file);
		BufferedInputStream in = new BufferedInputStream(fin);
		byte[] bytes = readAllBytes(in);
		String text = new String(bytes, "UTF-8");
		in.close();

		return text;
	}

	public String toString() {
		return file.getAbsolutePath();
	}

	public BufferedFileManager(String filename) {
		file = new File(filename);
	}
}
