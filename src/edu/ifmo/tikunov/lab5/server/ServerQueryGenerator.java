package edu.ifmo.tikunov.lab5.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.ifmo.tikunov.lab5.common.command.Commands;
import edu.ifmo.tikunov.lab5.common.command.ExecutionQuery;
import edu.ifmo.tikunov.lab5.common.command.QueryGenerator;
import edu.ifmo.tikunov.lab5.common.command.StdinQueryGenerator;
import edu.ifmo.tikunov.lab5.server.network.NetworkQueryGenerator;

public class ServerQueryGenerator implements QueryGenerator {

	NetworkQueryGenerator network;
	StdinQueryGenerator input;

	@Override
	public List<ExecutionQuery> get(Map<String, Commands> allCommands) throws IOException {
		List<ExecutionQuery> networkQueries = network.get(allCommands);
		if (networkQueries.isEmpty()) {
			return input.get(allCommands);
		}
		return networkQueries;
	}

	public ServerQueryGenerator(NetworkQueryGenerator network) {
		this.network = network;
		this.input = new StdinQueryGenerator();
	}
}
