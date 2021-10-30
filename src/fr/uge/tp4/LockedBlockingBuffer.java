package fr.uge.tp4;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockedBlockingBuffer<E> {

	private final ArrayDeque<E> buffer;
	private final int size;
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition isFull = lock.newCondition();
	private final Condition isEmpty = lock.newCondition();

	public LockedBlockingBuffer(int size) {
		if (size < 0) {
			throw new IllegalArgumentException("Size must be > 0");
		}
		this.size = size;
		this.buffer = new ArrayDeque<E>(size);
	}

	public void put(E e) throws InterruptedException {
		Objects.requireNonNull(e);
		lock.lock();
		try {
			while (buffer.size() == size) {
				isFull.await();
			}
			buffer.addLast(e);
			isEmpty.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public E take() throws InterruptedException {
		lock.lock();
		try {
			while (buffer.isEmpty()) {
				isEmpty.await();
			}
			isFull.signalAll();
			return buffer.removeFirst();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String toString() {
		lock.lock();
		try {
			return buffer.toString();
		} finally {
			lock.unlock();
		}
	}
}
