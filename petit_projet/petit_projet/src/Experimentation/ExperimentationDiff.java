package Experimentation;

import java.awt.Point;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import algorithms.DefaultTeam;

public class ExperimentationDiff {

	public static void main(String[] args) {
		String inputDirectoryDiff = "./samplesDiff";
		String outputFileDiff = "./Resultat/resultats_experimentationDiff.txt";
		List<String> resultsDiff = new ArrayList<>();

		File folderDiff = new File(inputDirectoryDiff);
		if (!folderDiff.exists() || !folderDiff.isDirectory()) {
			System.err.println("Dossier introuvable : " + inputDirectoryDiff);
			return;
		}

		File[] filesDiff = folderDiff.listFiles((dir, name) -> name.endsWith(".points"));
		if (filesDiff == null || filesDiff.length == 0) {
			System.err.println("Aucun fichier .points trouvé dans : " + inputDirectoryDiff);
			return;
		}

		for (File file : filesDiff) {
			ArrayList<Point> pointsDiff = lirePointsDepuisFichier(file);

			if (pointsDiff.isEmpty()) {
				System.err.println("Fichier vide ou incorrect : " + file.getName());
				continue;
			}

			long debutAlgoNaif = System.nanoTime();
			DefaultTeam.calculCercleMinAlgoNaif(pointsDiff);
			long tempsNaif = (System.nanoTime() - debutAlgoNaif) / 1000;

			long debutAlgoWelzl = System.nanoTime();
			DefaultTeam.CalculCercleMinAlgoWelzl(pointsDiff);
			long tempsWelzl = (System.nanoTime() - debutAlgoWelzl) / 1000;

			String resultatDiff = String.format("%s, %d, %d", file.getName(), tempsNaif, tempsWelzl);
			resultsDiff.add(resultatDiff);
			System.out.println(resultatDiff);
		}

		ecrireResultatsDansFichier(resultsDiff, outputFileDiff);
		System.out.println("Les résultats ont été enregistrés dans : " + outputFileDiff);
	}

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
