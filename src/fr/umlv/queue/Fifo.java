package fr.umlv.queue;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

public class Fifo <E> implements Iterable<E>{
	private int head = 0;
	private int tail = 0;
	private int nbElements = 0;
	private final E[] elements;
	
	public Fifo(int size) {
		if(size <= 0) {
			throw new IllegalArgumentException("size mus be > 0");
		}
		@SuppressWarnings("unchecked")
		E[] elements = (E[]) new Object[size];
		this.elements = elements;
	}
	
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		for(var element : this) {
			sj.add(element.toString());
		}
		return sj.toString();
	}
	
	public void offer(E element) {
		Objects.requireNonNull(element);
		if(nbElements == elements.length) {
			throw new IllegalStateException("Fifo is full");
		}
		elements[tail] = element;
		nbElements++;
		tail++;
		tail %= elements.length;
	}
	
	public E poll() {
		if(nbElements == 0) {
			throw new IllegalStateException("Fifo is empty");
		}
		var element = elements[head];
		elements[head] = null;
		nbElements--;
		head++;
		head %= elements.length;
		return element;
	}
	
	public int size() {
		return nbElements;
	}
	
	public boolean isEmpty() {
		return nbElements == 0;
	}
	
	public Iterator<E> iterator(){
			
		Iterator<E> it = new Iterator<E>() {
			
	        private int itHead = head;    
	        private int itNbElements = nbElements;
	        
			@Override
			public boolean hasNext() {
				return itNbElements != 0;
			}

			@Override
			public E next() {
				if(!hasNext()) {
					throw new NoSuchElementException();
				}
				var element = elements[itHead];
				itNbElements--;
				itHead++;
				itHead %= elements.length;
				return element;
			}}; 
		return it;
	}
}
