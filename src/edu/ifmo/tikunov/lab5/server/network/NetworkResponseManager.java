package edu.ifmo.tikunov.lab5.server.network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import edu.ifmo.tikunov.lab5.common.command.ResponseManager;
import edu.ifmo.tikunov.lab5.common.command.ServerResponse;

public class NetworkResponseManager extends ResponseManager {

	private SocketChannel socket;

	@Override
	public void send(ServerResponse response) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream serializer = new ObjectOutputStream(out);
			serializer.writeObject(response);
			byte[] bytes = out.toByteArray();
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			socket.write(buf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {}
	}

	public NetworkResponseManager(SocketChannel socket) {
		this.socket = socket;
	}

}
