package core;

import java.util.ArrayList;

public class EnsembleObjetP {
	
	private ArrayList<ObjetPlateau> obPX;
	private ArrayList<ObjetPlateau> obPY;
	
	public EnsembleObjetP() {
		obPX = new ArrayList<ObjetPlateau>();
		obPY = new ArrayList<ObjetPlateau>();
	}
	
	public void add(ObjetPlateau ob) {
		boolean place = false;
		// insert l'objet dans le tableau tri� suivant la variable x
		for (int i = 0; i < obPX.size(); i++) {
			if (ob.getPos().x < obPX.get(i).getPos().x) {
				obPX.add(i, ob);
				place = true;
				break;
			}
		}
		if (!place) obPX.add(obPX.size(), ob);
		
		place = false;
		// insert l'objet dans le tableau tri� suivant la variable y
		for (int i = 0; i < obPY.size(); i++) {
			if (ob.getPos().y < obPY.get(i).getPos().y) {
				obPY.add(i, ob);
				place = true;
				break;
			}
		}
		if (!place) obPY.add(obPY.size(), ob);
	}
	
	public ArrayList<ObjetPlateau> getObPX() {
		return obPX;
	}
	public ArrayList<ObjetPlateau> getObPY() {
		return obPY;
	}
	
	
	
}
