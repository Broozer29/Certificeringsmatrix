package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.InnovationNumberCalculator;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.GenomeImplementations.WordScoreGene;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.GenomeImplementations.WordScoreNode;

public class Genome {

	private List<Gene> genes = new ArrayList<Gene>();
	private List<Node> nodes = new ArrayList<Node>();
	private LinkedHashMap<String, Double> scores = new LinkedHashMap<String, Double>();
	private Map<String, Double> adjustments = new HashMap<>();;
	private double fitness;

	public Genome() {
	}

	// Create a Genome with defined inputs & outputs. Required for a random initial
	// population
	public Genome(int inputSize, int outputSize, List<String> words) {
	    nodes = new ArrayList<>();
	    genes = new ArrayList<>();
	    InnovationNumberCalculator innovationCalculator = InnovationNumberCalculator.getInstance();

	    // Create & add input nodes
	    for (int i = 0; i < inputSize; i++) {
	        nodes.add(new WordScoreNode(i + 1, Node.NodeType.INPUT, words.get(i)));
	    }

	    // Create & add output nodes
	    for (int i = 0; i < outputSize; i++) {
	        nodes.add(new WordScoreNode(inputSize + i + 1, Node.NodeType.OUTPUT));
	    }

		// Create connections between ALL input and ALL output nodes
		// This is a standard for the NEAT algorithm
		for (int i = 1; i <= inputSize; i++) {
			for (int j = inputSize + 1; j <= inputSize + outputSize; j++) {
				int innovationNumber = innovationCalculator.getInnovationNumber(i, j);
				double randomWeight = Math.random() * 2 - 1;
				genes.add(new WordScoreGene(i, j, randomWeight, true, innovationNumber));
			}
		}
	}

	// Create a copy of an existing Genome, required for situations where offspring is
	// attempted to be made, but no second Genome is found
	public Genome(Genome genome) {
		this.nodes = genome.getNodes();
		this.genes = genome.getGenes();
		this.fitness = genome.getFitness();
	}

	public void addGene(Gene gene) {
		if (!this.genes.contains(gene)) {
			this.genes.add(gene);
		}
	}

	public void addNode(Node node) {
		if (!this.nodes.contains(node)) {
			this.nodes.add(node);
		}
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness2) {
		this.fitness = fitness2;
	}

	public List<Node> getNodes() {
		return this.nodes;
	}

	public List<Gene> getGenes() {
		return genes;
	}

	public LinkedHashMap<String, Double> getScores() {
		return scores;
	}

	public void setAdjustment(String word, double adjustment) {
		adjustments.put(word, adjustment);
	}

	public Double getAdjustment(String word) {
		return adjustments.getOrDefault(word, 0.0);
	}

	public boolean hasConnection(int inputNodeId, int outputNodeId) {
		for (Gene gene : genes) {
			if (gene.getInputNode() == inputNodeId && gene.getOutputNode() == outputNodeId) {
				return true;
			}
		}
		return false;
	}

	public LinkedHashMap<String, Double> adjustWordScores(LinkedHashMap<String, Double> wordScores,
			List<String> biasedWords) {
		LinkedHashMap<String, Double> adjustedScores = new LinkedHashMap<>();

		for (Map.Entry<String, Double> entry : wordScores.entrySet()) {
			String word = entry.getKey();
			double originalScore = entry.getValue();
			double bonus = 0;

			if (biasedWords.contains(word)) {
				for (Gene gene : genes) {
					WordScoreGene wordScoreGene = (WordScoreGene) gene;
					int inputNodeId = wordScoreGene.getInputNode();
					int outputNodeId = wordScoreGene.getOutputNode();

					WordScoreNode inputNode = (WordScoreNode) nodes.get(inputNodeId - 1);
					WordScoreNode outputNode = (WordScoreNode) nodes.get(outputNodeId - 1);
					if (inputNode.getWord().equals(word) && outputNode.getType() == Node.NodeType.OUTPUT) {
						bonus += wordScoreGene.getWeight();
					}
				}
			}

			adjustedScores.put(word, originalScore + bonus);
		}
		scores = adjustedScores;
		return adjustedScores;
	}

}
