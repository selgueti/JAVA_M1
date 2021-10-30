package fr.uge.tp2;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class HelloListBug {

	public static void main(String[] args) throws InterruptedException {
		int nbThreads = 4;
		//var numbers = new ArrayList<Integer>(nbThreads * 5_000);
		var numbers = new ArrayList<Integer>();

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
