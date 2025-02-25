package algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import supportGUI.Circle;
import supportGUI.Line;

/***************************************************************
 * TME 1: calcul de diamètre et de cercle couvrant minimum. * - Trouver deux
 * points les plus éloignés d'un ensemble de * points donné en entrée. * -
 * Couvrir l'ensemble de poitns donné en entrée par un * cercle de rayon
 * minimum. * * class Circle: * - Circle(Point c, int r) constructs a new circle
 * * centered at c with radius r. * - Point getCenter() returns the center
 * point. * - int getRadius() returns the circle radius. * * class Line: * -
 * Line(Point p, Point q) constructs a new line * starting at p ending at q. * -
 * Point getP() returns one of the two end points. * - Point getQ() returns the
 * other end point. *
 ***************************************************************/

public class DefaultTeam {

	// calculDiametre: ArrayList<Point> --> Line
	// renvoie une paire de points de la liste, de distance maximum.
	public Line calculDiametre(ArrayList<Point> points) {
		if (points.size() < 2) {
			return null;
		}

		Point p = points.get(0);
		Point q = points.get(1);

		double d = 0;
		for (Point p1 : points) {
			for (Point p2 : points) {
				double d2 = distance(p1, p2);
				if (d2 > d) {
					p = p1;
					q = p2;
					d = d2;
				}
			}
		}

		System.out.println("diametre norm : " + distance(p, q));

		return new Line(p, q);
	}

	// distance : Point * Point --> Double
	// fonction utiliser pour reduire le nombre d'operation lors du calcul de la
	// distance
	public static double distance(Point a, Point b) {
		double tmp1 = a.getX() - b.getX();
		double tmp2 = a.getY() - b.getY();
		return tmp1 * tmp1 + tmp2 * tmp2;
	}

	// calculCercleMin: ArrayList<Point> --> Circle
	// renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	// decommanter la méthode que vous voulez utilisé
	public Circle calculCercleMin(ArrayList<Point> inputPoints) {
		return calculCercleMinAlgoNaif(inputPoints);
//		return CalculCercleMinAlgoWelzl(inputPoints);
	}

	// calculCercleMinAlgoNaif : ArrayList<Point> --> Circle
	// l'implémentation d'algorithme naif ( c'est celle du prof avec quelques
	// modification apporté )
	public static Circle calculCercleMinAlgoNaif(ArrayList<Point> points) {
//		System.out.println("Commencement du calcul du cercle minimum");
		if (points.size() < 1)
			return null;
		double cX, cY, cRadiusSquared, dx, dy;
		for (Point p : points) {
			for (Point q : points) {
				cX = .5 * (p.x + q.x);
				cY = .5 * (p.y + q.y);
				dx = p.x - q.x;
				dy = p.y - q.y;
				cRadiusSquared = 0.25 * distance(p, q);
				boolean allHit = true;
				for (Point s : points) {
					dx = (s.x - cX);
					dy = (s.y - cY);
					if (dx * dx + dy * dy > cRadiusSquared) {
						allHit = false;
						break;
					}
				}
				if (allHit)
					return new Circle(new Point((int) cX, (int) cY), (int) Math.sqrt(cRadiusSquared));
			}
		}
		double resX = 0;
		double resY = 0;
		double resRadiusSquared = Double.MAX_VALUE;
		for (int i = 0; i < points.size(); i++) {
			for (int j = i + 1; j < points.size(); j++) {
				for (int k = j + 1; k < points.size(); k++) {
					Point p = points.get(i);
					Point q = points.get(j);
					Point r = points.get(k);
					// si les trois sont colineaires on passe
					if ((q.x - p.x) * (r.y - p.y) - (q.y - p.y) * (r.x - p.x) == 0)
						continue;
					// si p et q sont sur la meme ligne, ou p et r sont sur la meme ligne, on les
					// echange
					if ((p.y == q.y) || (p.y == r.y)) {
						if (p.y == q.y) {
							p = points.get(k); // ici on est certain que p n'est sur la meme ligne de ni q ni r
							r = points.get(i); // parce que les trois points sont non-colineaires
						} else {
							p = points.get(j); // ici on est certain que p n'est sur la meme ligne de ni q ni r
							q = points.get(i); // parce que les trois points sont non-colineaires
						}
					}
					// on cherche les coordonnees du cercle circonscrit du triangle pqr
					// soit m=(p+q)/2 et n=(p+r)/2
					double mX = .5 * (p.x + q.x);
					double mY = .5 * (p.y + q.y);
					double nX = .5 * (p.x + r.x);
					double nY = .5 * (p.y + r.y);
					// soit y=alpha1*x+beta1 l'equation de la droite passant par m et
					// perpendiculaire a la droite (pq)
					// soit y=alpha2*x+beta2 l'equation de la droite passant par n et
					// perpendiculaire a la droite (pr)
					double alpha1 = (q.x - p.x) / (double) (p.y - q.y);
					double beta1 = mY - alpha1 * mX;
					double alpha2 = (r.x - p.x) / (double) (p.y - r.y);
					double beta2 = nY - alpha2 * nX;
					// le centre c du cercle est alors le point d'intersection des deux droites
					// ci-dessus
					cX = (beta2 - beta1) / (double) (alpha1 - alpha2);
					cY = alpha1 * cX + beta1;
					cRadiusSquared = distance(p, q);
					if (cRadiusSquared >= resRadiusSquared)
						continue;
					boolean allHit = true;
					for (Point s : points) {
						dx = (s.x - cX);
						dy = (s.y - cY);
						if (dx * dx + dy * dy > cRadiusSquared) {
							allHit = false;
							break;
						}
					}
					if (allHit) {
//						System.out.println("Found r=" + Math.sqrt(cRadiusSquared));
						resX = cX;
						resY = cY;
						resRadiusSquared = cRadiusSquared;
					}
				}
			}
		}
//		System.out.println("calcul du cercle minimum terminer");
		return new Circle(new Point((int) resX, (int) resY), (int) Math.sqrt(resRadiusSquared));
	}

