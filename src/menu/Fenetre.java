package menu;

import java.awt.BorderLayout;
import java.io.File;
 
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import tools.Debug;
 
public class Fenetre extends JFrame {
 
  private JTree arbre;
  private DefaultMutableTreeNode racine;
  private Panneau panneau = new Panneau();
 
  public Fenetre(){
    this.setSize(500, 200);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("Les arbres");
    //On invoque la méthode de construction de notre arbre
    listRoot();
 
    this.setVisible(true);
  }
 
  private void listRoot(){
 
    this.racine = new DefaultMutableTreeNode("IA");
 
    File q_folder = new File(getClass().getResource("/ia/q/").getFile());
    DefaultMutableTreeNode q_ia = new DefaultMutableTreeNode("Q");
    try {
    	for(File nom : q_folder.listFiles()){
    		DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName());
    		q_ia.add(this.listFile(nom, node));
    	}
    } catch (NullPointerException e) {}
    
    this.racine.add(q_ia);
    
    
    File genetic_folder = new File(getClass().getResource("/ia/genetic/").getFile());
    DefaultMutableTreeNode genetic_ia = new DefaultMutableTreeNode("Genetic");
    try {
    	for(File nom : genetic_folder.listFiles()){
    		DefaultMutableTreeNode node = new DefaultMutableTreeNode(nom.getName());
    		genetic_ia.add(this.listFile(nom, node));
    	}
    } catch (NullPointerException e) {}
    
    this.racine.add(genetic_ia);
             
    //On crée, avec notre hiérarchie, un arbre
    arbre = new JTree(this.racine);
    arbre.setRootVisible(false);
    arbre.addTreeSelectionListener(new TreeSelectionListener(){
      public void valueChanged(TreeSelectionEvent event) {
        if(arbre.getLastSelectedPathComponent() != null){
          File file= new File(getAbsolutePath(event.getPath()));
          panneau.setTexte(getDescription(file));
        }
      }
 
      private String getAbsolutePath(TreePath treePath){
        String str = "";
        //On balaie le contenu de notre TreePath
        for(Object name : treePath.getPath()){
          //Si l'objet à un nom, on l'ajoute au chemin
          if(name.toString() != null)
            str += name.toString();
        }
        return str;
      }
 
      /**
      * Retourne une description d'un objet File
      * @param file
      * @return
      */
      private String getDescription(File file){
        String str = "Chemin d'accès sur le disque : \n\t" + file.getAbsolutePath() + "\n";
        str += (file.isFile()) ? "Je suis un fichier.\nJe fais " + file.length() + " ko\n" : "Je suis un dossier.\n";
        str += "J'ai des droits : \n";
        str += "\t en lecture : " + ((file.canRead()) ? "Oui;" : "Non;");
        str += "\n\t en écriture : " + ((file.canWrite()) ? "Oui;" : "Non;");
        return str;
      }
    });
    //On crée un séparateur de conteneur pour réviser
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(arbre), new JScrollPane(panneau));
    //On place le séparateur
    split.setDividerLocation(200);
    //On ajoute le tout
    this.getContentPane().add(split, BorderLayout.CENTER);
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
        //Pas plus de 5 enfants par nœud
        if(count < 5){
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