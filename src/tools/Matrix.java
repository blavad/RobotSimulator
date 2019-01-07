package tools;

import java.io.Serializable;

/** Classe matrcie fournissant les fonctions necessaires a la manipulation de matrices
 * 
 * @author DHT
 * 
 */
public class Matrix  implements Serializable{
	
	/** 
	 * Les coefficients de la matrice
	 */
	private double[][] a;
	
	public Matrix(double[][] a){
		this.a = a;
	}
	
	public Matrix(int l, int c){
		this.a = new double[l][c];
	}
	
	public Matrix(double[] a) {
		this.a = new double[a.length][1];
		for (int i=0; i<a.length;i++) {
			this.a[i][0] = a[i];
		}
	}

	/** Attribue des parametres aleatoire selon la loi Gaussienne a la matrice */
	public void random(){
		for (int i = 0; i < getNbLin(); i++)
            for (int j = 0; j < getNbCol(); j++)
                this.a[i][j] = Outils.RAND.nextGaussian();
	}
	/** Mets a 0 les coefficients de la matrice */
	public void nulle(){
		for (int i = 0; i < getNbLin(); i++)
            for (int j = 0; j < getNbCol(); j++)
                this.a[i][j] = 0.;
	}
	
	/** Mutltiplie 2 matrices
	 * 
	 * @param a
	 * @param b
	 * @return le resultat de la multiplication matricielle
	 */
	public static Matrix multiply(Matrix a, Matrix b){
		int la = a.getNbLin(), ca = a.getNbCol(), lb = b.getNbLin(), cb = b.getNbCol();
		if (ca != lb)
			throw new RuntimeException("Multiplication de matrices incorrecte "+a+" et "+b);
		double[][] c = new double[la][cb];
        for (int i = 0; i < la; i++)
            for (int j = 0; j < cb; j++)
            	for (int k = 0; k < ca; k++)
                    c[i][j] += a.get(i, k) * b.get(k, j);
        return new Matrix(c);
	}
	
	/** Applique la fonction sigmoid sur tous les coefs de la matrice
	 * 
	 * @param a
	 * @return
	 */
	public static Matrix sigmoid(Matrix a){
		int la = a.getNbLin(), ca = a.getNbCol();
		double[][] c = new double[la][ca];
        for (int i = 0; i < la; i++)
            for (int j = 0; j < ca; j++)
            	c[i][j] = 1./(1.+Math.exp(-a.get(i, j))); // Application de la fonction sigmoid au coef
        return new Matrix(c);
	}
	
	public double[][] copy(){
		return this.a;
	}

	/** Recupere l'argument du maximum du vecteur
 	 * 
	 * @return la position du maximum
	 */
	public int argmax() {
		if (this.getNbCol()>1){
			throw new RuntimeException("Argmax ne peut être appele sur une matrice "+ this.getNbLin() + "x"+this.getNbCol());
		}
		int amax = 0;
		for (int i=1; i<this.getNbLin();i++){
			if (a[i][0] > a[amax][0]) {
				amax = i;
			}
		}
		return amax;
	}
	
	public double get(int i, int j){
		return this.a[i][j];
	}
	
	public void set(int i, int j, double value){
		this.a[i][j] = value;
	}
	
	public int getNbCol(){
		return this.a[0].length;
	}
	
	public int getNbLin(){
		return this.a.length;
	}
	
	public String toString() {
		return "("+this.getNbLin()+"x"+this.getNbCol()+")";
	}		
}
