package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.StorageManager;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Competence;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

public class TFIDFDriver {

	private static TFIDFDriver instance = new TFIDFDriver();
	private StorageManager storageManager = StorageManager.getInstance();
	private LinkedHashMap<String, Double> sortedAverageTFIDFScores = new LinkedHashMap<String, Double>();
	private Map<Document, Map<String, Double>> TFIDFScoresPerDocument = new HashMap<Document, Map<String, Double>>();
	private Map<String, Double> averageTFIDFScores = new HashMap<String, Double>();
	private List<Competence> allCompetences = new ArrayList<Competence>();
	
	private TFIDFDriver() {
	}

	public static TFIDFDriver getInstance() {
		return instance;
	}

	public void calculateTFIDF() {
		TFIDFCalculator tfidfCalculator = new TFIDFCalculator();

		storageManager.refreshStorageManager();
		List<Document> allDocuments = storageManager.getAllDocuments();

		TFIDFScoresPerDocument = tfidfCalculator.calculateTFIDF(allDocuments);
		averageTFIDFScores = tfidfCalculator.calculateAverageTFIDF(TFIDFScoresPerDocument, allDocuments.size());

		saveTFIDFScoresToDocuments(TFIDFScoresPerDocument);
		sortedAverageTFIDFScores = tfidfCalculator.createSortedTFIDFScores(averageTFIDFScores);
		
		allCompetences = tfidfCalculator.createCompetences(sortedAverageTFIDFScores);
	}
	
	public void writeResultsToFile() {
		//Export the sorted list, moet uit deze klasse en in de driver
		try (FileWriter writer = new FileWriter("Average score per word.txt")) {
			for (Map.Entry<String, Double> entry : sortedAverageTFIDFScores.entrySet()) {
				writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
			}
			System.out.println("Wrote to: Average score per word.txt");
		} catch (IOException e) {
			System.out.println("Couldn't write the average score of words to Average score per word.txt");
			e.printStackTrace();
		}
	}
	
	// Iterate over each Document and save the TF-IDF scores to the Document
	public void saveTFIDFScoresToDocuments(Map<Document, Map<String, Double>> TFIDFScoresByDocument) {
		for (Document document : TFIDFScoresByDocument.keySet()) {
			Map<String, Double> TFIDFScores = TFIDFScoresByDocument.get(document);
			document.setTFIDFScores(TFIDFScores);
		}
	}

	public LinkedHashMap<String, Double> getSortedAverageTFIDFScores() {
		return sortedAverageTFIDFScores;
	}

	public Map<Document, Map<String, Double>> getTFIDFScoresByDocument() {
		return TFIDFScoresPerDocument;
	}

	public Map<String, Double> getAverageTFIDFScores() {
		return averageTFIDFScores;
	}
	
	public List<Competence> getCompetences(){
		return this.allCompetences;
	}

}
