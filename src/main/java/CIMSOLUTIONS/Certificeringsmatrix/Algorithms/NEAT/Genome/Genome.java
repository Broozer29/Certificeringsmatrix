package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.InnovationNumberCalculator;

public class Genome {

	private List<Gene> genes = new ArrayList<Gene>();
	private List<Node> nodes = new ArrayList<Node>();

	private double fitness;

	public Genome() {
	}

	// Create a Genome with defined inputs & outputs. Required for a random initial
	// population
	public Genome(int inputSize, int outputSize) {
		nodes = new ArrayList<>();
		genes = new ArrayList<>();
		InnovationNumberCalculator innovationCalculator = InnovationNumberCalculator.getInstance();

		// Create & add input nodes
		for (int i = 0; i < inputSize; i++) {
			nodes.add(new Node(i + 1, Node.NodeType.INPUT));
		}

		// Create & add output nodes
		for (int i = 0; i < outputSize; i++) {
			nodes.add(new Node(inputSize + i + 1, Node.NodeType.OUTPUT));
		}

		// Create connections between ALL input and ALL output nodes
		// This is a standard for the NEAT algorithm
		for (int i = 1; i <= inputSize; i++) {
			for (int j = inputSize + 1; j <= inputSize + outputSize; j++) {
				int innovationNumber = innovationCalculator.getInnovationNumber(i, j);
				double randomWeight = Math.random() * 2 - 1;
				genes.add(new Gene(i, j, randomWeight, true, innovationNumber));
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
	
	public LinkedHashMap<String, Double> adjustWordScores(LinkedHashMap<String, Double> wordScores) {
	    LinkedHashMap<String, Double> adjustedScores = new LinkedHashMap<>();

	    // TODO: Implement the logic to adjust the word scores using the genes and nodes in the Genome

	    return adjustedScores;
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

	public boolean hasConnection(int inputNodeId, int outputNodeId) {
		for (Gene gene : genes) {
			if (gene.getInputNode() == inputNodeId && gene.getOutputNode() == outputNodeId) {
				return true;
			}
		}
		return false;
	}

}
