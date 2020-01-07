package me.pascal.kursverwaltung.kurs;

import java.util.Date;
import me.pascal.kursverwaltung.dozent.Dozent;

public class Kurs {

	private int id;
	private String title;
	private Date beginn, ende;
	private Dozent leiter;

	public Kurs() {
	}

	public Kurs(int id, String title, Date beginn, Date ende, Dozent leiter) {
		this.id = id;
		this.title = title;
		this.beginn = beginn;
		this.ende = ende;
		this.leiter = leiter;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public boolean setTitle(String title) {
		this.title = title;
		return true;
	}

	public Date getBeginn() {
		return beginn;
	}

	public void setBeginn(Date beginn) {
		this.beginn = beginn;
	}

	public Date getEnde() {
		return ende;
	}

	public void setEnde(Date ende) {
		this.ende = ende;
	}

	public Dozent getLeiter() {
		return leiter;
	}

	public void setLeiter(Dozent leiter) {
		this.leiter = leiter;
	}
}
