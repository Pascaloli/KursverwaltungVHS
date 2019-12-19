package me.pascal.kursverwaltung;

import java.util.Scanner;
import java.util.stream.IntStream;
import me.pascal.kursverwaltung.dozent.Dozent;

public class KursverwaltungsMenue {

	private static Scanner scanner = new Scanner(System.in);

	public static void start() {
		askForMainMenuOptions();
	}

	private static void askForMainMenuOptions() {
		System.out.println("==========================================================");
		System.out.println("Willkommen in der VHS Kursverwaltung 2020, was möchten sie tun?");
		System.out.println("(1) Einen Dozenten anlegen");
		System.out.println("(2) Einen Kurs anlegen");
		System.out.println("(3) Einen Kurs suchen");
		System.out.println("(4) Einen Kurs bearbeiten");
		System.out.print("Ihre Auswahl: ");

		String eingabe = scanner.nextLine();
		switch (eingabe) {
			case "1":
				dozentenAnlegen();
				break;
			case "2":
				kursAnlegen();
				break;
			case "3":
				kursSuchen();
				break;
			case "4":
				kursBearbeiten();
				break;
			default:
				ungueltigeEingabe(eingabe);
				askForMainMenuOptions();
		}
	}

	private static void ungueltigeEingabe(String eingabe) {
		System.out.println("==========================================================");
		System.out.println("Ungültige Eingabe: '" + eingabe + "'");
	}

	private static void dozentenAnlegen() {
		Dozent dozent = new Dozent();
		do {
			System.out.println("==========================================================");
			System.out.println("Wie heißt der Dozent, welchen Sie anlegen wollen?");
			System.out.print("Ihre Eingabe: ");
			String name = scanner.nextLine();

			if (Kursverwaltung.instance.getDozentManager().getDozentByName(name) != null) {
				System.out.println("==========================================================");
				System.out.println("Ein Dozent mit diesem Namen existiert bereits.");
				System.out.println("Bitte vergeben Sie einen eindeutigen Namen.");
			} else if (!dozent.setName(name)) {
				ungueltigeEingabe(name);
			}


		} while (dozent.getName() == null);

	}

	private static void kursAnlegen() {
		System.out.println("==========================================================");
		System.out.println("Bitte wählen Sie einen Kursnamen aus.");
		System.out.print("Ihre Eingabe: ");
		String kursname = scanner.nextLine();

	}

	private static void kursSuchen() {
	}

	private static void kursBearbeiten() {
	}

	private static void clearOutput() {
		IntStream.range(0, 100).forEach(i -> System.out.println(""));
	}

}
