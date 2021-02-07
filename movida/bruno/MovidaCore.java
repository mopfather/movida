package movida.bruno;

import movida.commons.*;
import movida.bruno.dataStructures.*;
import movida.bruno.functions.*;
import movida.bruno.algorithms.*;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;
import java.io.BufferedWriter;

public class MovidaCore implements IMovidaDB, IMovidaConfig, IMovidaSearch, IMovidaCollaborations {
	private DataStructure<Movie> movies;
	private DataStructure<Person> people;
	private HashMap<String, ArrayList<Counter<Person>>> edges;
	private SortingAlgorithm alg;
	private MapImplementation map;
	
	public MovidaCore(SortingAlgorithm alg, MapImplementation map) {
		this.alg = alg;
		this.edges = new HashMap<String, ArrayList<Counter<Person>>>();
		CompareByTitle compareByTitle = new CompareByTitle();
		CompareByName compareByName = new CompareByName();
		
		switch(map) {
			case ArrayOrdinato:
				this.map = map;
				this.movies = new ArrayOrdinato<Movie>(compareByTitle);
				this.people = new ArrayOrdinato<Person>(compareByName);
				break;
			case ABR:
				this.map = map;
				this.movies = new ABR<Movie>(compareByTitle);
				this.people = new ABR<Person>(compareByName);
				break;
			default:
				System.out.println("The map " + map.toString() + " is not supported, ABR will be used instead");
				this.map = MapImplementation.ABR;
				this.movies = new ABR<Movie>(compareByTitle);
				this.people = new ABR<Person>(compareByName);
				break;
		}
	}
	
	public void addMovies(Movie[] movieArray) {
		for (int i = 0; i < movieArray.length; i++) {
			Movie replaced = movies.find(movieArray[i]);
			if (replaced != null) {
				Person[] replacedCast = replaced.getCast();
				for (int j = 0; j < replacedCast.length; j++) {
					Person p = people.find(replacedCast[j]);
					if (p.getAppearances() == 1) {
						people.deleteElement(p);
					}
					else {
						p.decreaseAppearances();
					}
				
					ArrayList<Counter<Person>> pEdges = edges.get(replacedCast[j].getName());
					for (int castIdx = 0; castIdx < replacedCast.length; castIdx++) {
						if (castIdx != j) {	
							Counter<Person> e = null;
							for (Counter<Person> edgeActor: pEdges) {
								if (edgeActor.getElement().getName().equals(replacedCast[castIdx].getName())) {
									e = edgeActor;
								}
							}
							if (e.getCount() == 1) {
								pEdges.remove(e);
							}
							else {
								e.decreaseCount();
							}
						}
					}
					if (pEdges.isEmpty()) {
						edges.remove(replacedCast[j].getName());
					}
				}
			}
			
			Person[] newCast = movieArray[i].getCast();
			for (int j = 0; j < newCast.length; j++) {
				Person p = people.find(newCast[j]);
				if (p == null) {
					people.addElement(newCast[j]);
				}
				else {
					p.increaseAppearances();
				}
				
				if (p == null) {
					edges.put(newCast[j].getName(), new ArrayList<Counter<Person>>());
				}
				ArrayList<Counter<Person>> pEdges = edges.get(newCast[j].getName());
				for (int castIdx = 0; castIdx < newCast.length; castIdx++) {
					if (castIdx != j) {	
						Counter<Person> e = null;
						for (Counter<Person> edgeActor: pEdges) {
							if (edgeActor.getElement().getName().equals(newCast[castIdx].getName())) {
								e = edgeActor;
							}
						}
						if (e == null) {
							pEdges.add(new Counter<Person>(newCast[castIdx]));
						}
						else {
							e.increaseCount();
						}
					}
				}
			}
			
			
			movies.addElement(movieArray[i]);
		}
	}
	
