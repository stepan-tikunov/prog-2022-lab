package edu.ifmo.tikunov.lab4.validate;

/**
 * Constraint type.
 */
public enum ConstraintType {
	MORE_THAN("more than", ">"),
	NOT_LESS_THAN("not less than", ">="),
	LESS_THAN("less than", "<"),
	NOT_MORE_THAN("not more than", "<="),
	EQUAL_TO("equal to", "="),
	NOT_EQUAL_TO("not equal to", "!=");

	public String value;
	public String shortValue;

	public String toString() {
		return value;
	}

	ConstraintType(String value, String shortValue) {
		this.value = value;
		this.shortValue = shortValue;
	}
}
