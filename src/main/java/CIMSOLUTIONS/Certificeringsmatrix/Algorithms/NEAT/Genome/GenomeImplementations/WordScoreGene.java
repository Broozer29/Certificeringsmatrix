package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.GenomeImplementations;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Gene;

public class WordScoreGene extends Gene {
    // Add any additional properties or methods specific to the word score adjustment task here


    public WordScoreGene(int inputNode, int outputNode, double weight, boolean isEnabled, int innovationNumber) {
        super(inputNode, outputNode, weight, isEnabled, innovationNumber);
    }

    public WordScoreGene(WordScoreGene wordScoreGene) {
        super(wordScoreGene);
    }

    // Add or override any methods specific to the word score adjustment task here
}
