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

/*- This class is responsible for steering the TF-IDF process
 *  The individual components of the TF-IDF algorithm are called from here
 */
public class TFIDFDriver {

	private StorageManager storageManager;
	private LinkedHashMap<String, Double> sortedAverageTFIDFScores;
	private Map<Document, Map<String, Double>> TFIDFScoresPerDocument;
	private Map<String, Double> averageTFIDFScores;
	private TFIDFCalculator tfidfCalculator;

	public TFIDFDriver() {
		storageManager = StorageManager.getInstance();
		sortedAverageTFIDFScores = new LinkedHashMap<String, Double>();
		TFIDFScoresPerDocument = new HashMap<Document, Map<String, Double>>();
		 averageTFIDFScores = new HashMap<String, Double>();
		 tfidfCalculator = new TFIDFCalculator();
	}

	public void calculateTFIDF() {
		List<Document> allDocuments = storageManager.getAllDocuments();

		System.out.println("   > Calculating TF-IDF scores of words within all documents");
		TFIDFScoresPerDocument = tfidfCalculator.calculateTFIDF(allDocuments);
		saveTFIDFScoresToDocuments(TFIDFScoresPerDocument);

		System.out.println("   > Calculating the average TF-IDF score of each word");
		averageTFIDFScores = tfidfCalculator.calculateAverageTFIDF(TFIDFScoresPerDocument, allDocuments.size());
		sortedAverageTFIDFScores = tfidfCalculator.createSortedTFIDFScores(averageTFIDFScores);
		
		System.out.println("   > Adding words & their average TF-IDF scores to the StorageManager in sorted order (Descending)");
		storageManager.setWordScores(sortedAverageTFIDFScores);
		
		System.out.println("   > Writing the words & their IF-TDF scores to a text file");
		writeResultsToFile();
	}

	public void writeResultsToFile() {
		// Export the sorted list, moet uit deze klasse en in de driver
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

}
