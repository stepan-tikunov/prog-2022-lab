package edu.ifmo.tikunov.lab5.client;

import java.net.UnknownHostException;

import edu.ifmo.tikunov.lab5.common.command.StdinQueryGenerator;

public class Client {
	public static void main(String... args) {
		try {
			ClientCommandExecutor executor = new ClientCommandExecutor(new StdinQueryGenerator(), "localhost", 1234);
			executor.listen();
		} catch (UnknownHostException e) {
			System.err.println("Host unreachable.");
			System.exit(1);
		}
	}
}
