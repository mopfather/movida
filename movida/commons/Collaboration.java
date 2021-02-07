package movida.commons;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;

public class Collaboration {

	Person actorA;
	Person actorB;
	ArrayList<Movie> movies;
	
	public Collaboration(Person actorA, Person actorB) {
		this.actorA = actorA;
		this.actorB = actorB;
		this.movies = new ArrayList<Movie>();
	}

	public Person getActorA() {
		return actorA;
	}

	public Person getActorB() {
		return actorB;
	}

	public Double getScore(){
		
		Double score = 0.0;
		
		for (Movie m : movies)
			score += m.getVotes();
		
		return score / movies.size();
	}
	
	public void addMovies(Movie[] movies) {
		Collections.addAll(this.movies, movies);
	}
	
	@Override
	public String toString() {
		return "Collaboration between " + actorA.getName() + " and " +
			actorB.getName() + ", score: " + BigDecimal.valueOf(getScore())
											.setScale(2, RoundingMode.HALF_UP); 
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof  Collaboration)) {
			return false;
		}
		
		Collaboration c = (Collaboration) o;
		return ((this.actorA.equals(c.actorA) &&
			this.actorB.equals(c.actorB)) ||
			this.actorA.equals(c.actorB) &&
			this.actorB.equals(c.actorA)
		);
	}
}
