package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.Crossoverseer;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.GenomeCompatibilityCalculator;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.GenomeFitnessCalculator;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.Mutator;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Population.Population;

/*- This class is responsible for driving the NEAT algorithm.
 *  It also contains a majority of the configuration settings of other compoments of the NEAT algorithm
 */
public class NEATDriver {
	private Genome bestPerformingGenome = null;

	private int neuronInputSize = 0;
	private int neuronOutputSize = 0;
	private int populationSize = 0;
	private int generations = 0;
	private int speciesSharingThreshold = 0;
	private int topXWordsUsedForFitnessCalculation = 0;
	private int amountOfOffspring = 0;

	private double excessGeneImportance = 0.0; /*- The importance of excess genes for calculating the similarity of 2 Genomes */
	private double disjointGeneImportance = 0.0; /*- The importance of disjoint genes for calculating the similarity of 2 Genomes */
	private double weightImportance = 0.0; /*- The importance of difference in weights of nodes for calculating the similarity of 2 Genomes */

	// Chances of this type of mutations to occur
	private double weightMutationRate = 0.0;
	private double newConnectionMutationRate = 0.00;
	private double newNodeMutationRate = 0.00;

	private double mutationStrength = 0;
	private double bonusPointsWeightSubtractor = 0;

	private Mutator mutator = null;
	private Crossoverseer crossoverseer = null;
	private GenomeCompatibilityCalculator genomeCompatabilityCalculator = null;
	private GenomeFitnessCalculator fitnessCalculator = null;

	private List<String> words = new ArrayList<String>();
	private List<String> biasedWords = new ArrayList<String>();
	private LinkedHashMap<String, Double> wordScores = new LinkedHashMap<String, Double>();

	private Population population = null;

	public NEATDriver(NEATConfiguration neatConfig) {
		this.neuronInputSize = neatConfig.getNeuronInputSize();
		this.neuronOutputSize = neatConfig.getNeuronOutputSize();
		this.populationSize = neatConfig.getPopulationSize();
		this.generations = neatConfig.getGenerations();
		this.speciesSharingThreshold = neatConfig.getSpeciesSharingThreshold();
		this.topXWordsUsedForFitnessCalculation = neatConfig.getTopXWordsUsedForFitnessCalculation();
		this.excessGeneImportance = neatConfig.getExcessGeneImportance();
		this.disjointGeneImportance = neatConfig.getDisjointGeneImportance();
		this.weightImportance = neatConfig.getWeightImportance();
		this.weightMutationRate = neatConfig.getWeightMutationRate();
		this.newConnectionMutationRate = neatConfig.getNewConnectionMutationRate();
		this.newNodeMutationRate = neatConfig.getNewNodeMutationRate();
		this.mutationStrength = neatConfig.getMutationStrength();
		this.amountOfOffspring = neatConfig.getMinimumAmountOfOffspring();
		this.bonusPointsWeightSubtractor = neatConfig.getBonusPointsWeightSubtractor();
	}

	public void initDriverWords(LinkedHashMap<String, Double> wordScores, List<String> biasedWords) {
		this.wordScores = wordScores;
		this.biasedWords = biasedWords;

		this.words = new ArrayList<>(wordScores.keySet());
	}

	public void initNEATAlgorithms() {
		fitnessCalculator = new GenomeFitnessCalculator(wordScores, biasedWords, topXWordsUsedForFitnessCalculation, bonusPointsWeightSubtractor);

		genomeCompatabilityCalculator = new GenomeCompatibilityCalculator(excessGeneImportance, disjointGeneImportance,
				weightImportance);

		mutator = new Mutator(weightMutationRate, newConnectionMutationRate, newNodeMutationRate, mutationStrength);
		crossoverseer = new Crossoverseer();
	}

	public void importPopulation() {

	}

	public void createPopulation() {
		this.population = new Population(populationSize, neuronInputSize, neuronOutputSize, speciesSharingThreshold,
				words, biasedWords, genomeCompatabilityCalculator, mutator, amountOfOffspring);
	}

	// Evolve the population for a given number of generations
	public void evolvePopulation() {
		population.evolvePopulation(generations, fitnessCalculator, crossoverseer);

		// Get the best performing genome after the specified number of generations
		bestPerformingGenome = population.getBestGenome();
		System.out.println("Best performing genome after " + generations + " generations:");
		System.out.println("Genome fitness: " + bestPerformingGenome.getFitness());
	}

	public Genome getBestPerformingGenome() {
		return population.getBestGenome();
	}

	public boolean isReadyForUse() {
		if (words.isEmpty() || words.size() == 0) {
			System.out.println(
					"Words is empty. Make sure the TF-IDF algorithm provided words or the Genome is imported correctly.");
			return false;
		}

		if (wordScores.isEmpty() || wordScores.size() == 0) {
			System.out.println(
					"WordScores is empty. Make sure TF-IDF algorithm provided words or the Genome is imported correctly.");
			return false;
		}

		if (biasedWords.isEmpty() || biasedWords.size() == 0) {
			System.out.println("BiasedWords is empty. Make sure the Biased Words are being loaded correctly.");
			return false;
		}

		if (fitnessCalculator == null) {
			System.out.println("The Fitness Calculator has not been created yet.");
			return false;
		}

		if (genomeCompatabilityCalculator == null) {
			System.out.println("The Genome compatability calculator has not been created yet.");
			return false;
		}

		if (mutator == null) {
			System.out.println("The Mutator has not been created yet.");
			return false;
		}

		if (crossoverseer == null) {
			System.out.println("The crossoverseer has not been created yet.");
			return false;
		}

		return true;
	}
}
