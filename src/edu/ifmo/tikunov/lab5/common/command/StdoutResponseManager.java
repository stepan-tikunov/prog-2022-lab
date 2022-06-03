package edu.ifmo.tikunov.lab5.common.command;

public class StdoutResponseManager extends ResponseManager {

	private static StdoutResponseManager instance;

	@Override
	public void send(ServerResponse response) {
		if (response.isError) {
			System.err.println(response);
		} else {
			System.out.println(response);
		}
	}

	@Override
	public void close() {}

	private StdoutResponseManager() {}

	public static StdoutResponseManager get() {
		if (instance == null) {
			instance = new StdoutResponseManager();
		}

		return instance;
	}
}
