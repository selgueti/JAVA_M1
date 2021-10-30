package fr.uge.tp3.conc;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class PhilosopherDinner {
	private final ReentrantLock[] forks;
	private final Object lock = new Object();

	public PhilosopherDinner(int forkCount) {
		var forks = new ReentrantLock[forkCount];
		Arrays.setAll(forks, i -> new ReentrantLock());
		this.forks = forks;
	}

	public void eat(int index) {
		var fork1 = forks[index];
		var fork2 = forks[(index + 1) % forks.length];

		synchronized (lock) {
			fork1.lock();
			try {
				fork2.lock();
				try {
					System.out.println("philosopher " + index + " eat");
				} finally {
					fork2.unlock();
				}
			} finally {
				fork1.unlock();
			}
		}
	}

	public static void main(String[] args) {
		var dinner = new PhilosopherDinner(5);
		IntStream.range(0, 5).forEach(i -> {
			new Thread(() -> {
				for (;;) {
					dinner.eat(i);
				}
			}).start();
		});
	}
}
