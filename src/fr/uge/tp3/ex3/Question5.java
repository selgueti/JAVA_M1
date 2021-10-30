package fr.uge.tp3.ex3;

public class Question5 {
	
	private static int slow() {
		var result = 1;
		for (var i = 0; i < 1_000_000; i++) {
			result += (result * 7) % 513;
			if(Thread.currentThread().isInterrupted()) {
				return result;
			}
		}
		return result;
	}
	
	public static void main(String[] args) throws InterruptedException {
	    var thread = new Thread(() -> {
	        var forNothing = 0;
	        while(true) {
	            try {
	            	forNothing += slow();
					Thread.sleep(1_000);
					forNothing += slow(); 
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} 
	            if(Thread.interrupted()) {
	            	System.out.println("end  : " + forNothing);
					return;
	            }
	            
	        }
	    });
	    thread.start();
	    Thread.sleep(1_000);
	    thread.interrupt();
	  }
}
