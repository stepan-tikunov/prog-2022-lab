package edu.ifmo.tikunov.lab4.command;

/**
 * {@code Command}'s parameter class
 */
public class CommandParameter {
	public final String name;
	public final Class<?> type;
	public final boolean optional;

	@Override
	public String toString() {
		return "[" + name + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof CommandParameter) {
			CommandParameter p = (CommandParameter) o;

			return name.equals(p.name) && type.equals(p.type);
		}
		return false;
	}

	public CommandParameter(String name, Class<?> type, boolean optional) {
		this.name = name;
		this.type = type;
		this.optional = optional;
	}

	public CommandParameter(String name, Class<?> type) {
		this(name, type, false);
	}
}
