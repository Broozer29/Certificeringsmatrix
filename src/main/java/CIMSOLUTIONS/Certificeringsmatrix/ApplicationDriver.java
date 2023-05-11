package CIMSOLUTIONS.Certificeringsmatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.HierarchicalClustering.CompetenceRoleCombiner;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.HierarchicalClustering.WordVectorMatrix;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.NEATConfiguration;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.NEATDriver;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF.TFIDFDriver;
import CIMSOLUTIONS.Certificeringsmatrix.Data.DocumentLoadingDriver;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Exporters.CertificeringsMatrixExporter;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Exporters.GenomeExporter;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.BiasedWordsLoader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.RoleLoader;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.StorageManager;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.CertificeringsMatrix;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Competence;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Role;

/*- This class is responsible for steering parts of the application. 
 *  Some functionalities require multiple method calls in chronological order, but functionalities are grouped together in this class
 */
public class ApplicationDriver {
	private DocumentLoadingDriver loadDriver = null;
	private StorageManager storageManager = null;
	private TFIDFDriver tfidfDriver = null;
	private WordVectorMatrix matrix = null;
	private RoleLoader roleLoader = null;
	private BiasedWordsLoader biasLoader = null;
	private NEATDriver neatDriver = null;

	private static ApplicationDriver instance = new ApplicationDriver();

	private ApplicationDriver() {

	}

	public static ApplicationDriver getInstance() {
		return instance;
	}

	/*- Loads & reads all files required for the program to function */
	public void loadAndReadFiles() {
		System.out.println("   > Loading and reading Biased Words, then storing them in StorageManager");
		biasLoader = new BiasedWordsLoader();
		biasLoader.loadAndReadBiasedWords();

		System.out.println("   > Loading, reading & creating Documents (Aanvragen & CV's) then storing them in StorageManager");
		loadDriver = DocumentLoadingDriver.getInstance();
		loadDriver.loadDocuments();

		System.out.println("   > Loading, reading & creating Roles then storing them in StorageManager");
		roleLoader = new RoleLoader();
		roleLoader.loadAndReadRoles();

		System.out.println("   > Filtering all loaded words so there are no duplicates for the upcoming algorithms");
		storageManager = StorageManager.getInstance();
		storageManager.filterDuplicateWords();

		
	}

	/*- Calculates the IF-TDF score of individual words from the loaded Documents*/
	public void performTFIDF() {
		tfidfDriver = new TFIDFDriver();
		tfidfDriver.calculateTFIDF();
	}

	/*- Creates a Word2vec matrix from individual words from the loaded Documents*/
	public void performHC() {
		System.out.println("   > Creating a Vector Matrix of all words loaded from the created Documents");
		matrix = WordVectorMatrix.getInstance();
		matrix.createWordVectorMatrix();
	}

	/*- Searches for additional biased words in the loaded Documents*/
	public void findSimilarBiasedWords() {
		System.out.println("   > Finding words that are adjacent to stored Biased Words and add them to the Biased Words for later algorithms");
		storageManager = StorageManager.getInstance();
		storageManager.calculatedAdjacentBiasedWords();
	}

	/*-Starts the NEAT algorithm for training Genomes and returns the best performing Genome afterwards*/
	public Genome trainNewNeuralNetworks() {
		System.out.println("   > Acquiring data from StorageManager required for the NEAT algorithm");
		LinkedHashMap<String, Double> wordScores = storageManager.getWordScores();
		List<String> biasedWords = storageManager.getAllBiasedWords();
		List<String> words = new ArrayList<>(wordScores.keySet());

		System.out.println("   > Creating a NEATConfiguration for the NEAT algorithm");
		NEATConfiguration neatConfig = new NEATConfiguration();
		neatConfig.setNeuronInputSize(words.size());
		neatConfig.setDefaultNEATConfigurations();
		neatDriver = new NEATDriver(neatConfig);

		System.out.println("   > Initializing the NEATDriver with words & their TF-IDF scores and all Biased Words");
		neatDriver.initDriverWords(wordScores, biasedWords);
		neatDriver.initNEATAlgorithms();
		if (neatDriver.isReadyForUse()) {
			System.out.println("   > Creating a population of Genomes");
			neatDriver.createPopulation();
			System.out.println("   > Evolving the population of Genomes corresponding with the given NEATConfigurations");
			neatDriver.evolvePopulation();
			return neatDriver.getBestPerformingGenome();
		} else
			return null;
	}

	/*- Starts the NEAT algorithm for training Genomes and returns the best performing Genome afterwards
	 *  This method replaces the normally randomly generated population of Genomes with copies of the given Genome
	 *  Use this method for training imported Genomes
	 * */
	public Genome trainExistingNeuralNetwork(Genome importedGenome) {
		System.out.println("   > Acquiring data from the imported Genome required for the NEAT algorithm");
		LinkedHashMap<String, Double> wordScores = importedGenome.getScores();
		List<String> biasedWords = importedGenome.getBiasedWords();
		List<String> words = new ArrayList<>(wordScores.keySet());

		System.out.println("   > Creating a NEATConfiguration for the NEAT algorithm");
		NEATConfiguration neatConfig = new NEATConfiguration();
		neatConfig.setNeuronInputSize(words.size());
		neatConfig.setDefaultNEATConfigurations();
		neatDriver = new NEATDriver(neatConfig);

		System.out.println("   > Initializing the NEATDriver with words & their TF-IDF scores and all Biased Words loaded from the Genome");
		neatDriver.initDriverWords(wordScores, biasedWords);
		neatDriver.initNEATAlgorithms();
		if (neatDriver.isReadyForUse()) {
			System.out.println("   > Creating a population of Genomes that are identical to the given Genome");
			neatDriver.createPopulation();
			System.out.println("   > Evolving the population of Genomes corresponding with the given NEATConfigurations");
			neatDriver.evolvePopulation();
			return neatDriver.getBestPerformingGenome();
		} else
			return null;
	}


	/*-Combines competences (individual words with scores) with Roles using the Word2vec matrix's similarity methods*/
	public CertificeringsMatrix combineCompetencesWithRoles(Genome bestPerformingGenome, int maximumAmountOfCompetences,
			double similarityThreshold) {
		System.out.println("   > Creating " + maximumAmountOfCompetences + " competences from the highest scored words of a Genome");
		List<Competence> competences = bestPerformingGenome.createCompetences(maximumAmountOfCompetences);
		List<Role> roles = storageManager.getAllRoles();
		
		System.out.println("   > Combinging Roles and Competences (implementatie moet nog veranderd worden, pas dit bericht aan zodra dat gedaan is)");
		CompetenceRoleCombiner compRoleCombiner = new CompetenceRoleCombiner();
		CertificeringsMatrix certificeringsMatrix = compRoleCombiner.addCompetencesToRoles(competences, roles, similarityThreshold);
		certificeringsMatrix.sortCompetencesBelongingToRoles();
		return certificeringsMatrix;
	}

	/*- Exports the generated Certificeringsmatrix of Roles and competences belonging to those roles */
	public void exportMatrix(CertificeringsMatrix certificeringsMatrix) {
		CertificeringsMatrixExporter exporter = new CertificeringsMatrixExporter();
		exporter.exportMatrix(certificeringsMatrix);
	}

	/*- Exports the given Genome as a serialized object*/
	public void exportGenome(Genome genome, String filename) throws IOException {
		GenomeExporter genomeExporter = GenomeExporter.getInstance();
		genomeExporter.exportGenome(genome, filename);
	}

	

}
