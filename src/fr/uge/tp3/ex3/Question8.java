package fr.uge.tp3.ex3;

import java.util.Scanner;
import java.util.stream.IntStream;

public class Question8 {

	public static void main(String[] args) {
		int nbThreads = 4;

		var threads = IntStream.range(0, nbThreads).mapToObj(i -> new Thread(() -> {
			int cpt = 0;
			for (;;) {
				try {
					Thread.sleep(1_000);
					System.out.println("Thread " + i + " -> compteur = " + cpt);
				} catch (InterruptedException e) {
					return ;
				}
				cpt++;
			}
		})).toList();

		//threads.forEach(t -> t.setDaemon(true));
		threads.forEach(Thread::start);

		System.out.println("enter a thread id:");
		try (var scanner = new Scanner(System.in)) {
			while (scanner.hasNextInt()) {
				var threadId = scanner.nextInt();
				var stopped = threads.get(threadId);
				stopped.interrupt();
			}
		}
	}
}
