package CIMSOLUTIONS.Certificeringsmatrix;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.HierarchicalClustering.WordVectorMatrix;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF.TFIDFBiasAdjuster;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF.TFIDFDriver;
import CIMSOLUTIONS.Certificeringsmatrix.Data.DocumentLoadingDriver;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.BiasedWordsLoader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.RoleLoader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.StorageManager;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Competence;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Role;

public class ApplicationDriver {
	private TFIDFBiasAdjuster biasAdjuster = null;
	private DocumentLoadingDriver loadDriver = null;
	private StorageManager storageManager = null;
	private TFIDFDriver tfidfDriver = null;
	private WordVectorMatrix matrix = null;
	private RoleLoader roleLoader = null;
	private BiasedWordsLoader biasLoader = null;

	private static ApplicationDriver instance = new ApplicationDriver();

	private ApplicationDriver() {

	}

	public static ApplicationDriver getInstance() {
		return instance;
	}

	public void loadAndReadFiles() {
		biasLoader = BiasedWordsLoader.getInstance();
		biasLoader.loadBiasedWords();
		
		biasAdjuster = TFIDFBiasAdjuster.getInstance();
		biasAdjuster.initializeBiasedWords();
		

		loadDriver = DocumentLoadingDriver.getInstance();
		loadDriver.loadDocuments();

		storageManager = StorageManager.getInstance();
		storageManager.refreshStorageManager();

		roleLoader = RoleLoader.getInstance();
		roleLoader.loadRoles();

		exportWordCounts();

	}

	public void performTFIDF() {
		tfidfDriver = TFIDFDriver.getInstance();
		tfidfDriver.calculateTFIDF();
	}

	public void performHC() {
		matrix = WordVectorMatrix.getInstance();
		matrix.createWordVectorMatrix();
	}

	public void combineCompetencesWithRoles() {
		List<Competence> competences = tfidfDriver.getCompetences();
		List<Role> roles = roleLoader.getRoles();
		matrix.addCompetencesToRoles(competences, roles);
	}

	public void generateMatrix() {
		String fileName = "Roles with competences.txt";
		try (FileWriter writer = new FileWriter(fileName)) {
			for (Role role : roleLoader.getRoles()) {
				writer.write("Role: " + role.getRole() + "\n");
				for (Competence competence : role.getCompetences()) {
					writer.write("Competence: " + competence.getCompetence() + "\n");
				}
				writer.write("\n");
			}
			System.out.println("Exported a matrix");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ---------------------------------------------------------------------------------------------
	/*- All methods within these borders exist only for the development of the Fitness Function of the NEAT algorithm.
	 *  These methods *may* be removed at the end of development, but may still prove useful
	 */
	public void exportWordCounts() {
		List<LinkedHashMap<String, Integer>> wordCountList = new ArrayList<>();
		for (Document docu : storageManager.getAllDocuments()) {
			docu.countWords();
			wordCountList.add(docu.getWordCount());
		}

		HashMap<String, Integer> combinedWordCounts = combineWordCountMaps(wordCountList);
		exportWordCountsToFile(combinedWordCounts);
	}

	private LinkedHashMap<String, Integer> combineWordCountMaps(List<LinkedHashMap<String, Integer>> wordCountList) {
		// Create a new LinkedHashMap to store the combined word counts
		LinkedHashMap<String, Integer> combinedWordCounts = new LinkedHashMap<>();

		// Loop through each word count map in the list
		for (LinkedHashMap<String, Integer> wordCounts : wordCountList) {
			// Loop through each word in the map
			for (String word : wordCounts.keySet()) {
				int count = wordCounts.get(word);
				// If the word is already in the combined map, add the counts together
				if (combinedWordCounts.containsKey(word)) {
					combinedWordCounts.put(word, combinedWordCounts.get(word) + count);
				}
				// Otherwise, add the word to the combined map with its count
				else {
					combinedWordCounts.put(word, count);
				}
			}
		}

		// Sort the combined word counts in descending order by value
		List<Map.Entry<String, Integer>> list = new LinkedList<>(combinedWordCounts.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		LinkedHashMap<String, Integer> sortedCombinedWordCounts = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> entry : list) {
			sortedCombinedWordCounts.put(entry.getKey(), entry.getValue());
		}

		return sortedCombinedWordCounts;
	}

	private void exportWordCountsToFile(HashMap<String, Integer> wordCounts) {
		try {
			// Create a new FileWriter object to write to the file
			FileWriter writer = new FileWriter("temp.txt");

			// Loop through each word count in the map and write it to the file
			for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
				String line = entry.getKey() + ": " + entry.getValue() + "\n";
				writer.write(line);
			}

			// Close the FileWriter object
			writer.close();

			System.out.println("Word counts exported to file: " + "temp.txt");
		} catch (IOException e) {
			System.out.println("An error occurred while exporting the word counts to file: " + e.getMessage());
		}
	}

	// ---------------------------------------------------------------------------------------------

}
