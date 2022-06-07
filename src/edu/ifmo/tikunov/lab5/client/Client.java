package edu.ifmo.tikunov.lab5.client;

import java.net.UnknownHostException;

import edu.ifmo.tikunov.lab5.common.command.StdinQueryGenerator;

public class Client {
	public static void main(String... args) {
		String host = "51.83.165.24";
		int port = 1234;
		try {
			host = args[0];
			port = Integer.parseInt(args[1]);
		} catch (IndexOutOfBoundsException e) {}

		try {
			ClientCommandExecutor executor = new ClientCommandExecutor(new StdinQueryGenerator(), host, port);
			executor.listen();
		} catch (UnknownHostException e) {
			System.err.println("Host unreachable.");
			System.exit(1);
		}
	}
}
