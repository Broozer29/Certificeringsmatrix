package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Document;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.StorageManager;

public class TFIDFDriver {

	private static TFIDFDriver instance = new TFIDFDriver();
	private StorageManager storageManager = StorageManager.getInstance();
	private LinkedHashMap<String, Double> sortedTFIDFScores = new LinkedHashMap<String, Double>();
	private Map<Document, Map<String, Double>> TFIDFScoresByDocument = new HashMap<Document, Map<String, Double>>();
	private Map<String, Double> averageTFIDFScores = new HashMap<String, Double>();

	private TFIDFDriver() {
	}

	public static TFIDFDriver getInstance() {
		return instance;
	}

	public void calculateTFIDF() {
		TFIDFCalculator tfidfCalculator = new TFIDFCalculator();

		storageManager.refreshStorageManager();
		List<Document> allDocuments = storageManager.getAllDocuments();

		TFIDFScoresByDocument = tfidfCalculator.calculateTFIDF(allDocuments);
		averageTFIDFScores = tfidfCalculator.calculateAverageTFIDF(TFIDFScoresByDocument, allDocuments.size());

		tfidfCalculator.saveTFIDFScoresToDocuments(TFIDFScoresByDocument);
		sortedTFIDFScores = tfidfCalculator.createSortedTFIDFScores(averageTFIDFScores);
	}
	
	public void writeResultsToFile() {
		//Export the ssorted list, moet uit deze klasse en in de driver
		try (FileWriter writer = new FileWriter("Average score per word.txt")) {
			for (Map.Entry<String, Double> entry : averageTFIDFScores.entrySet()) {
				writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
			}
			System.out.println("Wrote to: Average score per word.txt");
		} catch (IOException e) {
			System.out.println("Couldn't write the average score of words to Average score per word.txt");
			e.printStackTrace();
		}
	}

	public LinkedHashMap<String, Double> getSortedTFIDFScores() {
		return sortedTFIDFScores;
	}

	public Map<Document, Map<String, Double>> getTFIDFScoresByDocument() {
		return TFIDFScoresByDocument;
	}

	public Map<String, Double> getAverageTFIDFScores() {
		return averageTFIDFScores;
	}

}
