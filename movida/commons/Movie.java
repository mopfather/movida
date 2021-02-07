/* 
 * Copyright (C) 2020 - Angelo Di Iorio
 * 
 * Progetto Movida.
 * Corso di Algoritmi e Strutture Dati
 * Laurea in Informatica, UniBO, a.a. 2019/2020
 * 
*/
package movida.commons;

/**
 * Classe usata per rappresentare un film
 * nell'applicazione Movida.
 * 
 * Un film � identificato in modo univoco dal titolo 
 * case-insensitive, senza spazi iniziali e finali, senza spazi doppi. 
 * 
 * La classe pu˜ essere modicata o estesa ma deve implementare tutti i metodi getter
 * per recupare le informazioni caratterizzanti di un film.
 * 
 */
public class Movie {
	
	private String title;
	private Integer year;
	private Integer votes;
	private Person[] cast;
	private Person director;
	
	public Movie(String title, Integer year, Integer votes,
			Person[] cast, Person director) {
		this.title = title;
		this.year = year;
		this.votes = votes;
		this.cast = cast;
		this.director = director;
	}
	
	public Movie(Movie another) {
		this.title = another.title;
		this.year = another.year;
		this.votes = another.votes;
		this.cast = new Person[another.cast.length];
		for (int i = 0; i < cast.length; i++) {
			cast[i] = new Person(another.cast[i]);
		}
		this.director = new Person(another.director);
	}
	
	public String getTitle() {
		return this.title;
	}

	public Integer getYear() {
		return this.year;
	}

	public Integer getVotes() {
		return this.votes;
	}

	public Person[] getCast() {
		return this.cast;
	}

	public Person getDirector() {
		return this.director;
	}
	
	public void setTitle(String s) {
		title = s;
	}
	
	public void setDirector(Person s) {
		director = s;
	}
	
	public void setVotes(int i) {
		votes = i;
	}
	
	public void setCast(Person[] s) {
		cast = s;
	}
	
	public void setYear(int i) {
		year = i;
	}
	
	public String castToString() {
		String ret = "";
		for (int i = 0; i < cast.length; i++) {
			if (i != cast.length - 1) {
				ret += cast[i].getName() + "; ";
			}
			else {
				ret += cast[i].getName();
			}
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return "{" + 
				"Title: " + title + ", " +
				"Director: " + director.getName() + ", " +
				"Year: " + year + ", " +
				"Votes: " + votes + ", " +
				"Cast: " + castToString() +
		"}";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Movie)) {
			return false;
		}
		
		Movie m = (Movie) o;
		return title.equals(m.getTitle());
	}
}
