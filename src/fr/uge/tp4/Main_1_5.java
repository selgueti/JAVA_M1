package fr.uge.tp4;

import java.util.stream.IntStream;

public class Main_1_5 {

	public static void main(String[] args) throws InterruptedException {

//		BlockingQueue<String> linked = new ArrayBlockingQueue<String>(2);
		var linked = new LockedBlockingBuffer<String>(2);

		final int[] producersTimes = { 1_000, 4_000, 2_000 };
		final int[] consumersTimes = { 100, 300, 500, 700 };

		var producers = IntStream.range(0, producersTimes.length).mapToObj(i -> new Thread(() -> {
			for (;;) {
				try {
					Thread.sleep(producersTimes[i]);
					linked.put("Hello : " + i);
				} catch (InterruptedException e) {
					return;
				}
			}
		})).toList();

		var consumers = IntStream.range(0, consumersTimes.length).mapToObj(i -> new Thread(() -> {
			for (;;) {
				try {
					Thread.sleep(consumersTimes[i]);
					System.out.println(linked.take() + " -> was taking by consumer number " + i);
				} catch (InterruptedException e) {
					return;
				}
			}
		})).toList();

		producers.forEach(Thread::start);
		consumers.forEach(Thread::start);

		for (;;) {
			Thread.sleep(5_000);
			System.out.println(linked);
		}
	}
}
