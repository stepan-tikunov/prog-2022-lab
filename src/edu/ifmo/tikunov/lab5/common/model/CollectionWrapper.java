package edu.ifmo.tikunov.lab5.common.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

import edu.ifmo.tikunov.lab5.common.Identifiable;

public class CollectionWrapper<E extends Identifiable<K>, K extends Comparable<K>> implements Serializable {
	public final Collection<E> collection;

	public String toString() {
		return collection.stream()
			.map(e -> e.getId().toString() + ": " + e.toString())
			.collect(Collectors.joining("\n"));
	}

	public CollectionWrapper(Collection<E> collection) {
		this.collection = collection;
	}
}
