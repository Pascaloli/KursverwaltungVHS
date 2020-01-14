package me.pascal.kursverwaltung.kurs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import me.pascal.kursverwaltung.dozent.Dozent;

public class KursManager {

	private ArrayList<Kurs> kurse = new ArrayList<>();
	private final File kurseDatei = new File("kurse.txt").getAbsoluteFile();


	public KursManager() {
		kurseEinlesen();
	}

	public Kurs getKursByTitle(String name) {
		Optional<Kurs> optionalKurs = this.kurse.stream()
				.filter(kurs -> kurs.getTitle().equals(name)).findFirst();
		return optionalKurs.orElse(null);
	}

	public boolean isDozentInKursen(Dozent dozent) {
		Optional<Kurs> optionalKurs = this.kurse.stream()
				.filter(kurs -> kurs.getLeiter().equals(dozent)).findFirst();
		return optionalKurs.isPresent();
	}

	public List<Kurs> getKurseByDozent(Dozent dozent) {
		List<Kurs> kurse = this.kurse.stream()
				.filter(kurs -> kurs.getLeiter().equals(dozent)).collect(Collectors.toList());
		return kurse;
	}

	public void addKurs(Kurs kurs) {
		this.kurse.add(kurs);
		kurseSpeichern();
	}

	public ArrayList<Kurs> getKurse() {
		return this.kurse;
	}

	private void kurseEinlesen() {
		try {
			String json = new String(Files.readAllBytes(kurseDatei.toPath()));
			if (!json.isEmpty()) {
				Type listType = new TypeToken<ArrayList<Kurs>>() {
				}.getType();
				kurse = new Gson().fromJson(json, listType);

			/*	kurse.forEach(kurs -> {
					Dozent realDozent = Kursverwaltung.instance.getDozentManager().getDozentByDozent(kurs.getLeiter());
					if(realDozent == null){
						Kursverwaltung.instance.getDozentManager().addDozent(kurs.getLeiter());
						realDozent = Kursverwaltung.instance.getDozentManager().getDozentByDozent(kurs.getLeiter());
					}
					kurs.setLeiter(realDozent);
				});*/

			}
		} catch (NoSuchFileException | NullPointerException e) {
			kurseDateiErstellern();
			kurseSpeichern();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void kurseSpeichern() {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			Files.write(kurseDatei.toPath(), gson.toJson(kurse).getBytes());
		} catch (FileNotFoundException | NullPointerException e) {
			kurseDateiErstellern();
			kurseSpeichern();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void kurseDateiErstellern() {
		try {
			if (!this.kurseDatei.getParentFile().exists()) {
				this.kurseDatei.getParentFile().mkdirs();
			}
			this.kurseDatei.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}