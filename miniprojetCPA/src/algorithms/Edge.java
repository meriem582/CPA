package algorithms;

import java.awt.Point;
import java.util.Objects;

public class Edge implements Comparable<Edge> {

    public final Point p;
    public final Point q;
    public final double dist;

    public Edge(Point u, Point v) {
        this.p = u;
        this.q = v;
        this.dist = u.distance(v);
    }

    public Edge(Point u, Point v, double dist) {
        this.p = u;
        this.q = v;
        this.dist = dist;
    }

    public double distance() {
        return dist;
    }

    public Point getP() {
        return p;
    }

    public Point getQ() {
        return q;
    }

    public double getDist() {
        return dist;
    }

    /**
     * Compare les arêtes en fonction de leur distance pour une utilisation dans une PriorityQueue.
     */
    @Override
    public int compareTo(Edge other) {
        return Double.compare(this.dist, other.dist);
    }

    /**
     * Vérifie si deux arêtes sont identiques (indépendamment de l'ordre des sommets).
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge edge = (Edge) obj;
        return (Objects.equals(p, edge.p) && Objects.equals(q, edge.q)) ||
               (Objects.equals(p, edge.q) && Objects.equals(q, edge.p));
    }

    @Override
    public int hashCode() {
        return Objects.hash(p, q) + Objects.hash(q, p);
    }

    @Override
    public String toString() {
        return "Edge{" + p + " ↔ " + q + ", dist=" + dist + "}";
    }

    /**
     * Calcule l'angle entre cette arête et une autre arête.
     *
     * @param other L'autre arête
     * @return L'angle en degrés entre les deux arêtes
     */
    public double getAngleWith(Edge other) {
        double dx1 = this.q.x - this.p.x;
        double dy1 = this.q.y - this.p.y;
        double dx2 = other.q.x - other.p.x;
        double dy2 = other.q.y - other.p.y;

        double angle1 = Math.atan2(dy1, dx1);
        double angle2 = Math.atan2(dy2, dx2);

        double angleDiff = Math.toDegrees(angle1 - angle2);
        if (angleDiff < 0) angleDiff += 360; // Normalisation de l'angle

        return angleDiff;
    }
}
