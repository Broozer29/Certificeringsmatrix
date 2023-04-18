package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.BiasedWordsLoader;

public class TFIDFBiasAdjuster {

	private static TFIDFBiasAdjuster instance = new TFIDFBiasAdjuster();
	private List<String> biasedWords = new ArrayList<String>();
	private Map<String, Double> wordsWithBiasBonus = new HashMap<String, Double>();

	private TFIDFBiasAdjuster() {

	}
	
	// This class has all words that are biased, it now requires NEAT to tell it how large
	// the bias is.
	public void setBiasForWord(String word, Double bias) {
		
	}

	public static TFIDFBiasAdjuster getInstance() {
		return instance;
	}

	public void initializeBiasedWords() {
		BiasedWordsLoader loader = BiasedWordsLoader.getInstance();
		biasedWords = loader.getBiasedWords();
	}

	// Return the bias of a word or 0 if the word isn't biased.
	public double getBiasByWord(String word) {
		if (wordsWithBiasBonus.containsKey(word)) {
			return wordsWithBiasBonus.get(word);
		} else
			return 0.0;
	}

}
