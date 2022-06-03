package edu.ifmo.tikunov.lab5.common.command;

import java.util.Comparator;
import java.util.HashSet;

public class Commands extends HashSet<Command> {

	@Override
	public boolean add(Command e) {
		removeIf(c -> contains(e.signature.parameters.types()));
		return super.add(e);
	}

	public Command get(String params) {
		return stream()
			.sorted(Comparator.comparing(c -> -c.signature.parameters.count()))
			.filter(c ->
				c.signature.parameters.validate(params) ||
				c.signature.parameters.onlySimple().validate(params)
			).findFirst().orElse(null);
	}

	public Command get(Object... args) {
		return stream()
			.filter(c -> c.signature.parameters.validate(args))
			.findAny().orElse(null);
	}

	public boolean contains(Class<?>... types) {
		return stream()
			.filter(c -> c.signature.parameters.matches(types))
			.count() != 0;
	}

	public Commands() {
		super();
	}
}
