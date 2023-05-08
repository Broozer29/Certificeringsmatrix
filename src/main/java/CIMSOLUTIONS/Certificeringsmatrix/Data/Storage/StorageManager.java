package CIMSOLUTIONS.Certificeringsmatrix.Data.Storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.HierarchicalClustering.WordVectorMatrix;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.BiasedWordsLoader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.RoleLoader;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Role;

/*- This class is responsible for keeping track of the contents of ALL storages.
 *  Unless you want a single type of Document, use this class as access point for the contents of storages
 */
public class StorageManager {

	private static StorageManager instance = new StorageManager();
	private BiasedWordsLoader biasLoader;

	private LinkedHashMap<String, Double> avgWordScores = new LinkedHashMap<String, Double>();
	private List<String> allBiasedWords = new ArrayList<String>();
	private List<Document> allDocuments = new ArrayList<Document>();
	private List<String> allUniqueWords = new ArrayList<String>();
	private List<Role> allRoles = new ArrayList<Role>();

	private StorageManager() {
	}

	public static StorageManager getInstance() {
		return instance;
	}

	// When a storage has loaded files, this function needs to be called
	// to synchronize the manager with the storages
	public void populateStorageManager() {
		biasLoader = BiasedWordsLoader.getInstance();
		for (Document docu : allDocuments) {
			for (String word : docu.getWordsWithinDocument()) {
				if (!allUniqueWords.contains(word)) {
					allUniqueWords.add(word);
				}
			}
		}

		for (String biasedWord : biasLoader.getOriginalBiasedWords()) {
			allBiasedWords.add(biasedWord);
		}
		
	}

	public List<String> getAllWords() {
		return allUniqueWords;
	}

	public List<Document> getAllDocuments() {
		return allDocuments;
	}

	public List<String> getAllBiasedWords() {
		return allBiasedWords;
	}

	public List<Role> getAllRoles() {
		return allRoles;
	}

	public void addRoles(List<Role> roles) {
		for (Role role : roles) {
			if (!allRoles.contains(role)) {
				allRoles.add(role);
			}
		}
	}

	public void addBiasedWords(List<String> biasedWords) {
		for (String word : biasedWords) {
			if (!allBiasedWords.contains(word)) {
				allBiasedWords.add(word);
			}
		}
	}

	public void addDocuments(List<Document> documents) {
		for (Document docu : documents) {
			if (!allDocuments.contains(docu)) {
				allDocuments.add(docu);
			}
		}
	}

	public void addWords(List<String> words) {
		for (String word : words) {
			if (!allUniqueWords.contains(word)) {
				allUniqueWords.add(word);
			}
		}
	}

	public void calculatedAdjacentBiasedWords() {
		WordVectorMatrix vectorMatrix = WordVectorMatrix.getInstance();
		this.allBiasedWords = vectorMatrix.calculatedAdjacentBiasedWords(allBiasedWords);
	}

	public LinkedHashMap<String, Double> getWordScores() {
		return avgWordScores;
	}

	public void setWordScores(LinkedHashMap<String, Double> wordScores) {
		this.avgWordScores = wordScores;
	}
}
