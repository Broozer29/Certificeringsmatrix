package CIMSOLUTIONS.Certificeringsmatrix.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Document {

	private List<String> wordsWithinDocument = new ArrayList<String>();
	private List<String> sentencesWithinDocument = new ArrayList<String>();
	private Map<String, Double> IFTDFScores = new HashMap<String, Double>();
	private LinkedHashMap<String, Double> sortedIFTDFScores = new LinkedHashMap<String, Double>();
	private String documentName;

	public Document(String documentName) {
		this.documentName = documentName;
	}

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
