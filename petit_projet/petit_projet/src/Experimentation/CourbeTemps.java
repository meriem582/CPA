package Experimentation;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.io.*;

public class CourbeTemps extends JFrame {

    private static final long serialVersionUID = -2163531690191013060L;

    public CourbeTemps(String titre, String fichierResultat) {
        super(titre);

        // Créer le graphique
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Comparaison des Temps d'Exécution",
                "Fichiers .points",
                "Temps (µs)",
                creerDataset(fichierResultat),
                PlotOrientation.VERTICAL,
                true, true, false);

        // Ajouter le graphique à un panneau
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private CategoryDataset creerDataset(String outputFile) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Lire les résultats depuis le fichier de sortie
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

                    // Ajouter les points au dataset
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
        if (args.length < 1) {
            System.err.println("Usage : java Experimentation.CourbeTemps <fichier_resultat>");
            System.exit(1);
        }

        String fichierResultat = args[0];

        SwingUtilities.invokeLater(() -> {
        	CourbeTemps exemple = new CourbeTemps("Comparaison Algo Naïf vs Welzl", fichierResultat);
            exemple.setSize(1500, 800);
            exemple.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            exemple.setVisible(true);
        });
    }
}
