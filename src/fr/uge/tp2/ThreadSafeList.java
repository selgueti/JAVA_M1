package fr.uge.tp2;

import java.util.ArrayList;

public final class ThreadSafeList<E> {

	private final ArrayList<E> list = new ArrayList<>();

	public void add(E e) {
		synchronized (list) {
			list.add(e);
		}
	}

	public int size() {
		synchronized (list) {
			return list.size();
		}
	}
}
