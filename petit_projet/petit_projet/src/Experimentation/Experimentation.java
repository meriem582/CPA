package Experimentation;

import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


public class Experimentation {
	
	public static double distance(Point a, Point b) {
		double tmp1 = a.getX() - b.getX();
		double tmp2 = a.getY() - b.getY();
		return tmp1 * tmp1 + tmp2 * tmp2;
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

	// pour que l'implémention sois idéntique à celle du PDF
	// minidisk renvoie le Circle couverant de rayon minimum
	public static Circle minidisk(ArrayList<Point> inputPoints) {
		return b_minidisk(inputPoints, new ArrayList<Point>());
	}

	public static Circle b_minidisk(ArrayList<Point> P, ArrayList<Point> R) {
		ArrayList<Point> points = (ArrayList<Point>) P.clone();
		Circle D = null;
		if (P.size() < 1 || R.size() == 3) {
			D = b_md(new ArrayList<Point>(), R);
		} else {
			Random rand = new Random();
			Point p = P.get(rand.nextInt(points.size()));
			points.remove(p);
			D = b_minidisk(points, R);
			if (D != null && (p.distance(D.getCenter()) > D.getRadius())) {
				R.add(p);
				D = b_minidisk(points, R);
				R.remove(p);
			}
		}
		return D;
	}

	public static Circle b_md(ArrayList<Point> P, ArrayList<Point> R) {
		Circle D = null;
		if (P.size() < 1 && R.size() == 0) {
			D = new Circle(new Point(0, 0), 0);
		}
		switch (R.size()) {
		case 1: {
			D = new Circle(R.get(0), 0);
			break;
		}
		case 2: {
			double cX = .5 * (R.get(0).x + R.get(1).x);
			double cY = .5 * (R.get(0).y + R.get(1).y);
			Point centre = new Point((int) cX, (int) cY);
			double cRadiusSquared = .25 * distance(R.get(0), R.get(1));
			D = new Circle(centre, (int) Math.ceil(Math.sqrt(cRadiusSquared)));
			break;
		}
		case 3: {
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
			return null;
		}

		// Calcul des coordonnées du centre du cercle circonscrit
		double px2py2 = p.x * p.x + p.y * p.y;
		double qx2qy2 = q.x * q.x + q.y * q.y;
		double sx2sy2 = s.x * s.x + s.y * s.y;

		double ux = (px2py2 * (q.y - s.y) + qx2qy2 * (s.y - p.y) + sx2sy2 * (p.y - q.y)) / D;
		double uy = (px2py2 * (s.x - q.x) + qx2qy2 * (p.x - s.x) + sx2sy2 * (q.x - p.x)) / D;

		Point centre = new Point((int) ux, (int) uy);

		// Calcul du rayon (distance entre le centre et un des sommets)
		int radius = (int) Math.ceil(centre.distance(p));
		return new Circle(centre, radius);
	}

	public static void main(String[] args) {
		// Dossier contenant les fichiers .points
		String inputDirectory = "./samples";
		String inputDirectoryDiff = "./samplesDiff";

		// Fichier de sortie pour enregistrer les résultats
		String outputFile = "./resultats_experimentation.txt";
		String outputFileDiff = "./resultats_experimentationDiff.txt";

		// Liste pour stocker tous les résultats
		List<String> results = new ArrayList<>();
		List<String> resultsDiff = new ArrayList<>();

		// Parcourir tous les fichiers du dossier
		File folder = new File(inputDirectory);
		if (!folder.exists() || !folder.isDirectory()) {
			System.err.println("Dossier introuvable : " + inputDirectory);
			return;
		}
		
		File folderDiff = new File(inputDirectoryDiff);
		if (!folderDiff.exists() || !folderDiff.isDirectory()) {
			System.err.println("Dossier introuvable : " + inputDirectoryDiff);
			return;
		}

		File[] files = folder.listFiles((dir, name) -> name.endsWith(".points"));
		if (files == null || files.length == 0) {
			System.err.println("Aucun fichier .points trouvé dans : " + inputDirectory);
			return;
		}
		File[] filesDiff = folderDiff.listFiles((dir, name) -> name.endsWith(".points"));
		if (filesDiff == null || filesDiff.length == 0) {
			System.err.println("Aucun fichier .points trouvé dans : " + inputDirectoryDiff);
			return;
		}

		// Traiter chaque fichier .points
		for (File file : files) {
			ArrayList<Point> points = lirePointsDepuisFichier(file);

			if (points.isEmpty()) {
				System.err.println("Fichier vide ou incorrect : " + file.getName());
				continue;
			}

			// Mesurer le temps pour AlgoNaif
			long debutAlgoNaif = System.nanoTime();
			calculCercleMinAlgoNaif(points);
			long tempsNaif = (System.nanoTime() - debutAlgoNaif) / 1000;

			// Mesurer le temps pour AlgoWelzl
			long debutAlgoWelzl = System.nanoTime();
			CalculCercleMinAlgoWelzl(points);
			long tempsWelzl = (System.nanoTime() - debutAlgoWelzl) / 1000;

			// Préparer le résultat
			String resultat = String.format("%s, %d, %d", file.getName(), tempsNaif, tempsWelzl);
			results.add(resultat);
			System.out.println(resultat);
		}

		// Traiter chaque fichier .points
		for (File file : filesDiff) {
			ArrayList<Point> pointsDiff = lirePointsDepuisFichier(file);

			if (pointsDiff.isEmpty()) {
				System.err.println("Fichier vide ou incorrect : " + file.getName());
				continue;
			}

			// Mesurer le temps pour AlgoNaif
			long debutAlgoNaif = System.nanoTime();
			calculCercleMinAlgoNaif(pointsDiff);
			long tempsNaif = (System.nanoTime() - debutAlgoNaif) / 1000;

			// Mesurer le temps pour AlgoWelzl
			long debutAlgoWelzl = System.nanoTime();
			CalculCercleMinAlgoWelzl(pointsDiff);
			long tempsWelzl = (System.nanoTime() - debutAlgoWelzl) / 1000;

			// Préparer le résultat
			String resultatDiff = String.format("%s, %d, %d", file.getName(), tempsNaif, tempsWelzl);
			resultsDiff.add(resultatDiff);
			System.out.println(resultatDiff);
		}

		// Écrire tous les résultats dans le fichier de sortie
		ecrireResultatsDansFichier(results, outputFile);
		ecrireResultatsDansFichier(resultsDiff, outputFileDiff);
		System.out.println("Les résultats ont été enregistrés dans : " + outputFile);
	}

	// Méthode pour lire les points depuis un fichier
	private static ArrayList<Point> lirePointsDepuisFichier(File file) {
		ArrayList<Point> points = new ArrayList<>();
		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String[] coordinates = scanner.nextLine().trim().split("\\s+");
				if (coordinates.length == 2) {
					try {
						int x = Integer.parseInt(coordinates[0]);
						int y = Integer.parseInt(coordinates[1]);
						points.add(new Point(x, y));
					} catch (NumberFormatException e) {
						System.err.println("Coordonnées invalides dans : " + file.getName());
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Fichier non trouvé : " + file.getAbsolutePath());
		}
		return points;
	}

	// Méthode pour écrire les résultats dans un fichier
	private static void ecrireResultatsDansFichier(List<String> results, String outputFile) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			writer.write("Fichier, Temps Naif, Temps Welzl");
			writer.newLine();
			for (String result : results) {
				writer.write(result);
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("Erreur lors de l'écriture du fichier : " + outputFile);
		}
	}

}
