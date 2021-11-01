package fr.uge.tp5;

public class SpinLock {
	public void lock() {
		// TODO
	}

	public void unlock() {
		// TODO
	}

	public static void main(String[] args) throws InterruptedException {
		var runnable = new Runnable() {
			private int counter;
			private final SpinLock spinLock = new SpinLock();

			@Override
			public void run() {
				for (int i = 0; i < 1_000_000; i++) {
					spinLock.lock();
					try {
						counter++;
					} finally {
						spinLock.unlock();
					}
				}
			}
		};
		var t1 = new Thread(runnable);
		var t2 = new Thread(runnable);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println("counter " + runnable.counter);
	}
}
