package edu.ifmo.tikunov.lab5.client.network;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import edu.ifmo.tikunov.lab5.common.command.ServerResponse;

public class ResponseReader implements Closeable {
	private InputStream in;

	public static final ResponseReader noConnection = new ResponseReader(null) {
		@Override
		public ServerResponse get() {
			return ServerResponse.error("Couldn't establish connection with server.");
		}
	};

	public ServerResponse get() {
		try {
			ObjectInputStream reader = new ObjectInputStream(in);
			Object raw = reader.readObject();
			if (raw instanceof ServerResponse) {
				return (ServerResponse)raw;
			} else {
				return ServerResponse.error("Bad server response.");
			}
		} catch (IOException e) {
			return ServerResponse.error("Error processing response.");
		} catch (ClassNotFoundException e) {
			return ServerResponse.error("Bad server response.");
		}
	}

	@Override
	public void close() {
		try {
			in.close();
		} catch (IOException e) {
		}
	}

	public ResponseReader(InputStream in) {
		this.in = in;
	}
}
