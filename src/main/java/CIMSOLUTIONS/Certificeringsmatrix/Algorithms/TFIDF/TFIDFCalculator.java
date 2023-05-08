package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Competence;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;

/*- This class is responsible for the TF-IDF calculations
 *  It only contains calculations which are called by other classes
 */
public class TFIDFCalculator {

	public TFIDFCalculator() {

	}

	//Creates a sorted HashMap of the given HashMap
	public LinkedHashMap<String, Double> createSortedTFIDFScores(Map<String, Double> averageTFIDFScores) {
		ArrayList<Double> scoreList = new ArrayList<>();
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();

		for (Map.Entry<String, Double> entry : averageTFIDFScores.entrySet()) {
			scoreList.add(entry.getValue());
		}

		//Sort the list in reverse, so the highest scoring words are on top.
		Collections.sort(scoreList, Collections.reverseOrder());

		for (Double num : scoreList) {
			for (Map.Entry<String, Double> entry : averageTFIDFScores.entrySet()) {
				if (entry.getValue().equals(num)) {
					sortedMap.put(entry.getKey(), num);
				}
			}
		}

		return sortedMap;
	}

	// Calculates the TF-IDF score of all words contained in the given Documents.
	public Map<Document, Map<String, Double>> calculateTFIDF(List<Document> documents) {
		Map<Document, Map<String, Double>> documentsWithCalculatedTF = new HashMap<>();
		Map<String, Integer> amountOfDocumentsContainingWord = new HashMap<>();

		// Calculate the TF and DF scores of a word for each documet
		for (Document document : documents) {
			List<String> documentWords = document.getWordsWithinDocument();
			Map<String, Double> wordCount = new HashMap<>();
			Map<String, Double> TFMap = new HashMap<>();

			// Count the amount of times the word is encountered within a single document
			for (String word : documentWords) {
				Double currentWordCount = wordCount.getOrDefault(word, 0.0);
				wordCount.put(word, currentWordCount + 1);
			}

			// Count the amount documents that contain the word
			for (String word : wordCount.keySet()) {
				Integer currentDocumentCount = amountOfDocumentsContainingWord.getOrDefault(word, 0);
				amountOfDocumentsContainingWord.put(word, currentDocumentCount + 1);
			}

			/*-
			 * Calculates the TF (Term Frequency) score by the following method: 
			 * TF = (Number of times word t appears in a document) / (Total number of words in the document) 
			 * The TFscore overwrites the regular Frequency of the word
			 */
			for (String word : wordCount.keySet()) {
				Double TFscore = wordCount.get(word) / documentWords.size();
				TFMap.put(word, TFscore);
			}

			// Save the document and all containing words with corresponding TF score.
			documentsWithCalculatedTF.put(document, TFMap);
		}

		// Calculate the TF-IDF score of a word using the TF and DF scores
		for (Document document : documentsWithCalculatedTF.keySet()) {
			Map<String, Double> wordScoreDict = documentsWithCalculatedTF.get(document);

			/*-
			 * Calculates the TF-IDF score by the following method: 
			 * IDF = Log(total number of documents in StorageManager / number of documents in which the word appears) 
			 * TF-IDF = TF * IDF 
			 * Then overwrite the calculated TF score with the newer TF-IDF score of the word
			 */
			for (String term : wordScoreDict.keySet()) {
				double IDF = Math.log10((double) (documents.size() / amountOfDocumentsContainingWord.get(term)));
				double TFIDF = wordScoreDict.get(term) * IDF;
				wordScoreDict.put(term, TFIDF);
			}
		}

		return documentsWithCalculatedTF;
	}

	/*-
	 * Calculates the average TF-IDF score of a word across all documents I.E. 
	 * How important is the word "Hibernate" across ALL documents combined, instead of it's importance within a single document
	 */
	public Map<String, Double> calculateAverageTFIDF(Map<Document, Map<String, Double>> TFIDFScoresByDocument,
			int numDocuments) {

		/*-
		 * Iterate over every Document
		 * Iterate over every entry from the Document (Entries are "word" and "score")
		 * Add the TF-IDF score of the word from that document to the total TF-IDF score of that word.
		 */
		Map<String, Double> totalTFIDFScores = new HashMap<>();

		for (Map<String, Double> document : TFIDFScoresByDocument.values()) {
			for (Map.Entry<String, Double> entry : document.entrySet()) {
				String word = entry.getKey();
				double TFIDF = entry.getValue();
				double oldTotalTFIDF = totalTFIDFScores.getOrDefault(word, 0.0);
				totalTFIDFScores.put(word, oldTotalTFIDF + TFIDF);
			}
		}

		/*-
		 * Divide the total score of a word by the total amount of documents in StorageManager
		 */
		Map<String, Double> averageTFIDFScores = new HashMap<>();
		for (Map.Entry<String, Double> entry : totalTFIDFScores.entrySet()) {
			String word = entry.getKey();
			double averageTFIDF = entry.getValue() / numDocuments;
			averageTFIDFScores.put(word, averageTFIDF);
		}

		return averageTFIDFScores;
	}
	

}
