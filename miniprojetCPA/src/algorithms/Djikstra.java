package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.awt.Point;
import java.util.Map;
import java.util.PriorityQueue;


public class Djikstra {


    public static ArrayList<Integer> djikstra(ArrayList<Point> points, HashMap<Integer, List<Integer>> graph, int start, int goal) {
        //on utilise une file de priorité car c'est plus rapide pour trouver le chemin le plus court
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1])); 
        Map<Integer, Integer> previousNodes = new HashMap<>(); 
        Map<Integer, Double> distances = new HashMap<>();  

        pq.add(new int[]{start, 0});  //on ajoute le point de départ à la file de priorité
        distances.put(start, 0.0); 
        previousNodes.put(start, -1); //on met -1 pour dire que c'est le point avant le point de départ

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];

    
            if (u == goal) {
                return reconstructPath(previousNodes, goal);
            }
            for (int v : graph.getOrDefault(u, new ArrayList<>())) {
                double weight = points.get(u).distance(points.get(v));
                double newDist = distances.getOrDefault(u, Double.MAX_VALUE) + weight;

                if (newDist < distances.getOrDefault(v, Double.MAX_VALUE)) {
                    distances.put(v, newDist);
                    previousNodes.put(v, u);
                    pq.add(new int[]{v, (int) newDist});
                }
            }
        }
        return new ArrayList<>();
    }

    private static ArrayList<Integer> reconstructPath(Map<Integer, Integer> previousNodes, int goal) {
        ArrayList<Integer> path = new ArrayList<>();
        while (goal != -1) {
            path.add(goal);
            goal = previousNodes.getOrDefault(goal, -1);
        }
        Collections.reverse(path);
        return path;
    }
    
}