package edu.ifmo.tikunov.lab4.collection;

import java.util.ArrayDeque;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Class that manages {@code ArrayDeque}.
 *
 * @param	<E> element type
 * @param	<K> element id type
 * @see		CollectionManager
 * @see		ArrayDeque
 */
@JsonTypeName("array_deque")
public class ArrayDequeManager<E extends Comparable<E> & Identifiable<K> & CreationDateSpecifiable, K extends Comparable<K>>
		extends CollectionManager<E, K> {

	@JsonSetter("collection")
	public void setCollection(ArrayDeque<E> collection) {
		this.collection = collection;
	}

	@Override
	public boolean update(K id, E newElement) {
		E oldElement = get(id);

		if(oldElement == null) return false;

		newElement.setId(id);
		newElement.setCreationDate(oldElement.getCreationDate());

		collection = collection.stream()
				.map(e -> e.getId().equals(id) ? newElement : e)
				.collect(ArrayDeque::new, ArrayDeque::add, ArrayDeque::addAll);

		return true;
	}

	public ArrayDequeManager() {
	}

	public ArrayDequeManager(ArrayDeque<E> collection, IdSupplier<K> idSupplier) {
		super(collection, idSupplier);
	}
}
