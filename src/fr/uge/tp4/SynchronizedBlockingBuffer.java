package fr.uge.tp4;

import java.util.ArrayDeque;
import java.util.Objects;

public class SynchronizedBlockingBuffer<E> {

	private final ArrayDeque<E> array;
	private final int size;

	public SynchronizedBlockingBuffer(int size) {
		super();
		if (size < 0) {
			throw new IllegalArgumentException("Size must be > 0");
		}
		this.size = size;
		this.array = new ArrayDeque<E>(size);
	}

	public void put(E e) throws InterruptedException {
		Objects.requireNonNull(e);
		synchronized (array) {
			while (array.size() == size) {
				// throw new IllegalStateException("Buffer is full");
				array.wait();
			}
			array.addLast(e);
			array.notifyAll();
		}
	}

	public E take() throws InterruptedException {
		synchronized (array) {
			while (array.isEmpty()) {
				// throw new IllegalStateException("Buffer is empty");
				array.wait();
			}
			array.notifyAll();
			return array.removeFirst();
		}
	}

	@Override
	public String toString() {
		synchronized (array) {
			return array.toString();
		}
	}
}
