package algorithms;

import java.awt.Point;
import java.util.ArrayList;

public class DefaultTeam {
  public Tree2D calculSteiner(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
        Kruskal k = new Kruskal();//on utilise l'algorithme de Kruskal pour trouver l'arbre de Steiner
        ArrayList<Edge> steinerEdges = k.kruskal(hitPoints);  //on
        Tree2D steinerTree = k.edgesToTree(steinerEdges, hitPoints.get(0));
        int[][] paths = FloydWarshall.floydWarshall(points, edgeThreshold);
        return optimiSteinerTree(paths, steinerTree, points);
    }

    /*
     * cette méthode optimise l'arbre de Steiner en remplaçant 
     * chaque arête directe par le plus court chemin trouvé grâce à Floyd-Warshall.
     */
    private Tree2D optimiSteinerTree(int[][] paths, Tree2D steinerTree, ArrayList<Point> points) {
        Point root = steinerTree.getRoot();
        ArrayList<Tree2D> newChildren = new ArrayList<>();
        // Pour chaque sous-arbre, on vérifie si le prochain point est un enfant direct ou s'il faut ajouter un point intermédiaire
        for (Tree2D subtree : steinerTree.getSubTrees()) {
            Point child = subtree.getRoot();
            int nextIndex = paths[points.indexOf(root)][points.indexOf(child)];//on cherche le prochain point pour 
            //lequel on doit ajouter un point intermédiaire

            //si le prochain point est un enfant direct, on l'ajoute tel quel
            if (points.get(nextIndex).equals(child)) {
                newChildren.add(optimiSteinerTree(paths, subtree, points));
            } else {
                Point intermediate = points.get(nextIndex);//on ajoute un point intermédiaire si le prochain point n'est pas un enfant direct
                ArrayList<Tree2D> temp = new ArrayList<>();
                temp.add(subtree); 
                Tree2D newTree = new Tree2D(intermediate, temp);
                newChildren.add(optimiSteinerTree(paths, newTree, points));
            }
        }
        
        return new Tree2D(root, newChildren);
    }
    
    
  /*public Tree2D calculSteinerBudget(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
    //REMOVE >>>>>
    Tree2D leafX = new Tree2D(new Point(700,400),new ArrayList<Tree2D>());
    Tree2D leafY = new Tree2D(new Point(700,500),new ArrayList<Tree2D>());
    Tree2D leafZ = new Tree2D(new Point(800,450),new ArrayList<Tree2D>());
    ArrayList<Tree2D> subTrees = new ArrayList<Tree2D>();
    subTrees.add(leafX);
    subTrees.add(leafY);
    subTrees.add(leafZ);
    Tree2D steinerTree = new Tree2D(new Point(750,450),subTrees);
    //<<<<< REMOVE

    return steinerTree;
  }*/
}
