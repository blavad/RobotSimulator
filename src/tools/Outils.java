package tools;

import java.util.Random;

public class Outils {
	
	public static final double DeuxPif = 2 * Math.PI;
	public static Random RAND = new Random();
	

	public static double norme2AB(Vect2 p1, Vect2 p2) {
		return Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2);
	}
	
}
