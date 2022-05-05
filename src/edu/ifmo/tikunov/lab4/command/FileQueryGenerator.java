package edu.ifmo.tikunov.lab4.command;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileQueryGenerator extends QueryGenerator {

	private int toSkip;
	private String filename;

	@Override
	public List<ExecutionQuery> get(Map<String, Command> allCommands) throws IOException {
		toSkip++;
		List<ExecutionQuery> result = super.get(allCommands);
		result = result.stream()
			.map(q -> new ExecutionQuery(q.command, q.params, this))
			.collect(Collectors.toList());
		return result;
	}

	public void suspend() throws IOException {
		scanner.close();
	}

	public void resume() throws IOException {
		scanner = new Scanner(new BufferedInputStream(new FileInputStream(filename)));
		for (int i = 0; i < toSkip; ++i) {
			scanner.nextLine();
		}
	}

	public FileQueryGenerator(String filename) throws IOException {
		super(new BufferedInputStream(new FileInputStream(filename)));
		this.filename = filename;
		toSkip = 0;
	}
}
