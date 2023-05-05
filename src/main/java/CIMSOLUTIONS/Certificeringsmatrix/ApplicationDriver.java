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
	private NEATDriver neatInstance = null;

	private static ApplicationDriver instance = new ApplicationDriver();

	private ApplicationDriver() {

	}

	public static ApplicationDriver getInstance() {
		return instance;
	}

	/*- Loads & reads all files required for the program to function */
	public void loadAndReadFiles() {
		biasLoader = BiasedWordsLoader.getInstance();
		biasLoader.loadAndReadBiasedWords();

		loadDriver = DocumentLoadingDriver.getInstance();
		loadDriver.loadDocuments();

		roleLoader = RoleLoader.getInstance();
		roleLoader.loadAndReadRoles();

		storageManager = StorageManager.getInstance();
		storageManager.refreshStorageManager();

		
	}

	/*- Calculates the IF-TDF score of individual words from the loaded Documents*/
	public void performTFIDF() {
		tfidfDriver = TFIDFDriver.getInstance();
		tfidfDriver.calculateTFIDF();
	}

	/*- Creates a Word2vec matrix from individual words from the loaded Documents*/
	public void performHC() {
		matrix = WordVectorMatrix.getInstance();
		matrix.createWordVectorMatrix();
	}

	/*- Searches for additional biased words in the loaded Documents*/
	public void findSimilarBiasedWords() {
		storageManager = StorageManager.getInstance();
		storageManager.calculatedAdjacentBiasedWords();
	}

	/*-Starts the NEAT algorithm for training Genomes and returns the best performing Genome afterwards*/
	public Genome trainNewNeuralNetworks() {
		LinkedHashMap<String, Double> wordScores = storageManager.getWordScores();
		List<String> biasedWords = storageManager.getAllBiasedWords();
		List<String> words = new ArrayList<>(wordScores.keySet());

		NEATConfiguration neatConfig = new NEATConfiguration();
		neatConfig.setNeuronInputSize(words.size());
		neatConfig = setDefaultNEATConfigurations(neatConfig);
		neatInstance = new NEATDriver(neatConfig);

		neatInstance.initDriverWords(wordScores, biasedWords);
		neatInstance.initNEATAlgorithms();
		if (neatInstance.isReadyForUse()) {
			neatInstance.createPopulation();
			neatInstance.evolvePopulation();
			return neatInstance.getBestPerformingGenome();
		} else
			return null;
	}

	/*- Starts the NEAT algorithm for training Genomes and returns the best performing Genome afterwards
	 *  This method replaces the normally randomly generated population of Genomes with copies of the given Genome
	 *  Use this method for training imported Genomes
	 * */
	public Genome trainExistingNeuralNetwork(Genome importedGenome) {
		LinkedHashMap<String, Double> wordScores = importedGenome.getScores();
		List<String> biasedWords = importedGenome.getBiasedWords();
		List<String> words = new ArrayList<>(wordScores.keySet());

		NEATConfiguration neatConfig = new NEATConfiguration();
		neatConfig.setNeuronInputSize(words.size());
		neatConfig = setDefaultNEATConfigurations(neatConfig);
		neatInstance = new NEATDriver(neatConfig);

		neatInstance.initDriverWords(wordScores, biasedWords);
		neatInstance.initNEATAlgorithms();
		if (neatInstance.isReadyForUse()) {
			neatInstance.createPopulation();
			neatInstance.evolvePopulation();
			return neatInstance.getBestPerformingGenome();
		} else
			return null;
	}

	// Sets default configurations for a NEAT instance
	private NEATConfiguration setDefaultNEATConfigurations(NEATConfiguration neatConfig) {
		neatConfig.setNeuronOutputSize(5);
		neatConfig.setPopulationSize(5);
		neatConfig.setGenerations(1);
		neatConfig.setSpeciesSharingThreshold(3);
		neatConfig.setTopXWordsUsedForFitnessCalculation(30);

		neatConfig.setExcessGeneImportance(1.0);
		neatConfig.setDisjointGeneImportance(0.4);
		neatConfig.setWeightImportance(0.4);

		neatConfig.setWeightMutationRate(0.8);
		neatConfig.setNewConnectionMutationRate(0.05);
		neatConfig.setNewNodeMutationRate(0.03);

		neatConfig.setMutationStrength(1.0);

		return neatConfig;
	}

	/*-Combines competences (individual words with scores) with Roles using the Word2vec matrix's similarity methods*/
	public CertificeringsMatrix combineCompetencesWithRoles(Genome bestPerformingGenome, int maximumAmountOfCompetences,
			double similarityThreshold) {
		List<Competence> competences = bestPerformingGenome.createCompetences(maximumAmountOfCompetences);
		List<Role> roles = roleLoader.getRoles();
		
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
