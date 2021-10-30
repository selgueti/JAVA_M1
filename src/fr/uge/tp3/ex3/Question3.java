package fr.uge.tp3.ex3;

import java.lang.Thread;

public class Question3 {

	private static int slow() {
		var result = 1;
		for (var i = 0; i < 1_000_000; i++) {
			result += (result * 7) % 513;
		}
		return result;
	}

	public static void main(String[] args) throws InterruptedException {
		var thread = new Thread(() -> {
			var forNothing = 0;
			while (true) {
				forNothing += slow();
				
				if(Thread.interrupted()) {
					System.out.println("end : " + forNothing);
					return;
				}
			}
		});
		thread.start();
		Thread.sleep(1_000);
		thread.interrupt();
	}
}
