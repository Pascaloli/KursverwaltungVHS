package me.pascal.kursverwaltung.dozent;

import java.util.Objects;
import java.util.regex.Pattern;

public class Dozent {

	private String title, name, email, telNr;

	public Dozent() {
	}

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
		if (title.isEmpty()) {
			return false;
		}
		this.title = title;
		return true;
	}

	public String getName() {
		return name;
	}

	public String getFullName() {
		return (title.isEmpty() ? "" : title + " ") + name;
	}

	public boolean setName(String name) {
		if (!name.chars().allMatch(c -> (Character.isLetter(c) || Character.isSpaceChar(c)))) {
			return false;
		} else {
			this.name = name;
			return true;
		}
	}

	public String getEmail() {
		return email;
	}

	public boolean setEmail(String email) {
		//Quelle https://emailregex.com
		Pattern pattern = Pattern.compile(
				"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

		if (!pattern.matcher(email).matches()) {
			return false;
		} else {
			this.email = email;
			return true;
		}
	}

	public String getTelNr() {
		return telNr;
	}

	public boolean setTelNr(String telNr) {
		//Quelle: https://www.regextester.com/108528
		Pattern pattern = Pattern.compile("\\(?\\+\\(?49\\)?[ ()]?([- ()]?\\d[- ()]?){10}");

		if (!pattern.matcher(telNr).matches()) {
			return false;
		} else {
			this.telNr = telNr;
			return true;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Dozent dozent = (Dozent) o;
		return title.equals(dozent.title) &&
				name.equals(dozent.name) &&
				email.equals(dozent.email) &&
				telNr.equals(dozent.telNr);
	}
}
