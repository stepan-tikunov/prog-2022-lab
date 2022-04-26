package edu.ifmo.tikunov.lab4.collection;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
 * Interface that declares general contract for generating ids.
 *
 * @param	<K> id type
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes(@JsonSubTypes.Type(value = LongIdSupplier.class, name = "long"))
public interface IdSupplier<K> {
	K generateId();

	void update(K someId);

	void reset();
}
