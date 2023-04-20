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
    private double speciesSharingThreshold;
    private double sharedFitnessSum;
    private Random random = new Random();
    
    public Species(int id, Genome representative, double speciesSharingThreshold) {
        this.id = id;
        this.representative = representative;
        this.genomes = new ArrayList<>();
        this.sharedFitnessSum = 0;
        this.speciesSharingThreshold = speciesSharingThreshold;
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
        sharedFitnessSum += genome.getFitness() / speciesSharingThreshold;
    }

    public void clearGenomes() {
        genomes.clear();
    }

    public void updateRepresentative() {
        if (!genomes.isEmpty()) {
            representative = genomes.get(0);
        }
    }

    public void calculateSharedFitnessSum(double speciesSharingThreshold) {
        sharedFitnessSum = 0;
        for (Genome genome : genomes) {
            double sharedFitness = genome.getFitness() / speciesSharingThreshold;
            sharedFitnessSum += sharedFitness;
        }
    }
    
    public List<Genome> performSelection(int numberOfOffspring) {
        List<Genome> selectedGenomes = new ArrayList<>();

        // Calculate the total shared fitness of all genomes in the species
        double totalSharedFitness = getSharedFitnessSum();

        // Perform roulette wheel selection to choose genomes for reproduction
        for (int i = 0; i < numberOfOffspring; i++) {
            double randomValue = random.nextDouble() * totalSharedFitness;
            double accumulatedFitness = 0.0;

            for (Genome genome : genomes) {
                accumulatedFitness += genome.getFitness();
                if (accumulatedFitness >= randomValue) {
                    selectedGenomes.add(genome);
                    break;
                }
            }
        }

        return selectedGenomes;
    }

    public void applySurvivalSelection(double survivalRate) {
        int survivalThreshold = (int) Math.ceil(genomes.size() * survivalRate); // Keep top 50% of the individuals
        genomes.sort(Comparator.comparingDouble(Genome::getFitness).reversed());
        genomes = new ArrayList<>(genomes.subList(0, survivalThreshold));
    }
}
