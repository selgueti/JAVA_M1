package fr.uge.tp3.conc;

import java.util.Objects;

public class HonorBoard {

	private String firstName;
	private String lastName;
	private final Object lock = new Object();

	public void set(String firstName, String lastName) {
		Objects.requireNonNull(firstName);
		Objects.requireNonNull(lastName);
		synchronized (lock) {
			this.firstName = firstName;
			this.lastName = lastName;	
		}

	}

//	public String getFirstName() {
//			return firstName;
//	}
//
//	public String getLastName() {
//			return lastName;
//	}

	@Override
	public String toString() {
		synchronized(lock) {
			return firstName + ' ' + lastName;	
		}
	}

	public static void main(String[] args) {

		var board = new HonorBoard();

		new Thread(() -> {
			for(;;) {
				board.set("John", "Doe");
			}
		}).start();

		new Thread(() -> {
			for(;;) {
				board.set("Jane", "Odd");
			}
		}).start();

		new Thread(() -> {
			for(;;) {
				System.out.println(board);
				//System.out.println(board.getFirstName() + ' ' + board.getLastName());
			}
		}).start();
	}
}
