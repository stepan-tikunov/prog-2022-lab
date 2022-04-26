package edu.ifmo.tikunov.lab4.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import edu.ifmo.tikunov.lab4.composite.Composite;
import edu.ifmo.tikunov.lab4.composite.CompositeConstructor;
import edu.ifmo.tikunov.lab4.validate.Description;
import edu.ifmo.tikunov.lab4.validate.FieldName;

@Composite
@Description("coordinate")
public class Coordinates {
	@FieldName("x")
	@Description("x")
	private Float x;

	@FieldName("y")
	@Description("y")
	private Float y;

	@JsonSetter("x")
	public void setX(Float x) {
		this.x = x;
	}

	@JsonSetter("y")
	public void setY(Float y) {
		this.y = y;
	}

	@JsonGetter("x")
	public Float getX() {
		return x;
	}

	@JsonGetter("y")
	public Float getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Coordinates<" +
				"x=" + x.toString() + ", " +
				"y=" + y.toString() + ">";
	}

	public Coordinates() {
	}

	@CompositeConstructor
	public Coordinates(
			@FieldName("x") Float x,
			@FieldName("y") Float y) {
		this.x = x;
		this.y = y;
	}
}
