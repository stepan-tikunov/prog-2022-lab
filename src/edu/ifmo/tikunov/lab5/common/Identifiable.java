package edu.ifmo.tikunov.lab5.common;

/**
 * Any type that implements this interface has id.
 * @param	<K>	id type
 */
public interface Identifiable<K extends Comparable<K>> {
	void setId(K id);

	K getId();

	boolean idIsSet();
}
