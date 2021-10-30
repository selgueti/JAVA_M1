package fr.uge.tp1;

import java.io.IOException;
import java.nio.file.Path;

//

public class Main {

	public static void main(String[] args) throws IOException{
		var path = Path.of("movies.txt");
		//		var lines = Files.lines(path);;
		//		try {
		//			System.out.println(lines.count());
		//		}finally {
		//			lines.close();
		//			System.exit(1);
		//		}

		//		try(var lines = Files.lines(path)){
		//			System.out.println("lines : " + lines.count());	
		//		} catch(IOException e){
		//			System.err.println(e.getMessage());
		//			System.exit(1);
		//			return ;
		//		}

		try {
			var movies = Movies.movies(path);
			Movies.displayLimit20(movies);
			Movies.displayNumberOfActorNaif(movies);
			Movies.displayNumberOfActor(movies);
			var hmap = Movies.numberOfMoviesByActor(movies);
			System.out.println(hmap.get("Brad Pitt")); 

		} catch (IOException e) {
			System.err.println(e.getMessage());
			return;
		}
	}
}
