package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Population.Population;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Population.Species;

public class NEAT {
	private List<Genome> population = new ArrayList<Genome>();
	private List<Species> species = new ArrayList<Species>();
	
	private static NEAT instance = new NEAT();
	
	private NEAT() {
	}
	
	public static NEAT getInstance() {
		return instance;
	}
	
	public void doTheStuff() {
		
		
        // Define your problem-specific parameters
        int inputSize = 20; // Number of input neurons
        int outputSize = 10; // Number of output neurons
        int populationSize = 50; // Size of the population
        int generations = 10; // Number of generations to evolve
        int speciesSharingThreshold = 5; // Sharing threshold for speciation

        // Create the FitnessCalculator implementation for your specific problem
        FitnessCalculator fitnessEvaluator = new FitnessCalculator();

        // Initialize the population
        Population population = new Population(populationSize, inputSize, outputSize, speciesSharingThreshold);

        // Evolve the population
        population.evolvePopulation(generations, fitnessEvaluator);

        // Find the best genome after evolution
        Genome bestGenome = population.getBestGenome();
        System.out.println(bestGenome.getFitness());

        // Use the best genome to solve the problem or further analyze its structure
        // ...
		
		/*- We hebben nu:
		 * 		Een population met random Genomes
		 * 		Genomes krijgen een uniek innovationnumber
		 * 		Een population die kan mutaten door een Genome te geven aan Mutator
		 * 		Genomes die gecombineerd kunnen worden tot 1 nieuwe via Crossoverseer
		 * 		Een population die opgedeeld kan worden in species
		 * 
		 * Wat nu nog moet:
		 * 		Fitness van een genome berekenen
		 * 		Shared fitness van een genome berekenen
		 * 		De beste genomes uit een species selecteren voor verdere evolutie
		 * 		Termination criteria maken voor het killen van slechte genomes
		 * 
		 * Vervolgens kan dit aangesloten worden op een opdracht die het network moet uitvoeren. 
		 * 		Gaat hand in hand met fitness van een genome berekenen
		 */
		
	}
	
	private void refreshGeneList() {
	}
	
}
