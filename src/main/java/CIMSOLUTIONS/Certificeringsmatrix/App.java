package CIMSOLUTIONS.Certificeringsmatrix;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.GenomeLoader;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.CertificeringsMatrix;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

public class App {
	// Parts of the application are initialized from here
	public static void main(String[] args) {

		ApplicationDriver appDriver = ApplicationDriver.getInstance();
		System.out.println("Loading and reading files");
		appDriver.loadAndReadFiles();
		
		System.out.println("Performing the TF-IDF Algorithm");
		appDriver.performTFIDF();

		System.out.println("Performing Hierarchical Clustering");
		appDriver.performHC();

		System.out.println("Finding additional similar Biased words based on the given Biased words");
		appDriver.findSimilarBiasedWords();

		System.out.println("Performing the NEAT algorithm");
		GenomeLoader genomeLoader = new GenomeLoader();
		Genome loadedGenome = null;
		try {
			loadedGenome = genomeLoader.loadBestPerformingGenome("Best Performing Genome.ser");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		Genome bestPerformingGenome = appDriver.trainExistingNeuralNetwork(loadedGenome);
		Genome bestPerformingGenome = appDriver.trainNewNeuralNetworks();

		try {
			appDriver.exportGenome(bestPerformingGenome, "Best Performing Genome.ser");
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Combining competences with roles using the best performing genome");
		CertificeringsMatrix certificeringsMatrix = appDriver.combineCompetencesWithRoles(bestPerformingGenome, 30,
				0.5);

		System.out.println("Exporting matrix");
		appDriver.exportMatrix(certificeringsMatrix);

	}

	private static void writeTop5Results(List<Document> allDocuments) {
		// Print the top 5 words from all documents
		String filename = "Top 5 words per document.txt";
		try (FileWriter writer = new FileWriter(filename)) {
			for (Document docu : allDocuments) {
				int i = 0;
				writer.write("New Document: " + docu.getDocumentName() + "\n");
				for (Map.Entry<String, Double> entry : docu.getSortedIFTDFScores().entrySet()) {
					if (entry.getValue() != 0.0) {
						if (i < 5) {
							writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
							i++;
						} else {
							writer.write("\n");
							break;
						}
					}
				}
			}
			System.out.println("Wrote to: Top 5 words per document.txt");

		} catch (IOException e) {
			System.out.println("An error occurred while writing to " + filename);
			e.printStackTrace();
		}

	}
}
