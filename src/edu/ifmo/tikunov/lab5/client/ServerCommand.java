package edu.ifmo.tikunov.lab5.client;

import java.io.IOException;
import java.net.InetAddress;

import edu.ifmo.tikunov.lab5.client.network.RequestSender;
import edu.ifmo.tikunov.lab5.client.network.ResponseReader;
import edu.ifmo.tikunov.lab5.common.command.Command;
import edu.ifmo.tikunov.lab5.common.command.CommandRequest;
import edu.ifmo.tikunov.lab5.common.command.CommandSignature;
import edu.ifmo.tikunov.lab5.common.command.ExecutionQuery;
import edu.ifmo.tikunov.lab5.common.command.ExitSignal;
import edu.ifmo.tikunov.lab5.common.command.ResponseManager;
import edu.ifmo.tikunov.lab5.common.command.ServerResponse;

public class ServerCommand extends Command {

	public ServerCommand(CommandSignature signature) {
		super(signature);
	}

	protected void handleResponse(ResponseManager local, ServerResponse response) {
		local.send(response);
	}

	@Override
	public void execute(ExecutionQuery query) throws ExitSignal {
		try (RequestSender sender = new RequestSender(InetAddress.getByName("localhost"), 1234)) {
			CommandRequest payload = new CommandRequest(query);
			try (ResponseReader response = sender.send(payload)) {
				handleResponse(query.response(), response.get());
			}
		} catch (IOException e) {
			throw new ExitSignal("Command can't be executed because the server is down. Please try again later.", 1);
		}
	}
}
