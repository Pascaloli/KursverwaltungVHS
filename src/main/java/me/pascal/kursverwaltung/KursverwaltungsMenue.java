package me.pascal.kursverwaltung;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Scanner;
import java.util.stream.IntStream;
import me.pascal.kursverwaltung.dozent.Dozent;
import me.pascal.kursverwaltung.kurs.Kurs;
import org.omg.Messaging.SyncScopeHelper;

public class KursverwaltungsMenue {

	private static Scanner scanner = new Scanner(System.in);

	public static void start() {
		menu();
	}

	//Auswahl menü
	private static void menu() {
		String eingabe = eingabeAbfragen(
				"Willkommen in der VHS Kursverwaltung 2020, was möchten sie tun?\n"
						+ "(1) Dozent anlegen\n"
						+ "(2) Dozent bearbeiten\n"
						+ "(3) Kurs anlegen\n"
						+ "(4) Kurs suchen\n"
						+ "(5) Kurs bearbeiten", false);

		switch (eingabe) {
			case "1":
				dozentenAnlegen();
				break;
			case "2":
				dozentenBearbeitenUebersicht();
				break;
			case "3":
				kursAnlegen();
				break;
			case "4":
				kursSuchen();
				break;
			case "5":
				kursBearbeiten();
				break;
			default:
				ungueltigeEingabe(eingabe);
				menu();
		}
	}

	private static void ungueltigeEingabe(String eingabe) {
		System.out.println("==========================================================");
		System.out.println("Ungültige Eingabe: '" + eingabe + "'");
	}

	private static void ungueltigeEingabe(int eingabe) {
		System.out.println("==========================================================");
		System.out.println("Ungültige Eingabe: '" + eingabe + "'");
	}

	private static void dozentenAnlegen() {
		Dozent dozent = new Dozent();

		//Name des Dozenten festlegen
		do {
			String name = eingabeAbfragen("Wie heißt der Dozent, welchen Sie anlegen wollen?");
			if (name == null) {
				menu();
				return;
			}
			if (Kursverwaltung.instance.getDozentManager().getDozentByName(name) != null) {
				System.out.println("==========================================================");
				System.out.println("Ein Dozent mit diesem Namen existiert bereits.");
				System.out.println("Bitte vergeben Sie einen eindeutigen Namen.");
			} else if (!dozent.setName(name)) {
				ungueltigeEingabe(name);
			}
		} while (dozent.getName() == null);

		//Titel des Dozenten festlegen
		do {
			String titel = eingabeAbfragen("Welche Titel hat " + dozent.getName() + "?");
			if (titel == null) {
				menu();
				return;
			}
			if (!dozent.setTitle(titel)) {
				ungueltigeEingabe(titel);
			}
		} while (dozent.getTitle() == null);

		//Email des Dozenten festlegen
		do {
			String email = eingabeAbfragen(
					"Wie lautet die Email von " + dozent.getFullName() + "?");
			if (email == null) {
				menu();
				return;
			}
			if (!dozent.setEmail(email)) {
				ungueltigeEingabe(email);
			}
		} while (dozent.getEmail() == null);

		//Telefonnummer des Dozenten festlegen
		do {
			String telnr = eingabeAbfragen(
					"Wie lautet die Telefonnummer von " + dozent.getFullName() + "?");
			if (telnr == null) {
				menu();
				return;
			}
			if (!dozent.setTelNr(telnr)) {
				ungueltigeEingabe(telnr);
			}
		} while (dozent.getTelNr() == null);

		Kursverwaltung.instance.getDozentManager().addDozent(dozent);
		System.out.println("==========================================================");
		System.out.println("Der Dozent " + dozent.getFullName()
				+ " wurde erfolgreich angelegt.");
		//Zurück zum menü
		menu();
	}

