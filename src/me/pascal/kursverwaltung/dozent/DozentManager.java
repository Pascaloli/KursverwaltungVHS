package me.pascal.kursverwaltung.dozent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class DozentManager {

	private ArrayList<Dozent> dozenten = new ArrayList<>();
	private final File dozentenDatei = new File("dozenten.txt").getAbsoluteFile();

	public DozentManager() {
		dozentenEinlesen();
	}

	public Dozent getDozentByName(String name) {
		Optional<Dozent> optionalDozent = this.dozenten.stream()
				.filter(dozent -> dozent.getName().equals(name)).findFirst();
		return optionalDozent.orElse(null);
	}

	public ArrayList<Dozent> getDozenten() {
		return this.dozenten;
	}

	private void dozentenEinlesen() {
		try {
			FileReader fileReader = new FileReader(this.dozentenDatei);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] split = line.split(":");
				String title = split[0];
				String name = split[1];
				String email = split[2];
				String telNr = split[3];

				Dozent dozent = new Dozent(title, name, email, telNr);
				this.dozenten.add(dozent);
			}
			bufferedReader.close();
			fileReader.close();
		} catch (FileNotFoundException | NullPointerException e) {
			dozentenDateiErstellen();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void dozentenDateiErstellen() {
		try {
			if (!this.dozentenDatei.getParentFile().exists()) {
				this.dozentenDatei.getParentFile().mkdirs();
			}
			this.dozentenDatei.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
