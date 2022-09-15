package edu.ifmo.tikunov.lab5.server;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import edu.ifmo.tikunov.lab5.common.command.Commands;
import edu.ifmo.tikunov.lab5.common.command.ExecutionQuery;
import edu.ifmo.tikunov.lab5.common.command.QueryGenerator;
import edu.ifmo.tikunov.lab5.common.command.StdinQueryGenerator;
import edu.ifmo.tikunov.lab5.server.network.NetworkQueryGenerator;

public class ServerQueryGenerator extends QueryGenerator {

	private NetworkQueryGenerator network;
	private StdinQueryGenerator input;
	private FutureTask<List<ExecutionQuery>> inputTask;
	private Thread otherThread;

	private void resetInputTask() {
		inputTask = new FutureTask<>(() -> input.get());
		otherThread = new Thread(inputTask);
	}

	@Override
	public List<ExecutionQuery> get() throws IOException {
		if (otherThread.getState() == State.NEW)
			otherThread.start();
		if (inputTask.isDone()) {
			try {
				return inputTask.get();
			} catch(ExecutionException e) {
				Throwable cause = e.getCause();
				if (cause instanceof IOException)
					throw (IOException)cause;
			} catch (InterruptedException e) {
			} finally {
				resetInputTask();
			}
		}
		return network.get();
	}

	public ServerQueryGenerator(NetworkQueryGenerator network, Map<String, Commands> allCommands) {
		super(allCommands);
		this.network = network;
		this.input = new StdinQueryGenerator(allCommands);
		resetInputTask();
	}
}
