package edu.ifmo.tikunov.lab5.common.command;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FileQueryGenerator extends InputStreamQueryGenerator {

	private int toSkip;
	private String filename;

	@Override
	public List<ExecutionQuery> get(Map<String, Commands> allCommands) throws IOException {
		toSkip++;
		return super.get(allCommands);
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
