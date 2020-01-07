package me.pascal.kursverwaltung.dozent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Optional;

public class DozentManager {

	private ArrayList<Dozent> dozenten = new ArrayList<>();
	private final File dozentenDatei = new File("dozenten.txt").getAbsoluteFile();

	public DozentManager() {
		//Initialisierung
		dozentenEinlesen();
	}

	public Dozent getDozentByName(String name) {
		Optional<Dozent> optionalDozent = this.dozenten.stream()
				.filter(dozent -> dozent.getName().equals(name)).findFirst();
		return optionalDozent.orElse(null);
	}

	public void addDozent(Dozent dozent) {
		this.dozenten.add(dozent);
		dozentenSpeichern();
	}

	public ArrayList<Dozent> getDozenten() {
		return this.dozenten;
	}

	private void dozentenEinlesen() {
		try {

			String json = new String(Files.readAllBytes(dozentenDatei.toPath()));
			if (!json.isEmpty()) {
				Type listType = new TypeToken<ArrayList<Dozent>>() {
				}.getType();
				dozenten = new Gson().fromJson(json, listType);
			}
		} catch (FileNotFoundException | NullPointerException e) {
			dozentenDateiErstellen();
			dozentenSpeichern();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void dozentenSpeichern() {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Files.write(dozentenDatei.toPath(), gson.toJson(dozenten).getBytes());
		} catch (FileNotFoundException | NullPointerException e) {
			dozentenDateiErstellen();
			dozentenSpeichern();
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
