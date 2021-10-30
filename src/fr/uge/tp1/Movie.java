package fr.uge.tp1;

import java.util.List;
import java.util.Objects;

public record Movie(String title, List<String> actors) {
	public Movie {
		Objects.requireNonNull(title);
		Objects.requireNonNull(actors);
		actors = List.copyOf(actors);
	}
}
