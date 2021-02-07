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
 * Classe usata per rappresentare una persona, attore o regista,
 * nell'applicazione Movida.
 * 
 * Una persona � identificata in modo univoco dal nome 
 * case-insensitive, senza spazi iniziali e finali, senza spazi doppi. 
 * 
 * Semplificazione: <code>name</code> � usato per memorizzare il nome completo (nome e cognome)
 * 
 * La classe pu˜ essere modicata o estesa ma deve implementare il metodo getName().
 * 
 */
public class Person {

	private String name;
	private int movieAppearances;
	
	public Person(String name) {
		this.name = name;
		this.movieAppearances = 1;
	}
	
	public Person(Person another) {
		this.name = another.name;
		this.movieAppearances = another.movieAppearances;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getAppearances() {
		return this.movieAppearances;
	}
	
	public void decreaseAppearances() {
		this.movieAppearances--;
	}
	
	public void increaseAppearances() {
		this.movieAppearances++;
	}
	
	public String toString() {
		return this.getName() + ", appearances: " +
			Integer.toString(getAppearances()) +
		";";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Person)) {
			return false;
		}
		
		Person p = (Person) o;
		return name.equals(p.getName());
	}
}
