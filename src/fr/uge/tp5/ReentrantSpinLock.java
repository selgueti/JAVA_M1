package fr.uge.tp5;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.VarHandle;

public class ReentrantSpinLock {
	private volatile int lock;
	private Thread ownerThread;

	private final static VarHandle LOCK;

	static {
		Lookup lookup = MethodHandles.lookup();
		try {
			LOCK = lookup.findVarHandle(ReentrantSpinLock.class, "lock", int.class);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}

	public void lock() {
		// idée de l'algo
		// on récupère la thread courante
		// si lock == 0, on utilise un CAS pour le mettre à 1 et
		// on sauvegarde la thread qui possède le lock dans ownerThread.
		// sinon on regarde si la thread courante est ownerThread,
		// si oui alors on incrémente lock.
		//
		// et il faut une boucle pour retenter le CAS après avoir appelé onSpinWait()
		for (;;) {
			var current = Thread.currentThread();
			if (LOCK.compareAndSet(this, 0, 1)) {
				ownerThread = Thread.currentThread();
				break;
			}
			if (current == ownerThread) {
				lock++;
				break;
			}
			Thread.onSpinWait();
		}
	}

	public void unlock() {
		// idée de l'algo
		// si la thread courante est != ownerThread, on lève une exception
		// si lock == 1, on remet ownerThread à null
		// on décrémente lock
		if (Thread.currentThread() != ownerThread) {
			throw new IllegalAccessError("This thread does not have the lock...");
		}
		if (lock == 1) {
			ownerThread = null;
		}
		lock--;
	}

	public static void main(String[] args) throws InterruptedException {
		var runnable = new Runnable() {
			private int counter;
			private final ReentrantSpinLock spinLock = new ReentrantSpinLock();

			@Override
			public void run() {
				for (var i = 0; i < 1_000_000; i++) {
					spinLock.lock();
					try {
						spinLock.lock();
						try {
							counter++;
						} finally {
							spinLock.unlock();
						}
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