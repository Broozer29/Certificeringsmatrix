package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Population;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.Crossoverseer;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.GenomeCompatibilityCalculator;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.IFTDFFitnessCalculator;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.Mutator;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.TFIDF.TFIDFDriver;

public class Population {
	private List<Genome> genomes;
	private int populationSize;
	private Mutator mutator;
	private Random random;

	private List<Species> speciesList;
	private int nextSpeciesId;
	private GenomeCompatibilityCalculator compatibilityCalculator;
	private double survivalRate;
	// The speciesThreshold determines how similar genomes have to be, to be seen as the
	// same species
	// Higher result in fewer species with more genomes, lower values result in more
	// species with fewer genomes
	private double speciesSharingThreshold;

	public Population(int populationSize, int inputSize, int outputSize, int speciesSharingThreshold, List<String> words) {
		this.populationSize = populationSize;
		this.genomes = new ArrayList<Genome>(populationSize);
		this.mutator = Mutator.getInstance();
		this.random = new Random();

		speciesList = new ArrayList<>();
		nextSpeciesId = 1;
		compatibilityCalculator = new GenomeCompatibilityCalculator(1.0, 1.0, 0.4);
		this.speciesSharingThreshold = speciesSharingThreshold;
		// The top 50% of Genomes will survive, the rest won't.
		survivalRate = 0.5;

		// Initialize the population with random genomes
		for (int i = 0; i < populationSize; i++) {
			genomes.add(new Genome(inputSize, outputSize, words));
		}
	}

	public void evolvePopulation(int generations, IFTDFFitnessCalculator fitnessEvaluator) {
	    Crossoverseer crossover = Crossoverseer.getInstance();
	    for (int generation = 0; generation < generations; generation++) {
	        // Evaluate the fitness of each genome
	        for (Genome genome : genomes) {
	            double fitness = fitnessEvaluator.calculateFitness(genome);
	            genome.setFitness(fitness);
	        }

	        // Speciate the genomes
	        speciate();

	        // Apply survival selection
	        for (Species species : speciesList) {
	            species.applySurvivalSelection(survivalRate);
	        }

	        // Perform selection, crossover, and mutation to create a new generation
	        List<Genome> nextGeneration = new ArrayList<>();
	        for (Species species : speciesList) {
	            // Preserve the best performing genome from each species
	            Genome bestInSpecies = species.getGenomes().stream().max(Comparator.comparingDouble(Genome::getFitness))
	                    .orElse(null);
	            nextGeneration.add(bestInSpecies);

	            // Calculate the number of offspring for each species based on its shared fitness
	            int numberOfOffspring = (int) (species.getSharedFitnessSum() / getTotalSharedFitnessSum()
	                    * (populationSize - speciesList.size()));
	            List<Genome> selectedGenomes = species.performSelection(numberOfOffspring);

	            // Create offspring through crossover and mutation
	            for (int i = 0; i < numberOfOffspring; i++) {
	                if (selectedGenomes.size() >= 2) {
	                    Genome parent1 = selectedGenomes.get(random.nextInt(selectedGenomes.size()));
	                    Genome parent2 = selectedGenomes.get(random.nextInt(selectedGenomes.size()));
	                    Genome offspring = crossover.crossover(parent1, parent2);
	                    mutator.mutate(offspring);
	                    nextGeneration.add(offspring);
	                } else {
	                    nextGeneration.add(new Genome(selectedGenomes.get(0)));
	                }
	            }
	        }

	        // Clear the genomes in each species
	        // This is needed to prevent species from hyperfocussing on a single evolution
	        for (Species species : speciesList) {
	            species.clearGenomes();
	        }

	        // Replace the current generation with the new generation & repeat
	        genomes = nextGeneration;

	        // Update the representatives of each species
	        for (Species species : speciesList) {
	            species.updateRepresentative();
	        }
	    }
	}

	// Get the total shared fitness sum of all species
	private double getTotalSharedFitnessSum() {
		double totalSharedFitnessSum = 0;
		for (Species species : speciesList) {
			totalSharedFitnessSum += species.getSharedFitnessSum();
		}
		return totalSharedFitnessSum;
	}


	// Splits all genomes into species
	public void speciate() {
		// Clear the existing speciesList as we need a new one from scratch
		speciesList.clear();

		// For all genomes, check if they can be placed in an existing species, if so, add it
		// to the species
		for (Genome genome : genomes) {
			boolean addedToExistingSpecies = false;
			for (Species species : speciesList) {
				Genome representative = species.getRepresentative();
				double compatibilityDistance = compatibilityCalculator.calculateCompatibilityDistance(genome,
						representative);
				if (compatibilityDistance < speciesSharingThreshold) {
					species.addGenome(genome);
					addedToExistingSpecies = true;
					break;
				}
			}

			// If not, create a new species and make the genome the representative of the species.
			// speciesSharingThreshold is another customizable parameter for the algoritm, 3.0 is
			// chosen arbitrarily
			if (!addedToExistingSpecies) {
				Species newSpecies = new Species(nextSpeciesId++, genome, speciesSharingThreshold);
				newSpecies.addGenome(genome);
				speciesList.add(newSpecies);
			}
		}
	}

	public Genome getBestGenome() {
		return genomes.stream().max(Comparator.comparingDouble(Genome::getFitness)).orElse(null);
	}
	
	public List<Genome> getAllGenomes(){
		return genomes;
	}
}
