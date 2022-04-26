package edu.ifmo.tikunov.lab4.collection;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import edu.ifmo.tikunov.lab4.validate.ConstraintValidator;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = ArrayDequeManager.class, name = "array_deque"),
		@JsonSubTypes.Type(value = MyArrayDequeManager.class, name = "custom_array_deque")
})
/**
 * Class that has all essential methods to
 * manage collection.
 *
 * @param	<E> element type
 * @param	<K> element id type
 * @see		ArrayDequeManager
 */
public abstract class CollectionManager<E extends Comparable<E> & Identifiable<K> & CreationDateSpecifiable, K extends Comparable<K>> {
	private IdSupplier<K> idSupplier;
	private LocalDateTime creationDate;

	protected Collection<E> collection;

	protected final Comparator<E> COMPARATOR = new Comparator<E>() {
		@Override
		public int compare(E o1, E o2) {
			return o1.compareTo(o2);
		}
	};

	protected final Comparator<E> ID_COMPARATOR = new Comparator<E>() {
		@Override
		public int compare(E o1, E o2) {
			return o1.getId().compareTo(o2.getId());
		}
	};

	/**
	 * Generates new id.
	 *
	 * @return new free id
	 */
	protected final K generateId() {
		return idSupplier.generateId();
	}

	/**
	 * Resets id.
	 */
	protected final void resetId() {
		idSupplier.reset();
	}

	/**
	 * Get element by id or {@code null} if there is no such element.
	 *
	 * @param id
	 * @return element with specified id
	 */
	protected final E get(K id) {
		return collection.stream()
				.filter(e -> e.getId().equals(id))
				.findFirst().orElse(null);
	}

	/**
	 * Updates element of the collection with the specified id.
	 *
	 * @param id	id of element to update
	 * @param e		element to replace old
	 */
	public abstract void update(K id, E e);

	@JsonSetter("id_supplier")
	public void setIdSupplier(IdSupplier<K> idSupplier) {
		this.idSupplier = idSupplier;
	}

	@JsonSetter("creation_date")
	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	@JsonGetter("id_supplier")
	public IdSupplier<K> getIdSupplier() {
		return idSupplier;
	}

	@JsonGetter("creation_date")
	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	@JsonGetter("collection")
	public Collection<E> getCollection() {
		return collection;
	}

	/**
	 * Get collection size.
	 *
	 * @return collection size
	 */
	public final int count() {
		return collection.size();
	}

	/**
	 * Validates each element, removes those that don't meet constraints.
	 */
	public void validate() {
		Set<K> ids = new HashSet<>();

		List<E> toRemove = new ArrayList<>();

		for (E element : collection) {
			String validationResult = ConstraintValidator.validate(element, element.getClass());
			if (!validationResult.equals("")) {
				toRemove.add(element);
				System.err.print("The element with id=" + element.getId().toString() + ": " + validationResult);
				System.err.println("It was removed from the collection.");
			} else if (ids.contains(element.getId())) {
				toRemove.add(element);
				System.err.println("The element with id=" + element.getId().toString()
						+ " was removed from the collection because there already is an element with such id.");
			} else {
				ids.add(element.getId());
				idSupplier.update(element.getId());
			}
		}

		toRemove.stream()
				.forEach(collection::remove);
	}

	/**
	 * Adds new element to the collection
	 *
	 * @param newElement new element
	 */
	public void add(E newElement) {
		newElement.setId(generateId());
		collection.add(newElement);
	}

	/**
	 * Adds new element if it will be max.
	 *
	 * @param	e new element
	 * @return	{@code true} if element was added
	 */
	public boolean addIfMax(E e) {
		if (e.compareTo(collection.stream().max(COMPARATOR).orElse(null)) >= 0) {
			add(e);
			return true;
		}
		return false;
	}

	/**
	 * Adds new element if it will be min.
	 *
	 * @param	e new element
	 * @return	{@code true} if element was added
	 */
	public boolean addIfMin(E e) {
		if (e.compareTo(collection.stream().min(COMPARATOR).orElse(null)) <= 0) {
			add(e);
			return true;
		}
		return false;
	}

	/**
	 * Removes element with specified id.
	 *
	 * @param	id id of element to remove from collection
	 * @return	{@code true} if element was removed
	 */
	public boolean remove(K id) {
		E e = get(id);
		return collection.remove(e);
	}

	/**
	 * Removes all elements that are less than specified.
	 *
	 * @param	e element to test others against
	 * @return	number of removed elements
	 */
	public int removeLower(E e) {
		int count = collection.stream()
				.filter(el -> (el.compareTo(e) < 0))
				.map(el -> (collection.remove(el) ? 1 : 0))
				.collect(Collectors.summingInt(Integer::intValue));

		return count;
	}

	/**
	 * Clears the collection.
	 */
	public void clear() {
		collection.clear();
		resetId();
		creationDate = LocalDateTime.now();
	}

	/**
	 * Shows information about each element of the collection.
	 *
	 * @return information about collection elements
	 */
	public String show() {
		if (collection.size() == 0) {
			return "The collection is empty.";
		}

		return collection.stream()
				.sorted(ID_COMPARATOR)
				.map(e -> e.getId().toString() + ": " + e.toString())
				.collect(Collectors.joining("\n", "All elements in collection:\n", ""));
	}

	/**
	 * Groups all elements by value of "id" field
	 * and shows number of elements in each group.
	 *
	 * @return information about groups
	 */
	public String groupCountingById() {
		return collection.stream()
				.sorted(ID_COMPARATOR)
				.map(e -> "id=" + e.getId().toString() + ": 1 element")
				.collect(Collectors.joining("\n",
						"Groups of elements with equal value of \"id\" field have following sizes:\n", ""));
	}

	/**
	 * Returns min element of the collection by "id" field.
	 *
	 * @return element with the smallest id
	 */
	public E minById() {
		return collection.stream().min(ID_COMPARATOR).orElse(null);
	}

	@Override
	public String toString() {
		return collection.getClass().getSimpleName();
	}

	public CollectionManager() {
	}

	public CollectionManager(Collection<E> collection, IdSupplier<K> idSupplier) {
		this.collection = collection;
		this.creationDate = LocalDateTime.now();
		this.idSupplier = idSupplier;
	}
}
