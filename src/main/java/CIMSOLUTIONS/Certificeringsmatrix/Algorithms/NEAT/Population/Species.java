package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Population;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;

public class Species {
	private int id;
	private List<Genome> genomes;
	private Genome representative;
	private double sharedFitnessSum;
	private Random random = new Random();

	public Species(int id, Genome representative) {
		this.id = id;
		this.representative = representative;
		this.genomes = new ArrayList<>();
		this.sharedFitnessSum = 0;
	}

	public int getId() {
		return id;
	}

	public List<Genome> getGenomes() {
		return genomes;
	}

	public Genome getRepresentative() {
		return representative;
	}

	public double getSharedFitnessSum() {
		return sharedFitnessSum;
	}

	public void addGenome(Genome genome) {
		genomes.add(genome);
	}

	public void clearGenomes() {
		genomes.clear();
	}

	// Returns the first Genome that created (and thus resembles) the Species
	public void updateRepresentative() {
		if (!genomes.isEmpty()) {
			representative = genomes.get(0);
		}
	}

	// Calculate the total shared fitness of all Genomes in the species
	public void calculateSharedFitnessSum() {
	    sharedFitnessSum = 0;
	    for (Genome genome : genomes) {
	        sharedFitnessSum += genome.getFitness();
	    }
	    sharedFitnessSum = sharedFitnessSum / genomes.size();
	}
	
	public List<Genome> performSelection(int numberOfOffspring) {
	    List<Genome> selectedGenomes = new ArrayList<>();

	    calculateSharedFitnessSum();

	    // Perform roulette wheel selection to choose random Genomes of the species to be the parent
	    for (int i = 0; i < numberOfOffspring; i++) {
	        double randomValue = random.nextDouble() * sharedFitnessSum;
	        double accumulatedFitness = 0.0;
	        for (Genome genome : genomes) {
	            accumulatedFitness += genome.getFitness();
	            if (accumulatedFitness >= randomValue) {
	                selectedGenomes.add(genome);
	                break;
	            }
	        }
	    }

	    // Ensure that the list of selected genomes has at least 2 genomes
	    while (selectedGenomes.size() < 2 && genomes.size() >= 2) {
	        Genome randomGenome = genomes.get(random.nextInt(genomes.size()));
	        if (!selectedGenomes.contains(randomGenome)) {
	            selectedGenomes.add(randomGenome);
	        }
	    }

	    return selectedGenomes;
	}
	public void applySurvivalSelection(double survivalRate) {
		/*- Keep top X% of the individuals, then sort their fitness in descending order and keep the best performing 50%
		 *  Once again credits to Stack Overflow for the Comparator implementation
		*/
		int survivalThreshold = (int) Math.ceil(genomes.size() * survivalRate);
		genomes.sort(Comparator.comparingDouble(Genome::getFitness).reversed());
		genomes = new ArrayList<>(genomes.subList(0, survivalThreshold));
	}
}
