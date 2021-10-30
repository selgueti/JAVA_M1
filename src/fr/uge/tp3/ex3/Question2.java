package fr.uge.tp3.ex3;

public class Question2 {
	public static void main(String[] args) throws InterruptedException {
		var thread = new Thread(() -> {
			while(true){  
				try {
					Thread.sleep(1_000);
//					System.out.println("1 more sec");
				} catch (InterruptedException e) {
					System.out.println("end");
					return;
				}
			}
		});
		thread.start();
		Thread.sleep(1_000);
		Thread.sleep(1_000);
		thread.interrupt();
	}
}
