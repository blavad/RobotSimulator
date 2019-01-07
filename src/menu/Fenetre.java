package menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import core.TypeSimu;
import tools.Debug;
import tools.Outils;
 
public class Fenetre extends JFrame {
 
  public JTree arbre;
  private DefaultMutableTreeNode racine;
  private JButton annuler = new JButton("Annuler"), lancer = new JButton("Lancer");
  
  public Fenetre(){
    this.setSize(200, 200);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Choix IA");
    //On invoque la méthode de construction de notre arbre
    listRoot();
 
    this.setVisible(true);
  }
 
  private void listRoot(){
	  
	  
    this.racine = new DefaultMutableTreeNode("ia");
    // Hierarchie contenant les fichiers de Q-learning
    File q_folder = new File(getClass().getResource("/ia/q/").getFile());
    DefaultMutableTreeNode q_ia = new DefaultMutableTreeNode("q");
    try {
    	for(File nom : q_folder.listFiles()){
    		DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName());
    		q_ia.add(this.listFile(nom, node));
    	}
    } catch (NullPointerException e) {}   
    this.racine.add(q_ia);

    // Hierarchie contenant les fichiers d'algo genetique
    File genetic_folder = new File(getClass().getResource("/ia/genetic/").getFile());
    DefaultMutableTreeNode genetic_ia = new DefaultMutableTreeNode("genetic");
    try {
    	for(File nom : genetic_folder.listFiles()){
    		DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName());
    		genetic_ia.add(this.listFile(nom, node));
    	}
    } catch (NullPointerException e) {} 
    this.racine.add(genetic_ia);
    arbre = new JTree(racine);
    
    // Panel de bouton
    JPanel panelBoutton = new JPanel();
    panelBoutton.setLayout(new FlowLayout());
    annuler.addActionListener(new AnnulerControleur(this));
    panelBoutton.add(annuler);
    lancer.addActionListener(new LancerControleur(this));
    panelBoutton.add(lancer);
    
    this.getContentPane().add(new JScrollPane(arbre), BorderLayout.CENTER);
    this.getContentPane().add(panelBoutton, BorderLayout.SOUTH);
  }
     
  private DefaultMutableTreeNode listFile(File file, DefaultMutableTreeNode node){
    int count = 0;
 
    if(file.isFile())
      return new DefaultMutableTreeNode(file.getName());
    else{
      File[] list = file.listFiles();
      if(list == null)
        return new DefaultMutableTreeNode(file.getName()); 
      for(File nom : file.listFiles()){
        count++;
        //Pas plus de 100 enfants par noeud
        if(count < 100){
          DefaultMutableTreeNode subNode;
          if(nom.isDirectory()){
            subNode = new DefaultMutableTreeNode(nom.getName()+"\\");
            node.add(this.listFile(nom, subNode));
          }else{
            subNode = new DefaultMutableTreeNode(nom.getName());
          }
          node.add(subNode);
        }
      }
      return node;
    }
  }
  
 
  public static void main(String[] args){
    Fenetre fen = new Fenetre();
  }
}

class AnnulerControleur implements ActionListener {
	
	/** La fenetre d'appel */
	private Fenetre fenetre;
	
	/** Constructeur du controleur
	 * 
	 * @param f la fenetre liee au controleur 
	 * 
	 */
	public AnnulerControleur(Fenetre f){
		this.fenetre =f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Debug.log.println("Fermeture fenetre " + fenetre.getTitle());
		this.fenetre.setVisible(false);
	}
}

class LancerControleur implements ActionListener {
	
	/** La fenetre d'appel */
	private Fenetre fenetre;
	
	/** Constructeur du controleur
	 * 
	 * @param f la fenetre liee au controleur 
	 * 
	 */
	public LancerControleur(Fenetre f){
		this.fenetre =f;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DefaultMutableTreeNode mon_ia = (DefaultMutableTreeNode)fenetre.arbre.getLastSelectedPathComponent();
		DefaultMutableTreeNode mon_type_ia;
		String s = mon_ia.toString();
		if (!s.isEmpty() && s.charAt(s.length()-1)!='/') {
			mon_type_ia = (DefaultMutableTreeNode) mon_ia.getParent();
			s = mon_type_ia.toString()+ "/" + s;	
		
		s =  getClass().getResource("/ia/").getFile() + s;
		Debug.log.println("Load "+s);
		if (mon_type_ia.toString().equals("q"))
			new TrainingWindow(TypeSimu.TESTIA, Outils.loadQBrain(s));
		else 
			new TrainingWindow(TypeSimu.TESTIA, Outils.loadGBrain(s));
			
		}
	}
}
	
	
	
	