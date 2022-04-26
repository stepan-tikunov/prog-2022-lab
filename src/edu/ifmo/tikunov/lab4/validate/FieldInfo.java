package edu.ifmo.tikunov.lab4.validate;

import java.lang.reflect.Field;

/**
 * Information about composite type's field.
 */
public class FieldInfo {
	public final Field field;
	public final String name;
	public final String description;
	public final Constraint[] constraints;

	public FieldInfo(Field field, String name, String description, Constraint[] constraints) {
		this.field = field;
		this.name = name;
		this.description = description;
		this.constraints = constraints;
	}
}
