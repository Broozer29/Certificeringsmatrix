package CIMSOLUTIONS.Certificeringsmatrix;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.HierarchicalClustering.WordVectorMatrix;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF.TFIDFDriver;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Document;
import CIMSOLUTIONS.Certificeringsmatrix.Data.DocumentLoadingDriver;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.StorageManager;

public class App {
	public static void main(String[] args) {

		DocumentLoadingDriver loadDriver = DocumentLoadingDriver.getInstance();
		loadDriver.loadDocuments();

		TFIDFDriver iftdfDriver = TFIDFDriver.getInstance();
		iftdfDriver.calculateTFIDF();
//		iftdfDriver.writeResultsToFile();

		StorageManager storageManager = StorageManager.getInstance();
		storageManager.refreshStorageManager();

		WordVectorMatrix matrix = WordVectorMatrix.getInstance();
		matrix.createWordVectorMatrix();
		System.out.println(matrix.getNearestWords("developer", 10));
		System.out.println(matrix.getSimilarityOfMultiTermWords("java developer", "sqlplus"));

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
