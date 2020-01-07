package me.pascal.kursverwaltung;

import java.util.Date;
import java.util.Scanner;
import java.util.stream.IntStream;
import me.pascal.kursverwaltung.dozent.Dozent;
import me.pascal.kursverwaltung.kurs.Kurs;

public class KursverwaltungsMenue {

	private static Scanner scanner = new Scanner(System.in);

	public static void start() {
		menu();
	}

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
					"Wie lautet die Email von " + dozent.getTitle() + " " + dozent.getName() + "?");
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
					"Wie lautet die Telefonnummer von " + dozent.getTitle() + " " + dozent.getName() + "?");
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
		System.out.println("Der Dozent " + dozent.getTitle() + " " + dozent.getName()
				+ " wurde erfolgreich angelegt.");
	}

	private static void dozentenBearbeitenUebersicht() {
		int index = 1;
		StringBuilder uebersicht = new StringBuilder();
		for (Dozent dozent : Kursverwaltung.instance.getDozentManager().getDozenten()) {
			uebersicht.append("(" + index++ + ") " + dozent.getTitle() + " " + dozent.getName() + "\n");
		}

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

		dozentBearbeiten(auswahl - 1);
	}

	private static void dozentBearbeiten(int index) {
		StringBuilder uebersicht = new StringBuilder();
		Dozent dozent = Kursverwaltung.instance.getDozentManager().getDozenten().get(index);
		uebersicht.append("(1) Titel (").append(dozent.getTitle()).append(")\n");
		uebersicht.append("(2) Name (" + dozent.getName() + ")\n");
		uebersicht.append("(3) Email (" + dozent.getEmail() + ")\n");
		uebersicht.append("(4) Telefonnummer (" + dozent.getTelNr() + ")\n");
		uebersicht.append("(5) Löschen");

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

		if (eingabe == 5) {
			String bestätigung = eingabeAbfragen("Löschen bestätigen? j/n", false);
			if (bestätigung.equalsIgnoreCase("j") || bestätigung.equalsIgnoreCase("Y")) {
				Kursverwaltung.instance.getDozentManager().getDozenten().remove(index);
				Kursverwaltung.instance.getDozentManager().dozentenSpeichern();
				System.out.println("==========================================================");
				System.out.println("Der Dozent wurde erfolgreich gelöscht.");
				dozentenBearbeitenUebersicht();
				return;
			} else if (bestätigung.equalsIgnoreCase("n")) {
				abbrechen();
				dozentBearbeiten(index);
				return;
			}
		}

		boolean valueChanged = false;
		do {
			String neuerWert = eingabeAbfragen("Bitte geben sie einen neuen Wert ein.");
			if (neuerWert == null) {
				dozentBearbeiten(index);
				return;
			}

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

			if(!valueChanged){
				ungueltigeEingabe(neuerWert);
			}


		} while (!valueChanged);

		Kursverwaltung.instance.getDozentManager().dozentenSpeichern();
		System.out.println("==========================================================");
		System.out.println("Wert erfolgreich aktualisiert");
		dozentBearbeiten(index);
	}


	private static void kursAnlegen() {
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

		kurs.setBeginn(new Date(20158184));
		kurs.setEnde(new Date(98413156));
		kurs.setLeiter(Kursverwaltung.instance.getDozentManager().getDozenten().get(0));

		Kursverwaltung.instance.getKursManager().addKurs(kurs);
		System.out.println("==========================================================");
		System.out.println("Der Kurs " + kurs.getTitle() + " wurde erfolgreich angelegt.");
		menu();
	}

	private static void kursSuchen() {
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

	private static void clearOutput() {
		IntStream.range(0, Integer.MAX_VALUE).forEach(i -> System.out.println());
	}

}
