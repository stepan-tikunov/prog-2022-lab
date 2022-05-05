package edu.ifmo.tikunov.lab4.collection;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Class that generates {@code Long} ids by incrementing last used id.
 */
@JsonTypeName("long")
public class LongIdSupplier implements IdSupplier<Long> {
	private Long lastUsedId;

	/**
	 * Generates unique id.
	 *
	 * @return generated id
	 */
	@Override
	public Long generateId() {
		++lastUsedId;
		return lastUsedId;
	}

	/**
	 * Update last used id if some id in the collection is more.
	 *
	 * @param someId id of some element in the collection
	 */
	@Override
	public void update(Long someId) {
		if (lastUsedId < someId)
			lastUsedId = someId;
	}

	/**
	 * Resets last used id.
	 */
	@Override
	public void reset() {
		this.lastUsedId = -1L;
	}

	@JsonSetter("last_used_id")
	public void setLastUsedId(Long lastUsedId) {
		this.lastUsedId = lastUsedId;
	}

	@JsonGetter("last_used_id")
	public Long getLastUsedId() {
		return lastUsedId;
	}

	public LongIdSupplier() {
		this(0L);
	}

	public LongIdSupplier(Long lastUsedId) {
		this.lastUsedId = lastUsedId;
	}
}
