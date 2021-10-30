package fr.uge.tp2;

import java.util.stream.IntStream;

public class HelloSafeList {

	public static void main(String[] args) throws InterruptedException {
		int nbThreads = 4;
		var numbers = new ThreadSafeList<Integer>();

		var threads = IntStream.range(0, nbThreads)
				.mapToObj(
						i -> new Thread(
								() -> IntStream.range(0, 5_000).forEach(k -> numbers.add(k))
								)
						).toList();

		threads.forEach(Thread::start);

		for (var thread : threads) {
			thread.join();
		}

		System.out.println("size : " + numbers.size());
	}
}