	private static void dozentenBearbeitenUebersicht() {
		if(Kursverwaltung.instance.getDozentManager().getDozenten().size() == 0){
			System.out.println("==========================================================");
			System.out.println("Es existieren noch keine Dozenten.");
			menu();
		}

		//Übersicht der Dozenten aufbauen
		int index = 1;
		StringBuilder uebersicht = new StringBuilder();
		for (Dozent dozent : Kursverwaltung.instance.getDozentManager().getDozenten()) {
			uebersicht.append("(" + index++ + ") " + dozent.getFullName() + "\n");
		}

		//Dozenten auswahl abfragen
		int auswahl = eingabeAbfragenInt(
				"Bitte wählen Sie den zu bearbeitenden Dozenten aus.\n" + uebersicht
						.substring(0, uebersicht.length() - 1));
		if (auswahl == -1) {
			menu();
			return;
		}

		//Eingabe validieren
		if (auswahl < 1 || auswahl > Kursverwaltung.instance.getDozentManager()
				.getDozenten().size()) {
			ungueltigeEingabe(auswahl);
			dozentenBearbeitenUebersicht();
			return;
		}

		//Bearbeitungsmenü aufrufen
		dozentBearbeiten(auswahl - 1);
	}

	private static void dozentBearbeiten(int index) {
		//Übersucht der zu bearbeitenden Felder anzeigen
		StringBuilder uebersicht = new StringBuilder();
		Dozent dozent = Kursverwaltung.instance.getDozentManager().getDozenten().get(index);
		uebersicht.append("(1) Titel (").append(dozent.getTitle()).append(")\n");
		uebersicht.append("(2) Name (" + dozent.getName() + ")\n");
		uebersicht.append("(3) Email (" + dozent.getEmail() + ")\n");
		uebersicht.append("(4) Telefonnummer (" + dozent.getTelNr() + ")\n");
		uebersicht.append("(5) Löschen");

		//Eingabe abfragen
		int eingabe = eingabeAbfragenInt(
				"Bitte wählen Sie das zu bearbeitende Feld aus.\n" + uebersicht.toString());
		if (eingabe == -1) {
			dozentenBearbeitenUebersicht();
			return;
		}

		//Eingabe validieren
		if (eingabe < 1 || eingabe > 5) {
			ungueltigeEingabe(eingabe);
			dozentBearbeiten(index);
			return;
		}

		//Löschen
		if (eingabe == 5) {
			//Überprüfen ob der Dozent in einem kurs ist
			boolean inKurs = Kursverwaltung.instance.getKursManager().isDozentInKursen(dozent);

			String bestätigung = eingabeAbfragen("Löschen bestätigen? (j/n)" + (inKurs
					? "\n!!!Wenn sie diesen Dozenten löschen werden auch alle zugehörigen Kurse gelöscht!!!"
					: ""), false);

			if (bestätigung.equalsIgnoreCase("j") || bestätigung.equalsIgnoreCase("Y")) {
				Kursverwaltung.instance.getDozentManager().getDozenten().remove(index);
				Kursverwaltung.instance.getDozentManager().dozentenSpeichern();

				//Zugehörige Kurse löschen
				if (inKurs) {
					Kursverwaltung.instance.getKursManager().getKurse()
							.removeAll(Kursverwaltung.instance.getKursManager().getKurseByDozent(dozent));
					Kursverwaltung.instance.getKursManager().kurseSpeichern();
				}

				System.out.println("==========================================================");
				System.out.println("Der Dozent wurde erfolgreich gelöscht.");
				//Zurück zur übersicht
				dozentenBearbeitenUebersicht();
				return;
			} else if (bestätigung.equalsIgnoreCase("n")) {
				//Abbrechen und zurück zur auswahl der zu bearbeitenden felder
				abbrechen();
				dozentBearbeiten(index);
				return;
			}
		}

		//Alles andere außer löschen
		boolean valueChanged = false;
		do {
			String neuerWert = eingabeAbfragen("Bitte geben sie einen neuen Wert ein.");
			if (neuerWert == null) {
				dozentBearbeiten(index);
				return;
			}

			//selbsterklärend
			if (eingabe == 1) {
				if (dozent.setTitle(neuerWert)) {
					valueChanged = true;
				}
			} else if (eingabe == 2) {
				if (dozent.setName(neuerWert)) {
					valueChanged = true;
				}
			} else if (eingabe == 3) {
				if (dozent.setEmail(neuerWert)) {
					valueChanged = true;
				}
			} else if (eingabe == 4) {
				if (dozent.setTitle(neuerWert)) {
					valueChanged = true;
				}
			}

			//Wenn die änderung nicht erfolgreich war fehler ausgeben
			if (!valueChanged) {
				ungueltigeEingabe(neuerWert);
			}

			//Das alles so lange bis erfolgreich etwas geändert wurde
		} while (!valueChanged);

		Kursverwaltung.instance.getDozentManager().dozentenSpeichern();
		System.out.println("==========================================================");
		System.out.println("Wert erfolgreich aktualisiert");
		//ZUrück zur dozenten übersicht
		dozentBearbeiten(index);
	}


