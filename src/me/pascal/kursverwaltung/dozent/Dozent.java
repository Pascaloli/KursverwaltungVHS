package me.pascal.kursverwaltung.dozent;

public class Dozent {

	private String title, name, email, telNr;

	public Dozent(){}

	public Dozent(String title, String name, String email, String telNr) {
		this.title = title;
		this.name = name;
		this.email = email;
		this.telNr = telNr;
	}

	public String getTitle() {
		return title;
	}

	public boolean setTitle(String title) {
		this.title = title;
		return true;
	}

	public String getName() {
		return name;
	}

	public boolean setName(String name) {
		this.name = name;
		return true;
	}

	public String getEmail() {
		return email;
	}

	public boolean setEmail(String email) {
		this.email = email;
		return true;
	}

	public String getTelNr() {
		return telNr;
	}

	public boolean setTelNr(String telNr) {
		this.telNr = telNr;
		return true;
	}
}
