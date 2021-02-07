package movida.bruno.functions;
import movida.commons.Movie;

public class CompareByYear implements Comparator<Movie> {

	public int compare(Movie o1, Movie o2) {
		return o1.getYear().compareTo(o2.getYear());
	}

}
