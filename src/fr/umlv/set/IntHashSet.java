package fr.umlv.set;

import java.util.Objects;
import java.util.function.IntConsumer;

public class IntHashSet {

	private final int sizePool = 8;
	private final Entry pool[] = new Entry[sizePool];
	private int nbEntrys = 0;

	private record Entry(int value, Entry next) { // final static by default
	} 

	private int hash(int value) {
		return value & (sizePool - 1);
	}

	private boolean isPresent(int value, int bucket) {
		Entry current = pool[bucket];
		while (current != null) {
			if (current.value() == value) {
				return true;
			}
			current = current.next();
		}
		return false;
	}

	public void add(int value) {
		int bucket = hash(value);
		if (pool[bucket] == null) {
			pool[bucket] = new Entry(value, null);
			nbEntrys++;
		} else {
			// checking if value is already in the set
			if (isPresent(value, bucket)) {
				return;
			}
			pool[bucket] = new Entry(value, pool[bucket]);
			nbEntrys++;
		}
	}

	public int size() {
		return nbEntrys;
	}

	public void forEach(IntConsumer consumer) {
		Objects.requireNonNull(consumer);
		if (nbEntrys == 0) {
			return;
		}
		for (int i = 0; i < pool.length; i++) {
			Entry current = pool[i];
			while (current != null) {
				consumer.accept(current.value());
				current = current.next();
			}
		}
	}

	public boolean contains(int value) {
		int bucket = hash(value);
		return isPresent(value, bucket);
	}
}
