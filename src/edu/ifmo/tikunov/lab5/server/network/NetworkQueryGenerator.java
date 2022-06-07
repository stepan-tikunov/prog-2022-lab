package edu.ifmo.tikunov.lab5.server.network;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetBoundException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.ifmo.tikunov.lab5.common.ConnectionRequest;
import edu.ifmo.tikunov.lab5.common.command.Command;
import edu.ifmo.tikunov.lab5.common.command.CommandRequest;
import edu.ifmo.tikunov.lab5.common.command.CommandSignature;
import edu.ifmo.tikunov.lab5.common.command.Commands;
import edu.ifmo.tikunov.lab5.common.command.ExecutionQuery;
import edu.ifmo.tikunov.lab5.common.command.ExitSignal;
import edu.ifmo.tikunov.lab5.common.command.QueryGenerator;
import edu.ifmo.tikunov.lab5.common.command.ResponseFormat;
import edu.ifmo.tikunov.lab5.common.command.ServerResponse;

public class NetworkQueryGenerator implements QueryGenerator, Closeable {

	private ServerSocketChannel server;
	private int port;

	@Override
	public List<ExecutionQuery> get(Map<String, Commands> allCommands) throws IOException {
		try {
			SocketChannel socket = server.accept();
			
			if (socket == null) {
				return new ArrayList<>();
			}

			byte[] bytes = new byte[4];
			ByteBuffer buf = ByteBuffer.wrap(bytes);
			socket.read(buf);
			buf.flip();
			int size = buf.getInt();

			bytes = new byte[size];
			buf = ByteBuffer.wrap(bytes);
			socket.read(buf);

			ByteArrayInputStream is = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(is);
			NetworkResponseManager output = new NetworkResponseManager(socket);

			try {
				Object obj = ois.readObject();
				if (obj instanceof CommandRequest) {
					CommandRequest request = (CommandRequest) obj;
					request.query.setResponseManager(output);

					String commandName = request.query.command;
					if (allCommands.containsKey(commandName)) {
						Command command = allCommands.get(commandName).get(request.query.params);
						if (command != null && !command.signature.local) {
							List<ExecutionQuery> queries = new ArrayList<>();
							queries.add(request.query);
							return queries;
						}
					}
					output.error("No such command was found on server.");
					output.close();
					return new ArrayList<>();
				} else if (obj instanceof ConnectionRequest) {
					Object signatures = allCommands.values().stream()
						.flatMap(m -> m.stream())
						.filter(c -> !c.signature.local)
						.map(c -> c.signature)
						.collect(Collectors.toList())
						.toArray(new CommandSignature[0]);
					output.send(ServerResponse.info(ResponseFormat.NO_MESSAGE, signatures));
				} else {
					output.error("Bad request");
					output.close();
				}
			} catch (ClassNotFoundException e) {
			} catch (StreamCorruptedException e) {
				output.error(ResponseFormat.CORRUPT_REQUEST);
			}
		} catch (NotYetBoundException e) {
			throw new ExitSignal("Port " + port + " is already in use.", 1);
		}
		return new ArrayList<>();
	}

	public NetworkQueryGenerator(int port) {
		this.port = port;
		try {
			server = ServerSocketChannel.open();
			server.socket().bind(new InetSocketAddress(port));
			server.configureBlocking(false);
		} catch (IOException e) {

		}
	}

	@Override
	public void close() throws IOException {
		server.close();
	}

}
