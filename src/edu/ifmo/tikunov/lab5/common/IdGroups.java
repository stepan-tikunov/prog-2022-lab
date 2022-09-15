package edu.ifmo.tikunov.lab5.common;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

public class IdGroups<K extends Comparable<K>> implements Serializable {
	private Map<K, Integer> groups;

	public IdGroups(Map<K, Integer> groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return groups.entrySet().stream()
			.map(e -> String.valueOf(e.getKey()) + ": " + String.valueOf(e.getValue()))
			.collect(Collectors.joining("\n"));
	}
}