	private static void kursAnlegen() {
		if(Kursverwaltung.instance.getDozentManager().getDozenten().size() == 0){
			System.out.println("==========================================================");
			System.out.println("Die können momentan keinen Kurs anlegen, da keine Dozenten existieren.");
			menu();
		}


		Kurs kurs = new Kurs();
		//Titel des kurses festlegen
		do {
			String title = eingabeAbfragen("Wie heißt der Kurs, welchen Sie anlegen wollen?");
			if (title == null) {
				menu();
				return;
			}
			if (Kursverwaltung.instance.getKursManager().getKursByTitle(title) != null) {
				System.out.println("==========================================================");
				System.out.println("Ein Kurs mit diesem Titel existiert bereits.");
				System.out.println("Bitte vergeben Sie einen eindeutigen Titel.");
			} else if (!kurs.setTitle(title)) {
				ungueltigeEingabe(title);
			}
		} while (kurs.getTitle() == null);

		//Startdatum des kurses festlegen
		do {
			String eingabe = eingabeAbfragen("An welchem Tag startet der Kurs? (Format: 20.10.2019)");
			if (eingabe == null) {
				menu();
				return;
			}

			if (!kurs.setBeginn(eingabe)) {
				ungueltigeEingabe(eingabe);
			}
		} while (kurs.getBeginn() == null);

		//Enddatum des kurses festlegen
		do {
			String eingabe = eingabeAbfragen("An welchem Tag endet der Kurs? (Format: 20.10.2019)");
			if (eingabe == null) {
				menu();
				return;
			}

			if (!kurs.setEnde(eingabe)) {
				ungueltigeEingabe(eingabe);
			}
		} while (kurs.getEnde() == null);

		//Dozent des Kurses festlegen
		do {
			//Dozenten übersicht aufbauen
			int index = 1;
			StringBuilder uebersicht = new StringBuilder();
			for (Dozent dozent : Kursverwaltung.instance.getDozentManager().getDozenten()) {
				uebersicht.append("(" + index++ + ") " + dozent.getFullName() + "\n");
			}

			int auswahl = eingabeAbfragenInt(
					"Bitte wählen Sie einen Leiter für den Kurs aus.\n" + uebersicht
							.substring(0, uebersicht.length() - 1));
			if (auswahl == -1) {
				menu();
				return;
			}

			//Eingabe validieren
			boolean eingabeValid = true;
			if (auswahl < 1 || auswahl > Kursverwaltung.instance.getDozentManager()
					.getDozenten().size()) {
				ungueltigeEingabe(auswahl);
				eingabeValid = false;
			}

			//Dozent setzen
			if (eingabeValid) {
				Dozent dozent = Kursverwaltung.instance.getDozentManager().getDozenten().get(auswahl - 1);
				kurs.setLeiter(dozent);
			}
		} while (kurs.getLeiter() == null);

		Kursverwaltung.instance.getKursManager().addKurs(kurs);
		System.out.println("==========================================================");
		System.out.println("Der Kurs " + kurs.getTitle() + " wurde erfolgreich angelegt.");
		menu();
	}

