package movida.bruno.functions;
import movida.commons.Person;

public class CompareByAppearances implements Comparator<Person> {

	public int compare(Person o1, Person o2) {
		return o1.getAppearances() - o2.getAppearances();
	}

}
