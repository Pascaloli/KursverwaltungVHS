package me.pascal.kursverwaltung.kurs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import me.pascal.kursverwaltung.Kursverwaltung;
import me.pascal.kursverwaltung.dozent.Dozent;

public class KursManager {

	private ArrayList<Kurs> kurse = new ArrayList<>();
	private final File kurseDatei = new File("kurse.txt").getAbsoluteFile();

	public KursManager() {
		kurseEinlesen();
	}

	public Kurs getKursById(int id) {
		Optional<Kurs> optionalKurs = this.kurse.stream().filter(kurs -> kurs.getId() == id)
				.findFirst();
		return optionalKurs.orElse(null);
	}

	public ArrayList<Kurs> getKurse() {
		return this.kurse;
	}


	private void kurseEinlesen() {
		try {
			FileReader fileReader = new FileReader(this.kurseDatei);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] split = line.split(":");
				int id = Integer.valueOf(split[0]);
				String title = split[1];
				Date beginn = new Date(split[2]);
				Date ende = new Date(split[3]);
				Dozent dozent = Kursverwaltung.instance.getDozentManager().getDozentByName(split[4]);

				Kurs kurs = new Kurs(id, title, beginn, ende, dozent);
				if (dozent == null) {

				}

				kurse.add(kurs);
			}
			bufferedReader.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			kurseDateiErstellen();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void kurseDateiErstellen() {
		try {
			this.kurseDatei.getParentFile().mkdirs();
			this.kurseDatei.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