	private static void kursSuchen() {
		if(Kursverwaltung.instance.getKursManager().getKurse().size() == 0){
			System.out.println("==========================================================");
			System.out.println("Es existieren noch keine Kurse.");
			menu();
		}
		//Übersicht der Kurse aufbauen
		int index = 1;
		StringBuilder uebersicht = new StringBuilder();
		for (Kurs kurs : Kursverwaltung.instance.getKursManager().getKurse()) {
			uebersicht.append("(" + index++ + ") " + kurs.getTitle() + "\n");
		}

		//Eingabe abfragen
		int eingabe = eingabeAbfragenInt(
				"Bitte wählen Sie den anzuzeigenden Kurs an.\n" + uebersicht
						.substring(0, uebersicht.length() - 1));
		if (eingabe == -1) {
			menu();
			return;
		}

		//Eingabe validieren
		if (eingabe < 1 || eingabe > Kursverwaltung.instance.getKursManager().getKurse().size()) {
			kursSuchen();
			return;
		}

		Kurs auswahl = Kursverwaltung.instance.getKursManager().getKurse().get(eingabe - 1);
		kursAnzeigen(auswahl);
	}

	public static void kursAnzeigen(Kurs kurs) {
		System.out.println("==========================================================");
		//Standartinfos
		System.out.println("Titel: " + kurs.getTitle());
		System.out.println("Leiter: " + kurs.getLeiter().getFullName());
		System.out.println("Startdatum: " + kurs.getBeginnFormatted());
		System.out.println("Enddatum: " + kurs.getEndeFormatted());
		Date today = new Date();
		//Differenz / 1000 (ms) * 60 (s) * 60 (min) * 24 (stunden) = differenz in tagen
		long diffBeginn = (today.getTime() - kurs.getBeginn().getTime()) / (1000 * 60 * 60 * 24);
		long diffEnde = (today.getTime() - kurs.getEnde().getTime()) / (1000 * 60 * 60 * 24);
		System.out.println("Der Kurs " + (diffBeginn == 0 ? "begann heute "
				: ((diffBeginn < 0 ? "beginnt in " : "begann vor ") + Math.abs(diffBeginn) + " Tagen "))
				+ "und " + (diffEnde == 0 ? "endet heute "
				: ((diffEnde < 0 ? "endet in " : "endete vor ") + Math.abs(diffEnde) + " Tagen")));

		kursSuchen();
	}

	private static void kursBearbeiten() {
	}


	private static void abbrechen() {
		System.out.println("==========================================================");
		System.out.println("Operation abgebrochen");
	}

	private static int eingabeAbfragenInt(String frage) {
		String eingabe = eingabeAbfragen(frage);

		if (eingabe == null) {
			return -1;
		}

		int parsedEingabe = 0;
		try {
			parsedEingabe = Integer.parseInt(eingabe);
		} catch (NumberFormatException e) {
			ungueltigeEingabe(eingabe);
			return eingabeAbfragenInt(frage);
		}
		return parsedEingabe;
	}

	private static String eingabeAbfragen(String frage) {
		return eingabeAbfragen(frage, true);
	}

	private static String eingabeAbfragen(String frage, boolean abbrechen) {
		System.out.println("==========================================================");
		System.out.println(frage);
		if (abbrechen) {
			System.out.println("(x) um abzubrechen");
		}
		System.out.print("Ihre Eingabe: ");
		String eingabe = scanner.nextLine();
		if (abbrechen && eingabe.equals("x")) {
			abbrechen();
			return null;
		}

		return eingabe;
	}

}