	public void loadFromFile(File f) {
		DataStructure<Movie> tmpBuf;
		switch(map) {
			case ABR:
				tmpBuf = new ABR<Movie>(new CompareByTitle());
				break;
			case ArrayOrdinato:
				tmpBuf = new ArrayOrdinato<Movie>(new CompareByTitle());
				break;
			default:
				tmpBuf = new ABR<Movie>(new CompareByTitle());
				break;
		}
		
		try (BufferedReader in = new BufferedReader(new FileReader(f))) {
			while (in.ready()) {
				Movie movie = new Movie(null, 0, 0, null, null);
				String[] fields = new String[5];
				for (int i = 0; i < 5; i++) {
					String line = in.readLine();
					while (line.isBlank()) {
						line = in.readLine();
					}
					if (line != null) {
						String[] subStrs = line.split(":\\s", 2);
						if (subStrs[0].compareTo("Title") == 0) {
							fields[0] = subStrs[1];
						}
						else if (subStrs[0].compareTo("Year") == 0) {
							fields[1] = subStrs[1];
						}
						else if (subStrs[0].compareTo("Director") == 0) {
							fields[2] = subStrs[1];
						}
						else if (subStrs[0].compareTo("Cast") == 0) {
							fields[3] = subStrs[1];
						}
						else if (subStrs[0].compareTo("Votes") == 0) {
							fields[4] = subStrs[1];
						}
					}
				}
				in.readLine();
				
				for (int i = 0; i < 5; i++) {
					if (fields[i] == null || fields[i].isBlank() || fields[i].compareTo("") == 0) {
						throw new MovidaFileException();
					}
				}

				Person director = new Person(fields[2].strip());
				String[] strings = fields[3].split(",");
				Person[] cast = new Person[strings.length];
				for (int i = 0; i < strings.length; i++) {
					cast[i] = new Person(strings[i].strip());
				}
				
				movie.setTitle(fields[0].strip());
				movie.setYear(Integer.parseInt(fields[1].strip()));
				movie.setDirector(director);
				movie.setCast(cast);
				movie.setVotes(Integer.parseInt(fields[4].strip()));
				
				tmpBuf.addElement(movie);
			}
			Movie[] movieArray = tmpBuf.getArray(new Movie[0]);
			addMovies(movieArray);
		}
		
		catch (java.io.FileNotFoundException e) {
			System.err.println("FileNotFoundException: " + e.getMessage());
			throw new MovidaFileException();
		}
		catch (java.io.IOException e) {
			System.err.println("IOException: " + e.getMessage());
		}
		catch (java.lang.NumberFormatException e) {
			System.err.println("NumberFormatException: " + e.getMessage());
		}
		catch (java.lang.ArrayIndexOutOfBoundsException e) {
			System.err.println("ArrayIndexOutOfBoundesException: " + e.getMessage());
		}
	}
	
	public void saveToFile(File f) {
		try (BufferedWriter out = new BufferedWriter(new FileWriter(f))) {
			Movie[] movieArray = movies.getArray(new Movie[0]);
			for (int i = 0; i < movieArray.length; i++) {
				out.write("Title: " + movieArray[i].getTitle() + "\n");
				out.write("Year: " + Integer.toString(movieArray[i].getYear()) + "\n");
				out.write("Director: " + movieArray[i].getDirector().getName() + "\n");
				
				Person[] cast = movieArray[i].getCast();
				out.write("Cast: ");
				for (int j = 0; j < cast.length; j++) {
					if (j == cast.length - 1) {
						out.write(cast[j].getName() + "\n");
					}
					else {
						out.write(cast[j].getName() + ", ");
					}
				}
				out.write("Votes: " + Integer.toString(movieArray[i].getVotes()));
				if (i != movieArray.length - 1) {
					out.newLine();
					out.newLine();
				}
			}
		}

		catch (java.io.IOException e) {
			throw new MovidaFileException();
		}
	}
	
	public void clear() {
		movies.removeAllElements();
		people.removeAllElements();
		edges.clear();
	}
	
	public int countMovies() {
		return movies.getElemCount();
	}
	
	public int countPeople() {
		return people.getElemCount();
	}
	
	public boolean deleteMovieByTitle(String title) {
		Movie tmp = new Movie(title, 0, 0, null, null);
		Movie deleted = movies.find(tmp);
		if (deleted == null) {
			return false;
		}
		else {
			Person[] deletedCast = deleted.getCast();
			for (int j = 0; j < deletedCast.length; j++) {
				Person p = people.find(deletedCast[j]);
				if (p.getAppearances() == 1) {
					people.deleteElement(p);
				}
				else {
					p.decreaseAppearances();
				}
				
				ArrayList<Counter<Person>> pEdges = edges.get(deletedCast[j].getName());
				for (int castIdx = 0; castIdx < deletedCast.length; castIdx++) {
					if (castIdx != j) {	
						Counter<Person> e = null;
						for (Counter<Person> edgeActor: pEdges) {
							if (edgeActor.getElement().getName().equals(deletedCast[castIdx].getName())) {
								e = edgeActor;
							}
						}
						if (e.getCount() == 1) {
							pEdges.remove(e);
						}
						else {
							e.decreaseCount();
						}
					}
				}
				if (pEdges.isEmpty()) {
					edges.remove(deletedCast[j].getName());
				}
			}
		}
		
		movies.deleteElement(tmp);
		return true;
	}
	
