package movida.bruno.functions;
import movida.commons.Movie;

public class CompareByTitle implements Comparator<Movie> {

	public int compare(Movie o1, Movie o2) {
		return o1.getTitle().compareTo(o2.getTitle());
	}

}
