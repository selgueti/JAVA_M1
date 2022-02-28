package fr.umlv.queue;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.StringJoiner;

public class ResizeableFifo <E> extends AbstractQueue<E> implements Iterable<E>{
	private int head = 0;
	private int tail = 0;
	private int nbElements = 0;
	private E[] elements;
	
	public ResizeableFifo(int size) {
		if(size <= 0) {
			throw new IllegalArgumentException("size mus be > 0");
		}
		@SuppressWarnings("unchecked")
		E[] elements = (E[]) new Object[size];
		this.elements = elements;
	}
	
//	@Override
//	public String toString() {
//		if(nbElements == 0) {
//			return "[]";
//		}
//		StringJoiner sj = new StringJoiner(", ", "[", "]");
//		if(head < tail) {
//			for (int j = head; j < tail; j++) {
//				sj.add(elements[j].toString());
//			}	
//		}else {
//			for (int j = head; j < elements.length; j++) {
//				sj.add(elements[j].toString());
//			}
//			for (int j = 0; j < head; j++) {
//				sj.add(elements[j].toString());
//			}
//		}
//		return sj.toString();
//	}
	
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "[", "]");
		for(var element : this) {
			sj.add(element.toString());
		}
		return sj.toString();
	}
	
	private void grow() {
		if(head < tail) {
			elements = Arrays.copyOf(elements, elements.length * 2);
		}else {
			int lastSize = elements.length;
			@SuppressWarnings("unchecked")
			E[] tmp = (E[]) new Object[lastSize];
			
			System.arraycopy(elements, head, tmp, 0, lastSize - head);
			System.arraycopy(elements, 0, tmp, lastSize - head, head);
			head = 0;
			tail = lastSize;
			elements = Arrays.copyOf(tmp, elements.length * 2);
		}
	}
	
	@Override
	public boolean offer(E element) {
		Objects.requireNonNull(element);
		if(nbElements == elements.length) {
			grow();
//			throw new IllegalStateException("Fifo is full");
		}
		elements[tail] = element;
		nbElements++;
		tail++;
		tail %= elements.length;
		return true;
	}
	
	public E poll() {
		if(nbElements == 0) {
			return null;
			//throw new IllegalStateException("Fifo is empty");
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

	@Override
	public E peek() {
		return poll();
	}
}
