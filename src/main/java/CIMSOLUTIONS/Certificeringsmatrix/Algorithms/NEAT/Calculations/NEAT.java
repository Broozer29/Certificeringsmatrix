package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Population.Population;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF.TFIDFDriver;
import CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders.BiasedWordsLoader;

public class NEAT {
	private static NEAT instance = new NEAT();

	private NEAT() {
	}

	public static NEAT getInstance() {
		return instance;
	}

	public void executeNeatAlgorithm() {
		// Define your problem-specific parameters


		// Create the FitnessCalculator implementation for your specific problem
		TFIDFDriver tfidfDriver = TFIDFDriver.getInstance();
		BiasedWordsLoader biasedLoader = BiasedWordsLoader.getInstance();
		LinkedHashMap<String, Double> wordScores = tfidfDriver.getSortedAverageTFIDFScores();
		List<String> biasedWords = biasedLoader.getBiasedWords();
		List<String> words = new ArrayList<>(wordScores.keySet());

		IFTDFFitnessCalculator fitnessCalculator = new IFTDFFitnessCalculator(wordScores, biasedWords);

		
		int inputSize = words.size(); // Number of input neurons
		int outputSize = 5; // Number of output neurons
		int populationSize = 50; // Size of the population
		int generations = 10; // Number of generations to evolve
		int speciesSharingThreshold = 3; // Sharing threshold for speciation
		
		// Initialize the population
		Population population = new Population(populationSize, inputSize, outputSize, speciesSharingThreshold, words, biasedWords);

		// Evolve the population for a given number of generations
		population.evolvePopulation(generations, fitnessCalculator);

		// Get the best performing genome after the specified number of generations
		Genome bestGenome = population.getBestGenome();
		System.out.println("Best performing genome after " + generations + " generations:");
		System.out.println("Genome fitness: " + bestGenome.getFitness());
		
		String filename = "endresults.txt";
		try (FileWriter writer = new FileWriter(filename)) {
			for (Map.Entry<String, Double> entry : bestGenome.getScores().entrySet()) {
				if (entry.getValue() != 0.0) {
					writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
				}
			}
			System.out.println("endresults.txt");

		} catch (IOException e) {
			System.out.println("An error occurred while writing to " + filename);
			e.printStackTrace();
		}
		
	}
}
