package edu.ifmo.tikunov.lab5.client.network;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

import edu.ifmo.tikunov.lab5.common.ServerRequest;

public class RequestSender implements Closeable {
	protected Socket socket;

	public ResponseReader send(ServerRequest payload) {
		try {
			OutputStream socketOut = socket.getOutputStream();
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bytesOut);
			out.writeObject(payload);

			byte[] bytes = bytesOut.toByteArray();
			int size = bytes.length;
			bytes = ByteBuffer
				.allocate(size + 4)
				.putInt(size)
				.put(bytes)
				.array();
			socketOut.write(bytes);

			InputStream in = socket.getInputStream();
			ResponseReader reader = new ResponseReader(in);
			return reader;
		} catch (IOException e) {
			return ResponseReader.noConnection;
		}
	}

	public void close() {
		try {
			socket.close();
		} catch (Exception e) {
		}
	}

	public RequestSender(InetAddress host, int port) throws IOException {
		socket = new Socket(host, port);
	}
}
