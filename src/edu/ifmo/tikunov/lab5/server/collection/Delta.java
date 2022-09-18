package edu.ifmo.tikunov.lab5.server.collection;

import java.util.stream.Stream;

public final class Delta<E> {
	public static enum Type {
		ADD,
		UPDATE,
		REMOVE
	}

	public final Type type;
	public final Stream<E> newElement;

	public static <E> Delta<E> add(E newElement) {
		return new Delta<>(Type.ADD, newElement);
	}

	public static <E> Delta<E> update(E newElement) {
		return new Delta<>(Type.UPDATE, newElement);
	}

	public static <E> Delta<E> remove(E newElement) {
		return new Delta<>(Type.REMOVE, newElement);
	}

	public static <E> Delta<E> add(Stream<E> newElement) {
		return new Delta<>(Type.ADD, newElement);
	}

	public static <E> Delta<E> update(Stream<E> newElement) {
		return new Delta<>(Type.UPDATE, newElement);
	}

	public static <E> Delta<E> remove(Stream<E> newElement) {
		return new Delta<>(Type.REMOVE, newElement);
	}

	private Delta(Type type, Stream<E> newElements) {
		this.type = type;
		this.newElement = newElements;
	}
	private Delta(Type type, E newElement) {
		this(type, Stream.of(newElement));
	}
}
