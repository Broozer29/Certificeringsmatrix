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
	private AanvraagStorage aanvraagStorage;
	private CVStorage cvStorage;
	private RoleLoader roleLoader;
	private BiasedWordsLoader biasLoader;

	private LinkedHashMap<String, Double> avgWordScores = new LinkedHashMap<String, Double>();
	private List<String> allBiasedWords = new ArrayList<String>();
	private List<Document> allDocuments = new ArrayList<Document>();
	private List<String> allUniqueWords = new ArrayList<String>();
	private List<Role> allRoles = new ArrayList<Role>();

	private StorageManager() {
		aanvraagStorage = AanvraagStorage.getInstance();
		cvStorage = CVStorage.getInstance();
	}

	public static StorageManager getInstance() {
		return instance;
	}
	

	// When a storage has loaded additional files, this function needs to be called
	// again to synchronize the manager with the storages
	public void refreshStorageManager() {
		allDocuments = new ArrayList<Document>();
		aanvraagStorage = AanvraagStorage.getInstance();
		cvStorage = CVStorage.getInstance();
		roleLoader = RoleLoader.getInstance();
		biasLoader = BiasedWordsLoader.getInstance();

		for (Document docu : cvStorage.getAllDocuments()) {
			allDocuments.add(docu);
		}
		for (Document docu : aanvraagStorage.getAllAanvragen()) {
			allDocuments.add(docu);
		}
		
		for(Document docu : allDocuments) {
			for (String word : docu.getWordsWithinDocument()) {
				if(!allUniqueWords.contains(word)) {
					allUniqueWords.add(word);
				}
			}
		}
		
		for(Role role : roleLoader.getRoles()) {
			allRoles.add(role);
		}
		
		for(String biasedWord : biasLoader.getOriginalBiasedWords()) {
			allBiasedWords.add(biasedWord);
		}
	}
	
	
	public List<String> getAllWords(){
		return allUniqueWords;
	}
	
	public List<Document> getAllDocuments(){
		return allDocuments;
	}
	
	public List<String> getAllBiasedWords() {
		return allBiasedWords;
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
