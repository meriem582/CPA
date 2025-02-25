package Experimentation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.io.*;

public class CourbeTempsDiff extends JFrame {
	private static final long serialVersionUID = -7448017516440617558L;

	public CourbeTempsDiff(String titre) {
		super(titre);

		JFreeChart lineChart = ChartFactory.createLineChart("Comparaison des Temps d'Exécution", "Fichiers .points",
				"Temps (µs)", creerDataset(), PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
		setContentPane(chartPanel);
	}

	private CategoryDataset creerDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		String outputFile = "./Resultat/resultats_experimentationDiff.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(outputFile))) {
			String ligne;
			boolean premiereLigne = true; // Ignorer l'en-tête
			while ((ligne = reader.readLine()) != null) {
				if (premiereLigne) {
					premiereLigne = false;
					continue;
				}
				String[] parties = ligne.split(",");
				if (parties.length == 3) {
					String fichier = parties[0].trim();
					long tempsNaif = Long.parseLong(parties[1].trim());
					long tempsWelzl = Long.parseLong(parties[2].trim());

					dataset.addValue(tempsNaif, "Algo Naïf", fichier);
					dataset.addValue(tempsWelzl, "Algo Welzl", fichier);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Erreur lors de la lecture du fichier : " + outputFile);
		}

		return dataset;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			CourbeTempsDiff exemple = new CourbeTempsDiff("Comparaison Algo Naïf vs Welzl");
			exemple.setSize(1500, 800);
			exemple.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			exemple.setVisible(true);
		});
	}
}