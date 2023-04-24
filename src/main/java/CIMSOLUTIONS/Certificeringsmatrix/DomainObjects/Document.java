package CIMSOLUTIONS.Certificeringsmatrix.DomainObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Document {

	private List<String> wordsWithinDocument = new ArrayList<String>();
	private List<String> sentencesWithinDocument = new ArrayList<String>();
	private Map<String, Double> IFTDFScores = new HashMap<String, Double>();
	private LinkedHashMap<String, Double> sortedIFTDFScores = new LinkedHashMap<String, Double>();
	private String documentName;

	private LinkedHashMap<String, Integer> wordCounts = new LinkedHashMap<>();

	public Document(String documentName) {
		this.documentName = documentName;
	}
	
	// ---------------------------------------------------------------------------------------------
	/*- All methods within these borders exist only for the development of the Fitness Function of the NEAT algorithm.
	 *  These methods *may* be removed at the end of development, but may still prove useful
	 */

	public void countWords() {
		for (String word : wordsWithinDocument) {
			// If the word is already in the map, increment its count
			if (wordCounts.containsKey(word)) {
				wordCounts.put(word, wordCounts.get(word) + 1);
			}
			// Otherwise, add the word to the map with a count of 1
			else {
				wordCounts.put(word, 1);
			}
		}

		wordCounts = sortHashMapDescending(wordCounts);
	}

	private LinkedHashMap<String, Integer> sortHashMapDescending(HashMap<String, Integer> map) {
		// Create a list of map entries
		List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());

		// Sort the list in descending order by value
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		// Convert the sorted list back to a LinkedHashMap
		LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	public LinkedHashMap<String, Integer> getWordCount(){
		return wordCounts;
	}

	// ---------------------------------------------------------------------------------------------

	public void addWord(String word) {
		this.wordsWithinDocument.add(word);
	}

	public List<String> getWordsWithinDocument() {
		return wordsWithinDocument;
	}

	public void addSentenceToDocument(String sentence) {
		sentencesWithinDocument.add(sentence);
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setTFIDFScores(Map<String, Double> wordsWithScores) {
		this.IFTDFScores = wordsWithScores;
		sortTFIDFScores();
	}

	public Map<String, Double> getWordsWithScores() {
		return IFTDFScores;
	}

	public LinkedHashMap<String, Double> getSortedIFTDFScores() {
		return this.sortedIFTDFScores;
	}

	public Double getWordScore(String word) {
		return IFTDFScores.get(word);
	}

	private void sortTFIDFScores() {
		sortedIFTDFScores = new LinkedHashMap<String, Double>();
		ArrayList<Double> scoreList = new ArrayList<>();

		for (Map.Entry<String, Double> entry : IFTDFScores.entrySet()) {
			scoreList.add(entry.getValue());
		}

		Collections.sort(scoreList);

		for (Double num : scoreList) {
			for (Map.Entry<String, Double> entry : IFTDFScores.entrySet()) {
				if (entry.getValue().equals(num)) {
					sortedIFTDFScores.put(entry.getKey(), num);
				}
			}
		}
	}

	public List<String> getSentencesWithinDocument() {
		return sentencesWithinDocument;
	}

}
