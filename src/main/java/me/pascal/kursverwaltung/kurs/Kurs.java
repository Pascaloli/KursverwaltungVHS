package me.pascal.kursverwaltung.kurs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import me.pascal.kursverwaltung.dozent.Dozent;

public class Kurs {

	public final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	private String title;
	private Date beginn, ende;
	private Dozent leiter;

	public Kurs() {
	}

	public Kurs(String title, Date beginn, Date ende, Dozent leiter) {
		this.title = title;
		this.beginn = beginn;
		this.ende = ende;
		this.leiter = leiter;
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

	public Date getBeginn() {
		return beginn;
	}

	public boolean setBeginn(String beginn) {
		if (beginn.contains("-")) {
			return false;
		}
		try {
			Date date = dateFormat.parse(beginn);
			if (ende != null && date.after(ende)) {
				return false;
			}
			this.beginn = date;
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public Date getEnde() {
		return ende;
	}

	public boolean setEnde(String ende) {
		if (ende.contains("-")) {
			return false;
		}
		try {
			Date date = dateFormat.parse(ende);
			if (beginn != null && date.before(beginn)) {
				return false;
			}
			this.ende = date;
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public Dozent getLeiter() {
		return leiter;
	}

	public void setLeiter(Dozent leiter) {
		this.leiter = leiter;
	}

	public String getBeginnFormatted() {
		return dateFormat.format(beginn);
	}

	public String getEndeFormatted() {
		return dateFormat.format(ende);
	}
}
