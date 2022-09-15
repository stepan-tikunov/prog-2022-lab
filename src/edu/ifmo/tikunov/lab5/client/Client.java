package edu.ifmo.tikunov.lab5.client;

import java.net.UnknownHostException;

public class Client {
	public static void main(String... args) {
		String host = "localhost";
		int port = 1234;
		try {
			host = args[0];
			port = Integer.parseInt(args[1]);
		} catch (IndexOutOfBoundsException e) {}

		try {
			ClientCommandExecutor executor = new ClientCommandExecutor(host, port);
			executor.listen();
		} catch (UnknownHostException e) {
			System.err.println("Host unreachable.");
			System.exit(1);
		}
	}
}
