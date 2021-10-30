package fr.uge.tp1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Movies {
	public static List<Movie> movies(Path path) throws IOException {
		Objects.requireNonNull(path);
		try(var lines = Files.lines(path)){
			return lines.map( line -> {
				var listElem = line.split(";");
				var title = listElem[0];
				var actors = Stream.of(listElem).skip(1).toList();
				return new Movie(title, actors);
			}).toList();
		}
	}

	public static Map<String, Movie> movieMap(List<Movie> movies){
		Objects.requireNonNull(movies);
		return movies.stream().collect(Collectors.toUnmodifiableMap(Movie::title, Function.identity()));
	}

	//	public static Map<String, Long> numberOfMoviesByActor(List<Movie>movies){		
	//		var res = new HashMap<String, Long>();
	//		movies.stream().flatMap(movie -> movie.actors().stream()).forEach(actor -> res.merge(actor, 1L, (s1, s2) -> s1 + s2));
	//		return res;
	//	}

	public static void displayLimit20(List<Movie> movies) {
		Objects.requireNonNull(movies);
		movies.stream().flatMap(movie -> movie.actors().stream()).limit(20).forEach(System.out::println);
	}
	
	public static long numberOfUniqueActors(List<Movie> movies) {
		Objects.requireNonNull(movies);
		return movies.stream().flatMap(movie -> movie.actors().stream()).distinct().count();
	}

	public static void displayNumberOfActorNaif(List<Movie> movies) {
		Objects.requireNonNull(movies);
		System.out.println(movies.stream().flatMap(movie -> movie.actors().stream()).count());;
	}

	public static void displayNumberOfActor(List<Movie> movies) {
		Objects.requireNonNull(movies);
		System.out.println(movies.stream().flatMap(movie -> movie.actors().stream()).distinct().count());;
	}

	public static Map<String, Long> numberOfMoviesByActor(List<Movie> movies){
		Objects.requireNonNull(movies);
		return movies.stream().flatMap(movie -> movie.actors().stream()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	public static Optional<ActorMovieCount> actorInMostMovies(Map<String, Long> movies) {
		Objects.requireNonNull(movies);
		return movies.entrySet().stream().map(entry -> 

		new ActorMovieCount(entry.getKey(), entry.getValue())

				).collect(Collectors.maxBy(Comparator.comparingLong(ActorMovieCount::movieCount)));
	}
}






