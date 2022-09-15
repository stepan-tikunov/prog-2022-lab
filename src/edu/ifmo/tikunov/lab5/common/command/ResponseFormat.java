package edu.ifmo.tikunov.lab5.common.command;

public enum ResponseFormat {
	NO_MESSAGE(""),
	CORRUPT_REQUEST(""),
	STRING("%s"),
	ADDED("Element was added to the collection."),
	NOT_MIN("Element was not added to the collection because it is not less than others."),
	NOT_MAX("Element was not added to the collection because it is not more than others."),
	UPDATED("The element was updated."),
	NOT_UPDATED("The element wasn't updated."),
	REMOVED("Element was removed from the collection."),
	NOT_REMOVED("Element with such id was not found in the collection."),
	ONE_REMOVED("1 element was removed from the collection."),
	REMOVE_COUNT("%d elements were removed from the collection."),
	ZERO_REMOVED("There were no elements that are less than this %s"),
	INFO("%s:\n\tElement type: %s\n\tWhen created: %s\n\tElement count: %d"),
	EMPTY("The collection is empty."),
	SHOW("All elements in collection:\n%s"),
	MIN_BY_ID("An element with the least id in the collection (id=%s):\n%s"),
	CLEARED("The collection was cleared."),
	GROUPS("Groups of elements with equal value of \"id\" field have following sizes:\n%s")
	;
	public String format;

	public String toString() {
		return format;
	}

	public String apply(Object... objects) {
		return String.format(format, objects);
	}

	private ResponseFormat(String format) {
		this.format = format;
	}
}
