package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Utile {

    public static ArrayList<Edge> steinerBase(ArrayList<Point> points, int edgeThreshold, ArrayList<Point> hitPoints) {
        Kruskal kruskal = new Kruskal();
        ArrayList<Edge> edges = kruskal.kruskal(hitPoints);

        int[][] paths = FloydWarshall.floydWarshall(points, edgeThreshold);
        ArrayList<Edge> newEdges = new ArrayList<>();
        HashSet<Point> pointsInclus = new HashSet<>();

        for (Edge edge : edges) {
            ArrayList<Integer> path = FloydWarshall.reconstructPath(paths, points.indexOf(edge.p), points.indexOf(edge.q));
            if (path.isEmpty()) {
                continue;
            }

            Point current = edge.p;
            pointsInclus.add(current);

            for (int i = 1; i < path.size(); i++) {
                Point next = points.get(path.get(i));
                newEdges.add(new Edge(current, next));
                pointsInclus.add(next);
                current = next;
            }
        }

        ArrayList<Point> finalPoints = new ArrayList<>(pointsInclus);
        ArrayList<Edge> optimizedEdges = kruskal.kruskal(finalPoints);
        ArrayList<Edge> finalTree = new ArrayList<>();

        for (Edge edge : optimizedEdges) {
            if (edge.distance() <= edgeThreshold) {
                finalTree.add(edge);
            }
        }
        return finalTree;
    }

    public static ArrayList<Edge> optimizeSteiner(
            ArrayList<Point> points, ArrayList<Edge> edges,
            ArrayList<Point> hitPoints, int edgeThreshold, int budgetLimit) {

        // Structures de stockage des meilleurs résultats
        ArrayList<Edge> bestEdges = new ArrayList<>();
        double bestDistance = Double.MAX_VALUE;
        int maxConnectedPoints = 0;

        if (hitPoints.isEmpty()) {
            return bestEdges;
        }

        // Parcourir plusieurs points de départ pour trouver la meilleure 
        for (Point startPoint : hitPoints) {
            HashSet<Point> connectedPoints = new HashSet<>();
            ArrayList<Edge> currentEdges = new ArrayList<>();
            double totalDistance = 0;

            connectedPoints.add(startPoint);

            // Trier les hitPoints selon la distance au point de départ testé
            ArrayList<Point> sortedHitPoints = new ArrayList<>(hitPoints);
            sortedHitPoints.sort(Comparator.comparingDouble(p -> p.distance(startPoint)));

            // File de priorité pour sélectionner les meilleures connexions
            PriorityQueue<Edge> edgeQueue = new PriorityQueue<>(Comparator.comparingDouble(Edge::distance));
            edgeQueue.addAll(edges);

            while (!edgeQueue.isEmpty() && totalDistance < budgetLimit) {
                Edge bestEdge = null;
                double minDistance = Double.MAX_VALUE;
                Point newPoint = null;

                // Sélectionner la connexion la plus efficace
                for (Edge edge : edgeQueue) {
                    if ((connectedPoints.contains(edge.p) || connectedPoints.contains(edge.q))
                            && edge.distance() < minDistance) {

                        Point candidatePoint = connectedPoints.contains(edge.p) ? edge.q : edge.p;

                        if (!connectedPoints.contains(candidatePoint) && (totalDistance + edge.distance()) <= budgetLimit) {
                            bestEdge = edge;
                            minDistance = edge.distance();
                            newPoint = candidatePoint;
                        }
                    }
                }

                // Vérifier si une connexion valide a été trouvée
                if (bestEdge == null || newPoint == null) {
                    break; // Arrêt si aucune nouvelle connexion possible
                }

                // Ajouter la connexion et le nouveau point
                currentEdges.add(bestEdge);
                totalDistance += bestEdge.distance();
                connectedPoints.add(newPoint);
                edgeQueue.remove(bestEdge);
            }

            // Comparer cette configuration aux meilleures trouvées
            if (connectedPoints.size() > maxConnectedPoints
                    || (connectedPoints.size() == maxConnectedPoints && totalDistance < bestDistance)) {
                bestEdges = new ArrayList<>(currentEdges);
                bestDistance = totalDistance;
                maxConnectedPoints = connectedPoints.size();
            }
        }

        return bestEdges;
    }

}
