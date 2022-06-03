package edu.ifmo.tikunov.lab5.server.storage.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Simple JSON serializer based on Jackson library.
 */
public class JsonConverter<E> {
	private ObjectMapper mapper;
	private ObjectReader reader;
	private ObjectWriter writer;

	/**
	 * Deserialize object from json.
	 *
	 * @param	json json string
	 * @return	deserialized object
	 * @throws	JacksonException if syntax is wrong
	 */
	public E deserialize(String json) throws JacksonException {
		E object = reader.readValue(json);
		return object;
	}

	/**
	 * Serialize object to json.
	 *
	 * @param	object object to serialize
	 * @return	json string
	 * @throws	JacksonException if cannot serialize object
	 */
	public String serialize(E object) throws JacksonException {
		String json = writer.writeValueAsString(object);
		return json;
	}

	public JsonConverter(Class<E> eClass) {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		reader = mapper.readerFor(eClass);
		writer = mapper.writerFor(eClass).withDefaultPrettyPrinter();
	}

	public JsonConverter(TypeReference<E> eClass) {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		reader = mapper.readerFor(eClass);
		writer = mapper.writerFor(eClass).withDefaultPrettyPrinter();
	}

	public JsonConverter(Class<?> parametrized, Class<?>... parameters) {
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		JavaType type = mapper.getTypeFactory().constructParametricType(parametrized, parameters);

		reader = mapper.readerFor(type);
		writer = mapper.writerFor(type).withDefaultPrettyPrinter();
	}
}