	public Movie getMovieByTitle(String title) {
		Movie tmp = new Movie(title, 0, 0, null, null);
		return new Movie(movies.find(tmp));
	}
	
	public Person getPersonByName(String name) {
		Person tmp = new Person(name);
		return new Person(people.find(tmp));
	}
	
	public Movie[] getAllMovies() {
		Movie[] allMovies = movies.getArray(new Movie[0]);
		Movie[] ret = new Movie[allMovies.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new Movie(allMovies[i]);
		}
		return ret;
	}
	
	public Person[] getAllPeople() {
		Person[] allPeople = people.getArray(new Person[0]);
		Person[] ret = new Person[allPeople.length];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new Person(allPeople[i]);
		}
		return ret;
	}
	
	public boolean setMap(MapImplementation m) {
		switch(m) {
			case ArrayOrdinato:
				if (map != MapImplementation.ArrayOrdinato) {
					Movie[] movieArray = movies.getArray(new Movie[0]);
					movies = new ArrayOrdinato<Movie>(new CompareByTitle());
					people = new ArrayOrdinato<Person>(new CompareByName());
					addMovies(movieArray);
					return true;
				}
				return false;
				
			case ABR:
				if (map != MapImplementation.ABR) {
					Movie[] movieArray = movies.getArray(new Movie[0]);
					movies = new ABR<Movie>(new CompareByTitle());
					people = new ABR<Person>(new CompareByName());
					addMovies(movieArray);
					return true;
				}
				return false;
			
			default:
				return false;
		}
	}
	
	public boolean setSort(SortingAlgorithm a) {
		switch(a) {
			case HeapSort:
				if (alg != SortingAlgorithm.HeapSort) {
					alg = SortingAlgorithm.HeapSort;
					return true;
				}
				return false;
			
			case SelectionSort:
				if (alg != SortingAlgorithm.SelectionSort) {
					alg = SortingAlgorithm.SelectionSort;
					return true;
				}
				return false;
			
			default:
				return false;
		}
	}
	
	public <T> Movie[] searchMoviesByX(T match, Condition<Movie, T> c) {
		Movie[] allMovies = movies.getArray(new Movie[0]);
		int count = 0;
		for (int i = 0; i < allMovies.length; i++) {
			if (allMovies[i] == null) {
				break;
			}
			if (c.match(allMovies[i], match)) {
				count++;
			}
		}
		Movie[] ret = new Movie[count];
		int idx = 0;
		for (int i = 0; i < allMovies.length; i++) {
			if (allMovies[i] == null) {
				break;
			}
			if (c.match(allMovies[i], match)) {
				ret[idx] = new Movie(allMovies[i]);
				idx++;
			}
		}
		return ret;
	}
	
	public Movie[] searchMoviesByTitle(String title) {
		return searchMoviesByX(title, (Movie m, String s) -> {
			return m.getTitle().contains(title);
		});
	}
	
	public Movie[] searchMoviesInYear(Integer year) {
		return searchMoviesByX(year, (Movie m, Integer i) -> {
			return m.getYear().equals(i);
		});
	}
	
	public Movie[] searchMoviesDirectedBy(String name) {
		return searchMoviesByX(name, (Movie m, String s) -> {
			return m.getDirector().getName().equals(s);
		});
	}
	
	public Movie[] searchMoviesStarredBy(String name) {
		return searchMoviesByX(name, (Movie m, String s) -> {
			boolean ret = false;
			Person[] cast = m.getCast();
			for (int i = 0; i < cast.length; i++) {
				if (cast[i].getName().equals(s)) {
					ret = true;
				}
			}
			return ret;
		});
	}
		
	public Movie[] searchMostVotedMovies(Integer N) {
		Movie[] allMovies = movies.getArray(new Movie[0]);
		Movie[] ret = new Movie[Math.min(movies.getElemCount(), N)];
		switch(alg) {
			case HeapSort:
				HeapSort.sort(allMovies, new CompareByVotes(), false);
				break;
			case SelectionSort:
				SelectionSort.sort(allMovies, new CompareByVotes(), false);
				break;
			default:
				System.err.println("Error: alg is set to illegal value");
		}
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new Movie(allMovies[i]);
		};
		return ret;
	}
	
	public Movie[] searchMostRecentMovies(Integer N) {
		Movie[] allMovies = movies.getArray(new Movie[0]);
		Movie[] ret = new Movie[Math.min(movies.getElemCount(), N)];
		switch(alg) {
			case HeapSort:
				HeapSort.sort(allMovies, new CompareByYear(), false);
				break;
			case SelectionSort:
				SelectionSort.sort(allMovies, new CompareByYear(), false);
				break;
			default:
				System.err.println("Error: alg is set to illegal value");
		}
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new Movie(allMovies[i]);
		};
		return ret;
	}
	
	public Person[] searchMostActiveActors(Integer N) {
		Person[] allPeople = people.getArray(new Person[0]);
		Person[] ret = new Person[Math.min(people.getElemCount(), N)];
		switch(alg) {
			case HeapSort:
				HeapSort.sort(allPeople, new CompareByAppearances(), false);
				break;
			case SelectionSort:
				SelectionSort.sort(allPeople, new CompareByAppearances(), false);
				break;
			default:
				System.err.println("Error: alg is set to illegal value");
		}
		for (int i = 0; i < ret.length; i++) {
			ret[i] = new Person(allPeople[i]);
		};
		return ret;
	}
	
	
	public Person[] getDirectCollaboratorsOf(Person actor) {
		Person p = people.find(actor);
		if (p == null) {
			return null;
		}
		
		ArrayList<Counter<Person>> pEdges = edges.get(p.getName());
		Person[] ret = new Person[pEdges.size()];
		for (int i = 0; i < pEdges.size(); i++) {
			ret[i] = new Person(pEdges.get(i).getElement());
		}

		return ret;
	}
	
	public Person[] getTeamOf(Person actor) {
		Person p = people.find(actor);
		if (p == null) {
			return null;
		}
		
		ArrayList<Person> team = new ArrayList<>();
		Stack<String> stack = new Stack<>();
		stack.push(p.getName());
		
		while (!stack.isEmpty()) {
			String s = stack.peek();
			stack.pop();
			ArrayList<Counter<Person>> pEdges = edges.get(s);
			for (Counter<Person> node: pEdges) {
				Person edgeActor = node.getElement();
				if (!team.contains(edgeActor)) {
					team.add(new Person(edgeActor));
					stack.push(edgeActor.getName());
				}
			}
		}
				
		return team.toArray(new Person[0]);
	}
	
	public Movie[] getCoStarredMovies(Person actorA, Person actorB) {
		ArrayList<Movie> ret = new ArrayList<Movie>();
		Movie[] moviesOfA = searchMoviesStarredBy(actorA.getName());
		for (int i = 0; i < moviesOfA.length; i++) {
			Person[] cast = moviesOfA[i].getCast();
			for (int j = 0; j < cast.length; j++) {
				if (cast[j].equals(actorB)) {
					ret.add(moviesOfA[i]);
				}
			}
		}
		return ret.toArray(new Movie[0]);
	}
	
	public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
		ArrayList<Person> spanningTree = new ArrayList<Person>();
		spanningTree.add(actor);
		PriorityQueue<Collaboration> q = new PriorityQueue<Collaboration>(1, (Collaboration c1, Collaboration c2) -> {
			return (int) (c2.getScore() - c1.getScore());
		});
		
		Person[] directTeam = getDirectCollaboratorsOf(actor);
		for (int i = 0; i < directTeam.length; i++) {
			Collaboration collab = new Collaboration(actor, directTeam[i]);
			collab.addMovies(getCoStarredMovies(actor, directTeam[i]));
			q.add(collab);
		}

		Collaboration[] ret = new Collaboration[getTeamOf(actor).length - 1];
		int retIdx = 0;
		while (spanningTree.size() < ret.length + 1) {
			Collaboration collab = q.poll();
			Person ActorA = collab.getActorA();
			Person ActorB = collab.getActorB();
			boolean AinTree = spanningTree.contains(ActorA);
			boolean BinTree = spanningTree.contains(ActorB);
			if (!AinTree || !BinTree) {
				ret[retIdx] = collab;
				retIdx++;
				
				Person p;
				if (AinTree) {
					directTeam = getDirectCollaboratorsOf(collab.getActorB());
					p = ActorB;
					spanningTree.add(p);
				}
				else {
					directTeam = getDirectCollaboratorsOf(collab.getActorA());
					p = ActorA;
					spanningTree.add(p);
				}
				for (int teamIdx = 0; teamIdx < directTeam.length; teamIdx++) {
					Collaboration c = new Collaboration(p, directTeam[teamIdx]);
					c.addMovies(getCoStarredMovies(p, directTeam[teamIdx]));
					q.add(c);

				}
			}
		}
		return ret;
	}
	
}
