package edu.ifmo.tikunov.lab5.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import edu.ifmo.tikunov.lab5.client.network.RequestSender;
import edu.ifmo.tikunov.lab5.client.network.ResponseReader;
import edu.ifmo.tikunov.lab5.common.command.Command;
import edu.ifmo.tikunov.lab5.common.command.CommandRequest;
import edu.ifmo.tikunov.lab5.common.command.CommandSignature;
import edu.ifmo.tikunov.lab5.common.command.ExecutionQuery;
import edu.ifmo.tikunov.lab5.common.command.ExitSignal;
import edu.ifmo.tikunov.lab5.common.command.ResponseFormat;
import edu.ifmo.tikunov.lab5.common.command.ResponseManager;
import edu.ifmo.tikunov.lab5.common.command.ServerResponse;

public class ServerCommand extends Command {

	private InetAddress host;
	private int port;

	public ServerCommand(CommandSignature signature, InetAddress host, int port) {
		super(signature);
		this.host = host;
		this.port = port;
	}

	protected void handleResponse(ResponseManager local, ServerResponse response) {
		local.send(response);
	}

	@Override
	public void execute(ExecutionQuery query) throws ExitSignal {
		try (RequestSender sender = new RequestSender(host, port)) {
			CommandRequest payload = new CommandRequest(query);
			try (ResponseReader reader = sender.send(payload)) {
				ServerResponse response = reader.get();

				if (response.format.equals(ResponseFormat.CORRUPT_REQUEST)) {
					System.err.println("Request corrupt, retrying...");
					execute(query);
					return;
				}

				handleResponse(query.response(), reader.get());
			}
		} catch (IOException e) {
			throw new ExitSignal("Command can't be executed because the server is down. Please try again later.", 1);
		}
	}
}
