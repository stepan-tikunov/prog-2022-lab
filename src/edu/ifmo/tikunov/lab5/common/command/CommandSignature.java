package edu.ifmo.tikunov.lab5.common.command;

import java.io.Serializable;

public class CommandSignature implements Serializable {
	public final String name;
	public final String description;
	public final ParameterList parameters;
	public final boolean local;
	public final boolean available;

	public static CommandSignature help() {
		return new CommandSignature(
			"help",
			"prints information about all available commands",
			new ParameterList(),
			true
		);
	}

	public static CommandSignature usageHelp() {
		return new CommandSignature(
			"help",
			"prints information about command usage.",
			new ParameterList(new CommandParameter("name", String.class)),
			true
		);
	}

	public static CommandSignature exit() {
		return new CommandSignature(
			"exit",
			"stops the program",
			new ParameterList(),
			true
		);
	}

	public static CommandSignature executeScript() {
		return new CommandSignature(
			"execute_script",
			"executes the script from some file",
			new ParameterList(new CommandParameter("filename", String.class)),
			true
		);
	}

	public static CommandSignature add(Class<?> elementType) {
		return new CommandSignature(
			"add",
			"adds new " + elementType.getSimpleName() + " to the collection",
			new ParameterList(new CommandParameter("new element", elementType))
		);
	}

	public static CommandSignature addIfMin(Class<?> elementType) {
		return new CommandSignature(
			"add_if_min",
			"adds new " + elementType.getSimpleName()
				+ " to the collection if it is less than other elements",
			new ParameterList(new CommandParameter("new element", elementType))
		);
	}

	public static CommandSignature addIfMax(Class<?> elementType) {
		return new CommandSignature(
			"add_if_max",
			"adds new " + elementType.getSimpleName()
				+ " to the collection if it is more than other elements",
			new ParameterList(new CommandParameter("new element", elementType))
		);
	}

	public static CommandSignature update(Class<?> elementType, Class<?> idType) {
		return new CommandSignature(
			"update",
			"updates the element with specified id",
			new ParameterList(
				new CommandParameter("id", idType),
				new CommandParameter("new element", elementType)
			)
		);
	}

	public static CommandSignature remove(Class<?> idType) {
		return new CommandSignature("remove", "removes the element with specified id",
		new ParameterList(new CommandParameter("id", idType)));
	}

	public static CommandSignature removeLower(Class<?> elementType) {
		return new CommandSignature("remove_lower",
		"removes all elements that are less than the specified " + elementType.getSimpleName(),
		new ParameterList(new CommandParameter("element to compare", elementType)));
	}

	public static CommandSignature info() {
		return new CommandSignature(
			"info",
			"shows the information about current collection",
			new ParameterList()
		);
	}

	public static CommandSignature show() {
		return new CommandSignature(
			"show",
			"prints info about elements in collection",
			new ParameterList()
		);
	}

	public static CommandSignature minById() {
		return new CommandSignature(
			"min_by_id",
			"prints info about an element with the smallest id",
			new ParameterList()
		);
	}

	public static CommandSignature save(String whereSaves) {
		return new CommandSignature(
			"save",
			"saves the collection to " + whereSaves,
			new ParameterList(),
			true
		);
	}

	public static CommandSignature clear() {
		return new CommandSignature(
			"clear",
			"clears the collection",
			new ParameterList()
		);
	}

	public static CommandSignature groupCountingById() {
		return new CommandSignature(
			"group_counting_by_id",
			"groups all elements by the value of \"id\" field and prints the number of elements in each group",
			new ParameterList()
		);
	}

	public static CommandSignature printFieldAscendingWeaponTypes() {
		return new CommandSignature(
			"print_field_ascending_weapon_type",
			"prints sorted values of \"weapon_type\" of each element in the collection",
			new ParameterList()
		);
	}

	private CommandSignature(
		String name,
		String description,
		ParameterList parameters,
		boolean local,
		boolean available
	) {
		this.name = name;
		this.description = description;
		this.parameters = parameters;
		this.local = local;
		this.available = available;
	}

	private CommandSignature(String name, String description, ParameterList parameters, boolean local) {
		this(name, description, parameters, local, false);
	}
	private CommandSignature(String name, String description, ParameterList parameters) {
		this(name, description, parameters, false);
	}
}