	// CalculCercleMinAlgoWelzl : ArrayList<Point> --> Circle
	// implémentation de l'algorithme Welzl qui renvoie le circle couverant tout
	// point de la liste, de rayon minimum
	


	public static Circle CalculCercleMinAlgoWelzl(ArrayList<Point> inputPoints) {
//		System.out.println("commencement du welzel");
		return minidisk(inputPoints);
	}
	
//	j'ai utilisé cette écriture pour éviter d'avoir deux appels récursive qui cause un débordement de la pile
//	function procedure MINIDISK(P); comment: returns md(P) 
//	return B_MINIDTSK(P, O) ;  
	public static Circle minidisk(ArrayList<Point> inputPoints) {
		return b_minidisk(inputPoints, new ArrayList<Point>());
	}
	
	public static Circle b_minidisk(ArrayList<Point> P, ArrayList<Point> R) { // P (ensemble de points à englober) et R (points à fixer sur la frontière).
		ArrayList<Point> points = (ArrayList<Point>) P.clone(); // Crée une copie de l'ensemble des points pour éviter de modifier la liste d'origine lors de l'exécution
		 // Déclare une variable D de type Circle pour stocker le plus petit disque englobant.
		Circle D = null;
		if (P.size() < 1 || R.size() == 3) {
//			Si la liste P est vide (tous les points sont traités) ou si R contient 3 points (un cercle est déterminé par au plus 3 points)
//			on passe à la construction finale du disque avec b_md.
			D = b_md(new ArrayList<Point>(), R);
		} else {
			Random rand = new Random();
			Point p = P.get(rand.nextInt(points.size()));
			points.remove(p); 
			D = b_minidisk(points, R); //  Calculer le plus petit disque englobant sans tenir compte du point p pour l'instant ( p est pris aléatoirement)
			if (D != null && (p.distance(D.getCenter()) > D.getRadius())) { 
				// Si le point p est en dehors du disque actuel, il faut le prendre en compte pour ajuster le disque ( si il est dedans il est déjà retiré et ne sera jamais pris en compte)
				R.add(p);
				D = b_minidisk(points, R); // Ajuster le disque pour forcer l'inclusion de p en le plaçant sur la frontière, puis revenir à l'état initial après l'ajustement
				R.remove(p);
			}
		}
		return D;
	}

	public static Circle b_md(ArrayList<Point> P, ArrayList<Point> R) {
		Circle D = null; // stocker le plus petit cercle, au début c'est à null pour gérer ça : "if p=0 then D:=0; "
		if (P.size() < 1 && R.size() == 0) {
			D = new Circle(new Point(0, 0), 0); // Créer un cercle arbitraire (centre (0, 0) et rayon 0) pour gérer le cas trivial où il n’y a rien à englober " cas de secours"
		}
		switch (R.size()) {
		case 1: {
			D = new Circle(R.get(0), 0); // Créer un cercle minimal lorsque un seul point est à englober, ce cercle est juste le point lui-même.
			break;
		}
		case 2: {
			// Construire un cercle minimal passant exactement par deux points sur la frontière.

			double cX = .5 * (R.get(0).x + R.get(1).x); // utiliser pour optimiser le calcule
			double cY = .5 * (R.get(0).y + R.get(1).y);
			Point centre = new Point((int) cX, (int) cY);
			double cRadiusSquared = .25 * distance(R.get(0), R.get(1)); 
			D = new Circle(centre, (int) Math.ceil(Math.sqrt(cRadiusSquared)));
			break;
		}
		case 3: {
//			si y a 3 points on cherche sur le cercle circonscrit au triangle formé par ces trois points.
			D = circleDuTriangle(R.get(0), R.get(1), R.get(2));
			break;
		}
		}
		return D;
	}

	public static Circle circleDuTriangle(Point p, Point q, Point s) {
		// Calcul du déterminant pour vérifier l'alignement
		double D = 2 * ((p.x * (q.y - s.y)) + (q.x * (s.y - p.y)) + (s.x * (p.y - q.y)));

		if (D == 0) {
			return null; // Il est impossible de définir un cercle circonscrit unique car les points sont aligné 
		}

		// Calcul des coordonnées du centre du cercle circonscrit
		double px2py2 = p.x * p.x + p.y * p.y; // fait pour optimiser le calcul
		double qx2qy2 = q.x * q.x + q.y * q.y;
		double sx2sy2 = s.x * s.x + s.y * s.y;

		double ux = (px2py2 * (q.y - s.y) + qx2qy2 * (s.y - p.y) + sx2sy2 * (p.y - q.y)) / D;
		double uy = (px2py2 * (s.x - q.x) + qx2qy2 * (p.x - s.x) + sx2sy2 * (q.x - p.x)) / D;

		Point centre = new Point((int) ux, (int) uy);

		// Calcul du rayon (distance entre le centre et un des sommets exemple 'p' ici)
		int radius = (int) Math.ceil(centre.distance(p));
		return new Circle(centre, radius);
	}

}