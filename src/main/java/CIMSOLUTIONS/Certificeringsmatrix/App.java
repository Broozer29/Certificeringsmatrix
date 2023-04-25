package CIMSOLUTIONS.Certificeringsmatrix;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.NEAT;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

public class App {
	public static void main(String[] args) {

		ApplicationDriver appDriver = ApplicationDriver.getInstance();
		System.out.println("Loading and reading files");
		appDriver.loadAndReadFiles();
		System.out.println("Performing the TF-IDF Algorithm");
		appDriver.performTFIDF();
//		appDriver.performHC();
//		appDriver.combineCompetencesWithRoles();
//		appDriver.generateMatrix();
		NEAT neatInstance = NEAT.getInstance();
		System.out.println("Performing the NEAT algorithm");
		neatInstance.executeNeatAlgorithm();

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
