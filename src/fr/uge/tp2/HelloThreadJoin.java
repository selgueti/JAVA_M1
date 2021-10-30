package fr.uge.tp2;

import java.util.stream.IntStream;

public class HelloThreadJoin {

	public static void main(String[] args) throws InterruptedException {

		int nbThreads = 4;

		var threads = IntStream.range(0, nbThreads)
				.mapToObj(
						i -> new Thread(
								() -> IntStream.range(0, 5_000).forEach(
										k -> System.out.println("hello " + i + " " + k)
										)
								)
						).toList();

		threads.forEach(Thread::start);

		for (var thread : threads) {
			thread.join();
		}

		System.out.println("Le programme est terminé");
	}
}
