package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Population.Population;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Population.Species;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF.TFIDFDriver;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.BiasedWordsLoader;

public class NEAT {
	private List<Genome> population = new ArrayList<Genome>();
	private List<Species> species = new ArrayList<Species>();

	private static NEAT instance = new NEAT();

	private NEAT() {
	}

	public static NEAT getInstance() {
		return instance;
	}

	public void executeNeatAlgorithm() {
		// Define your problem-specific parameters
		int inputSize = 300; // Number of input neurons
		int outputSize = 1; // Number of output neurons
		int populationSize = 100; // Size of the population
		int generations = 500; // Number of generations to evolve
		int speciesSharingThreshold = 3; // Sharing threshold for speciation

		// Create the FitnessCalculator implementation for your specific problem
		TFIDFDriver tfidfDriver = TFIDFDriver.getInstance();
		BiasedWordsLoader biasedLoader = BiasedWordsLoader.getInstance();
		LinkedHashMap<String, Double> wordScores = tfidfDriver.getSortedAverageTFIDFScores();
		List<String> biasedWords = biasedLoader.getBiasedWords();
		List<String> words = new ArrayList<>(wordScores.keySet());

		IFTDFFitnessCalculator fitnessCalculator = new IFTDFFitnessCalculator(wordScores, biasedWords);

		// Initialize the population
		Population population = new Population(populationSize, inputSize, outputSize, speciesSharingThreshold, words);

		// Evolve the population for a given number of generations
		population.evolvePopulation(generations, fitnessCalculator);

		// Get the best performing genome after the specified number of generations
		Genome bestGenome = population.getBestGenome();
		System.out.println("Best performing genome after " + generations + " generations:");
		System.out.println("Genome fitness: " + bestGenome.getFitness());
//        System.out.println(bestGenome.getScores());
		
		
		int i = 0;
		for (Map.Entry<String, Double> entry : bestGenome.getScores().entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
			i++;
			if (i > 30) {
				break;
			}
		}
	}
}
