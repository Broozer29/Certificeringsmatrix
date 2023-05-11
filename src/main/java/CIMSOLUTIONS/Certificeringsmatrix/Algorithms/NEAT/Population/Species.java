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
        sharedFitnessSum += genome.getFitness() / genomes.size();
    }

    public void clearGenomes() {
        genomes.clear();
    }

    public void updateRepresentative() {
        if (!genomes.isEmpty()) {
            representative = genomes.get(0);
        }
    }

    //Selects the best performing Genomes or random Genomes for crossover
    public List<Genome> performSelection(int numberOfOffspring, double randomSelectionProb) {
        List<Genome> selectedGenomes = new ArrayList<>();
        double totalSharedFitness = getSharedFitnessSum();
        List<Double> normalizedFitness = new ArrayList<>();
        for (Genome genome : genomes) {
            normalizedFitness.add(genome.getFitness() / totalSharedFitness);
        }

        for (int i = 0; i < numberOfOffspring; i++) {
            if (random.nextDouble() < randomSelectionProb) {
                // Choose a random genome
                int randomIndex = random.nextInt(genomes.size());
                selectedGenomes.add(genomes.get(randomIndex));
            } else {
                // Perform roulette wheel selection
                double randomValue = random.nextDouble();
                double accumulatedFitness = 0.0;
                int index = 0;
                for (Double fitness : normalizedFitness) {
                    accumulatedFitness += fitness;
                    if (accumulatedFitness >= randomValue) {
                        selectedGenomes.add(genomes.get(index));
                        break;
                    }
                    index++;
                }
            }
        }

        return selectedGenomes;
    }
    
    
//    public List<Genome> performSelection(int numberOfOffspring) {
//        List<Genome> selectedGenomes = new ArrayList<>();
//
//        // Calculate the total shared fitness of all Genomes in the species
//        double totalSharedFitness = getSharedFitnessSum();
//
//        // Normalize fitness scores
//        List<Double> normalizedFitness = new ArrayList<>();
//        for (Genome genome : genomes) {
//            normalizedFitness.add(genome.getFitness() / totalSharedFitness);
//        }
//
//        // Perform roulette wheel selection to choose Genomes for the next generation
//        for (int i = 0; i < numberOfOffspring; i++) {
//            double randomValue = random.nextDouble();
//            double accumulatedFitness = 0.0;
//            int index = 0;
//
//            for (Double fitness : normalizedFitness) {
//                accumulatedFitness += fitness;
//                if (accumulatedFitness >= randomValue) {
//                    selectedGenomes.add(genomes.get(index));
//                    break;
//                }
//                index++;
//            }
//        }
//
//        return selectedGenomes;
//    }

    public void applySurvivalSelection(double survivalRate) {
    	// Keep top X% of the individuals, then sort their fitness in descending order and keep the best performing 50%
    	// Once again credits to Stack Overflow for the Comparator implementation
        int survivalThreshold = (int) Math.ceil(genomes.size() * survivalRate); 
        genomes.sort(Comparator.comparingDouble(Genome::getFitness).reversed());
        genomes = new ArrayList<>(genomes.subList(0, survivalThreshold));
    }
}