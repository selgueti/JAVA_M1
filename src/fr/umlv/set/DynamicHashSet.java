package fr.umlv.set;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

public class DynamicHashSet<E> {
	private int sizePool = 8;
	//private final Entry<E>[] pool;
	private Entry<E>[] pool;
	private int nbEntrys = 0;

	public DynamicHashSet() {
		@SuppressWarnings("unchecked")
		Entry<E>[] entries = (Entry<E>[]) new Entry<?>[sizePool];
		pool = entries;
	}

	private record Entry<E> (E value, Entry<E> next) { // final static by default
	}

	private int hash(int value) {
		Objects.requireNonNull(value);
		return value & (sizePool - 1);
	}

	private boolean isPresent(Object o, int bucket) {
		Entry<E> current = pool[bucket];
		while (current != null) {
			if (o.equals(current.value())) {
				return true;
			}
			current = current.next();
		}
		return false;
	}

	public void add(E value) {
		Objects.requireNonNull(value);
		if(nbEntrys > sizePool /2) {
			sizePool *= 2;
			@SuppressWarnings("unchecked")
			Entry<E>[] entries = (Entry<E>[]) new Entry<?>[sizePool];
			for (int i = 0; i < pool.length; i++) {
				Entry<E> current = pool[i];
				while(current != null) {
					int newBucket = hash(current.hashCode());
					entries[newBucket] = new Entry<E>(current.value(), entries[newBucket]);
					current = current.next();
				}
			}
			pool = entries;			
		}
		int bucket = hash(value.hashCode());
		if (!isPresent(value, bucket)) {
			pool[bucket] = new Entry<E>(value, pool[bucket]);
			nbEntrys++;
		}
	}

	public int size() {
		return nbEntrys;
	}

	public void forEach(Consumer<? super E> consumer) {
		Objects.requireNonNull(consumer);
		if (nbEntrys == 0) {
			return;
		}
		for (int i = 0; i < pool.length; i++) {
			Entry<E> current = pool[i];
			while (current != null) {
				consumer.accept(current.value());
				current = current.next();
			}
		}
	}

	public boolean contains(Object o) {
		Objects.requireNonNull(o);
		int bucket = hash(o.hashCode());
		return isPresent(o, bucket);
	}
	
	public void addAll(Collection<? extends E> collection) {
		Objects.requireNonNull(collection);
		collection.forEach(this::add);
	}
}
