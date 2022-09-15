package edu.ifmo.tikunov.lab5.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.stream.Stream;

import edu.ifmo.tikunov.lab5.client.network.RequestSender;
import edu.ifmo.tikunov.lab5.client.network.ResponseReader;
import edu.ifmo.tikunov.lab5.common.ConnectionRequest;
import edu.ifmo.tikunov.lab5.common.command.CommandExecutor;
import edu.ifmo.tikunov.lab5.common.command.CommandSignature;
import edu.ifmo.tikunov.lab5.common.command.ServerResponse;
import edu.ifmo.tikunov.lab5.common.command.StdinQueryGenerator;

public class ClientCommandExecutor extends CommandExecutor {

	protected final InetAddress ip;
	protected final int port;

	protected void fetchCommands() {
		ConnectionRequest request = new ConnectionRequest();
		try (RequestSender sender = new RequestSender(ip, port)) {
			ResponseReader reader = sender.send(request);
			ServerResponse response = reader.get();
			if (response.matches(CommandSignature[].class)) {
				Stream.of((CommandSignature[])response.body[0])
					.forEach(s -> setCommand(new ServerCommand(s, ip, port)));
			}
		} catch (IOException e) {
			System.err.println("The server is down. Try again later.");
			System.exit(1);
		}
	}

	public ClientCommandExecutor(String host, int port) throws UnknownHostException {
		super(null);

		this.ip = InetAddress.getByName(host);
		this.port = port;

		fetchCommands();
		setQueryGenerator(new StdinQueryGenerator(commands));
	}
}
