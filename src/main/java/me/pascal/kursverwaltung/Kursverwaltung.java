package me.pascal.kursverwaltung;

import com.google.gson.Gson;
import me.pascal.kursverwaltung.dozent.DozentManager;
import me.pascal.kursverwaltung.kurs.KursManager;

public class Kursverwaltung {

	public static final Kursverwaltung instance = new Kursverwaltung();

	private DozentManager dozentManager;
	private KursManager kursManager;

	public void init(){
		dozentManager = new DozentManager();
		kursManager = new KursManager();

		KursverwaltungsMenue.start();
	}

	public DozentManager getDozentManager(){
		return dozentManager;
	}

	public KursManager getKursManager(){
		return kursManager;
	}

}
