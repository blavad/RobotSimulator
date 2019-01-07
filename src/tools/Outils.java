package tools;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import core.GeneticBrain;
import core.GeneticRobot;
import core.QBrain;
import core.Robot;

public class Outils {
	
	public static final double DeuxPif = 2 * Math.PI;
	public static Random RAND = new Random();
	

	public static double norme2AB(Vect2 p1, Vect2 p2) {
		return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
	}
	
	
	public static void saveGBrain(GeneticBrain ia, String name) {
		try {
			FileOutputStream fos = new FileOutputStream(name);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(ia);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static GeneticBrain loadGBrain(String name) {
		GeneticBrain ia = null;
		try {
			FileInputStream fis = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ia = (GeneticBrain)ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ia;
	}
	
	public static void saveQBrain(QBrain ia, String name) {
		try {
			FileOutputStream fos = new FileOutputStream(name);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(ia);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static QBrain loadQBrain(String name) {
		QBrain ia = null;
		try {
			FileInputStream fis = new FileInputStream(name);
			ObjectInputStream ois = new ObjectInputStream(fis);
			ia = (QBrain)ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ia;
	}
	
	public static void saveGResults(ArrayList<GeneticRobot> population , String name, int nbObjectifs) {
		
		float[] res1 = new float[nbObjectifs + 1];
		for (Robot r : population) {
			res1[r.getNbFoundObj()] += 1;
		}
		
		 String res = nbObjectifs + "";
		 for (int i = 0; i <= nbObjectifs; i++) {
			 res += ";" + (100 * res1[i] / population.size());
		 }
		
		
		 try{
			// 1) Instanciation de l'objet
			 PrintWriter fich = new PrintWriter(new BufferedWriter(new FileWriter(name, true)));
             try {
                  // 2) Utilisation de l'objet
                  fich.write(res + "\n");
             } finally {
                  // 3) Libération de la ressource exploitée par l'objet
                  fich.close();
             }
		  } catch (IOException er) {;}
		 
		/*try {
			FileOutputStream fos = new FileOutputStream(name);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			//oos.writeObject(ia);
			oos.close();
			System.out.println("Sauvegarde des resultats reussie");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
}
