package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.GenomeImplementations;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Node;

public class WordScoreNode extends Node {
    private String word;

    public WordScoreNode() {
        super();
    }

    public WordScoreNode(int id, NodeType type, double value) {
        super(id, type, value);
    }

    public WordScoreNode(int id, NodeType type) {
        super(id, type);
    }

    public WordScoreNode(int id, NodeType type, String word) {
        super(id, type);
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    // Add or override any methods specific to the word score adjustment task here
}

