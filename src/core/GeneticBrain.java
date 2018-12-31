package core;

import java.util.ArrayList;

import capteur.EnsembleDeCapteurs;
import tools.Debug;
import tools.Matrix;
import tools.Outils;

public class GeneticBrain extends IA {

	/** Nombre de neuronne de chaque couche */
	private ArrayList<Integer> layer = new ArrayList<Integer>();
	
	/** Ensemble des genes/parametres/poids de chaque liaison neuronale */
	private ArrayList<Matrix> params = new ArrayList<Matrix>();
	
	
	/** Construit un NN d'apres un ensemble de taille de couches
	 * 
	 * @param layer
	 */
	public GeneticBrain(int ... layer) {
		this.layer.add(layer[0]);
		for (int prevL = 0, nextL = 1; nextL < layer.length; prevL++ ,nextL++){
			this.layer.add(layer[nextL]);
			Matrix m = new Matrix(layer[nextL], layer[prevL]);
			m.random();
			this.params.add(m);
		}
	}
	
	/** Construit un NN d'apres une liste de tailles de couche
	 * 
	 * @param layer
	 */
	public GeneticBrain(ArrayList<Integer> layer) {
		this.layer.add(layer.get(0));
		for (int prevL = 0, nextL = 1; nextL < layer.size(); prevL++ ,nextL++){
			this.layer.add(layer.get(nextL));
			this.params.add(new Matrix(layer.get(nextL), layer.get(prevL)));
		}
	}

	/** Forward du reseau de neuronne
	 * 
	 * @param listCapteur l'ensemble des capteurs
	 * @return le numero de l'action a executer
	 */
	public int output(EnsembleDeCapteurs ... listCapteur){
		// Initialisation de la matrice d'entree
		double[][] a = new double[layer.get(0)][1];
		
		// Recuperation des donnees des capteurs
		for (EnsembleDeCapteurs lcapt : listCapteur){
			for (int i = 0; i < lcapt.getSize(); i++){
				a[i][0] = lcapt.getCapteur(i).getValue();
			}
		}
		int n = params.size();
		Matrix[] o = new Matrix[n+1];
		o[0] = new Matrix(a);
		// Pour chaque couche de neuronne, on calcul la sommation et on la fonction d'activation
		for (int i=0; i<n; i++){
			o[i+1] = Matrix.multiply(params.get(i),o[i]);
			o[i+1] = Matrix.sigmoid(o[i+1]);
		}
		int res = o[n].argmax();
		
		return res;
	}
	
	/** Croisement des genes
	 * 
	 * @param brain2 
	 * @return 
	 */
	public GeneticBrain cross(GeneticBrain brain2) {
		GeneticBrain newBrain = new GeneticBrain(layer);
		for (int m=0; m<params.size(); m++) {
			for (int l=0; l<params.get(m).getNbLin(); l++) {
				for(int c=0; c<params.get(m).getNbCol(); c++) {
					GeneticBrain b = Outils.RAND.nextBoolean()? brain2: this;
					newBrain.params.get(m).set(l,c,b.getParams().get(m).get(l, c));
				}
			}
		}
		return newBrain;
	}
	
	/** Mutation
	 * 
	 * @param brain2 
	 * @return 
	 */
	public GeneticBrain mutate(double mutationRate) {
		GeneticBrain newBrain = new GeneticBrain(layer);
		for (int m=0; m<params.size(); m++) {
			for (int l=0; l<params.get(m).getNbLin(); l++) {
				for(int c=0; c<params.get(m).getNbCol(); c++) {
					if (Math.random() <= mutationRate)
						newBrain.params.get(m).set(l,c,Outils.RAND.nextGaussian());
					else
						newBrain.params.get(m).set(l,c,this.getParams().get(m).get(l, c));
				}
			}
		}
		return newBrain;
	}
	
	public ArrayList<Matrix> getParams() {
		return this.params;
	}
	
	public ArrayList<Integer> getLayers() {
		return this.layer;
	}
	
	public String toString(){
		String str = "";
		for (Integer l : layer){
			str += " " + l + " ";
		}
		return "Genetic Neural Network Fully Connected (" + str + ")";
	}

}
