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
import core.QRobot;
import core.Robot;

/** Classe comportant différents outils
 * 
 * @author DHT
 *
 */
public class Outils {
	
	public static final double DeuxPif = 2 * Math.PI;
	public static Random RAND = new Random();
	

	public static double norme2AB(Vect2 p1, Vect2 p2) {
		return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
	}
	
	/** Sauvegarde une IA d'algo genetique
	 * 
	 * @param ia l'ia genetique
	 * @param name le nom de la simulation
	 */
	public static void saveGBrain(GeneticBrain ia, String name) {
		try {
			FileOutputStream fos = new FileOutputStream(name+".ia");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(ia);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Charge un IA gentique
	 * 
	 * @param name le nom de la simulation
	 * @return l'ia
	 */
	public static GeneticBrain loadGBrain(String name) {
		GeneticBrain ia = null;
		try {
			FileInputStream fis = new FileInputStream(name+".ia");
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
	
	/** Sauvegarde une IA de Q-learning
	 * 
	 * @param ia l'ia de Q-learning
	 * @param name le nom de la simulation
	 */
	public static void saveQBrain(QBrain ia, String name) {
		try {
			FileOutputStream fos = new FileOutputStream(name+".ia");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(ia);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Charge un IA de Q-learning
	 * 
	 * @param name le nom de la simulation
	 * @return l'ia
	 */
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
	
	/** Sauvegarde dans un fichier TXT les resultats de l'IA genetique
	 * 
	 * @param population
	 * @param name
	 * @param nbObjectifs
	 */
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
			 PrintWriter fich = new PrintWriter(new BufferedWriter(new FileWriter(name+".txt", true)));
             try {
                  // 2) Utilisation de l'objet
                  fich.write(res + "\n");
             } finally {
                  // 3) Lib�ration de la ressource exploit�e par l'objet
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
	
	/** Sauvegarde dans un fichier TXT les resultats de l'IA Q-learning
	 * 
	 * @param robot
	 * @param name
	 * @param nbObjectifs
	 */
	public static void saveQResults(QRobot robot , String name, int nbObjectifs) {
		
		 String res = nbObjectifs + "";
		 res += ";" + robot.getNbFoundObj();
		
		
		 try{
			// 1) Instanciation de l'objet
			 PrintWriter fich = new PrintWriter(new BufferedWriter(new FileWriter(name+".csv", true)));
             try {
                  // 2) Utilisation de l'objet
                  fich.write(res + "\n");
             } finally {
                  // 3) Lib�ration de la ressource exploit�e par l'objet
                  fich.close();
             }
		  } catch (IOException er) {;}
		 
	}
	
}
