package edu.ifmo.tikunov.lab5.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import edu.ifmo.tikunov.lab5.common.composite.Composite;
import edu.ifmo.tikunov.lab5.common.composite.CompositeConstructor;
import edu.ifmo.tikunov.lab5.common.composite.CompositeParser;
import edu.ifmo.tikunov.lab5.common.validate.Description;
import edu.ifmo.tikunov.lab5.common.validate.FieldName;

@Composite
@Description("coordinate")
public class Coordinates implements Serializable {
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
		return CompositeParser.stringValue(this);
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
