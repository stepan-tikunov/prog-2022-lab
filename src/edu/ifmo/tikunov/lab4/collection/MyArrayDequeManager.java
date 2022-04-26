package edu.ifmo.tikunov.lab4.collection;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonTypeName;

import edu.ifmo.tikunov.lab4.model.SpaceMarine;

@JsonTypeName("custom_array_deque")
public class MyArrayDequeManager extends ArrayDequeManager<SpaceMarine, Long> {
	/**
	 * Absolutely useless command.
	 *
	 * @return some weapons sorted... hope it helps.........
	 */
	public String printFieldAscendingWeaponType() {
		return collection.stream()
				.sorted(Comparator.comparing(SpaceMarine::getWeaponType))
				.map(e -> "  Element #" + e.getId().toString() + ": " + e.getWeaponType().toString())
				.collect(Collectors.joining("\n", "Weapon types sorted for each element:\n\n", ""));
	}

	public MyArrayDequeManager() {
	}

	public MyArrayDequeManager(ArrayDeque<SpaceMarine> collection, LongIdSupplier idSupplier) {
		super(collection, idSupplier);
	}
}
