package edu.ifmo.tikunov.lab5.server.collection;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonTypeName;

import edu.ifmo.tikunov.lab5.common.model.SpaceMarine;

@JsonTypeName("custom_array_deque")
public class MyArrayDequeManager extends ArrayDequeManager<SpaceMarine, Long> {
	/**
	 * Absolutely useless command.
	 *
	 * @return some weapons sorted... hope it helps.........
	 */
	public String printFieldAscendingWeaponType() {
		if (collection.isEmpty()) return "The collection is empty.";
		return collection.stream()
				.sorted(Comparator.comparing(SpaceMarine::getWeaponType, Comparator.nullsFirst(Comparator.naturalOrder())))
				.map(e -> "\tElement #" + e.getId().toString() + ": " + (e.getWeaponType() == null ?
					"null"
					:
					e.getWeaponType().toString()))
				.collect(Collectors.joining("\n", "Weapon types sorted for each element:\n\n", ""));
	}

	public MyArrayDequeManager() {
	}

	public MyArrayDequeManager(ArrayDeque<SpaceMarine> collection, LongIdSupplier idSupplier) {
		super(collection, idSupplier);
	}
}
