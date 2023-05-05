package CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.HierarchicalClustering.WordVectorMatrix;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.StorageManager;

/*- This class is responsible for loading all words that need to recieve bonus points in the adjustment of the TF-IDF scores
 *  of the NEAT algorithm's task. Words that are loaded here will recieve a higher score through the NEAT algorithm
 */
public class BiasedWordsLoader {

	private static BiasedWordsLoader instance = new BiasedWordsLoader();
	
	private List<String> originalBiasedWords = new ArrayList<String>();

	private BiasedWordsLoader() {

	}
	
	public static BiasedWordsLoader getInstance() {
		return instance;
	}

	// Loads and reads all given biased words
	public void loadAndReadBiasedWords() {
		originalBiasedWords = new ArrayList<String>();
		String filePath = "resources/Bias/BiasedWords.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] words = line.split("\n");
				for (String word : words) {
					String cleanedWord = word.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();
					if (!originalBiasedWords.contains(cleanedWord)) {
						this.originalBiasedWords.add(cleanedWord);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getOriginalBiasedWords(){
		return this.originalBiasedWords;
	}
}
