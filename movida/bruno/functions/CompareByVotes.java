package movida.bruno.functions;
import movida.commons.Movie;

public class CompareByVotes implements Comparator<Movie> {

	public int compare(Movie o1, Movie o2) {
		return o1.getVotes().compareTo(o2.getVotes());
	}

}