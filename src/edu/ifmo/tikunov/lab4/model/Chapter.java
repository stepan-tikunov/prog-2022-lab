package edu.ifmo.tikunov.lab4.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import edu.ifmo.tikunov.lab4.composite.Composite;
import edu.ifmo.tikunov.lab4.composite.CompositeConstructor;
import edu.ifmo.tikunov.lab4.composite.CompositeParser;
import edu.ifmo.tikunov.lab4.validate.Constraint;
import edu.ifmo.tikunov.lab4.validate.ConstraintType;
import edu.ifmo.tikunov.lab4.validate.Constraints;
import edu.ifmo.tikunov.lab4.validate.Description;
import edu.ifmo.tikunov.lab4.validate.FieldName;

@Composite
@Description("chapter")
public class Chapter {

	@FieldName("name")
	@Constraints({
		@Constraint(type = ConstraintType.NOT_EQUAL_TO, value = ""),
		@Constraint(type = ConstraintType.NOT_EQUAL_TO, value = " ")
	})
	@Description("name")
	private String name; // !null, !""

	@FieldName("parent_legion")
	@Description("parent legion")
	private String parentLegion;

	@JsonSetter("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonSetter("parent_legion")
	public void setParentLegion(String parentLegion) {
		this.parentLegion = parentLegion;
	}

	@JsonGetter("name")
	public String getName() {
		return name;
	}

	@JsonGetter("parent_legion")
	public String getParentLegion() {
		return parentLegion;
	}

	public String toString() {
		return CompositeParser.stringValue(this);
	}

	public Chapter() {
	}

	@CompositeConstructor
	public Chapter(
			@FieldName("name") String name,
			@FieldName("parent_legion") String parentLegion) {
		this.name = name;
		this.parentLegion = parentLegion;
	}
}
