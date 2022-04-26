package edu.ifmo.tikunov.lab4.collection;

/**
 * Any type that implements this interface has id.
 * @param	<K>	id type
 */
public interface Identifiable<K extends Comparable<K>> {
	void setId(K id);

	K getId();

	boolean idIsSet();
}
