package fr.uge.tp4;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class Main {

	public static void main(String[] args) throws InterruptedException {

//		BlockingQueue<String> linked = new LinkedBlockingQueue<String>(2);
		BlockingQueue<String> array = new ArrayBlockingQueue<String>(2);

		int[] times = { 1_000, 4_000 };

		var threads = IntStream.range(0, times.length).mapToObj(i -> new Thread(() -> {
			for (;;) {
				try {
					Thread.sleep(times[i]);
//					linked.put("Hello : " + i);
					array.put("Hello : " + i);
				} catch (InterruptedException e) {
					return;
				}
				System.out.println("Hello : " + i);
			}
		})).toList();

		threads.forEach(Thread::start);

		for (;;) {
			Thread.sleep(5_000);
//			System.out.println(linked);
			System.out.println(array);
		}
	}

}
