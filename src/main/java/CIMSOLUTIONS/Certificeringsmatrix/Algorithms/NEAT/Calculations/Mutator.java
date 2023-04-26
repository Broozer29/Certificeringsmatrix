package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.List;
import java.util.Random;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Gene;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Node;

/*- This class is responsible for creating mutations
 * 
 */
public class Mutator {
	private static Mutator instance = new Mutator();
	private Random random = new Random();
	private InnovationNumberCalculator innovationCalculator = InnovationNumberCalculator.getInstance();

	private double weightMutationRate = 0.8;
	private double newConnectionMutationRate = 0.05;
	private double newNodeMutationRate = 0.03;

	private Mutator() {
	}

	public static Mutator getInstance() {
		return instance;
	}

	// Select a random mutation, or no mutation at all.
	public void mutate(Genome genome) {
		Double randomDouble = random.nextDouble();

		if (randomDouble < weightMutationRate) {
			mutateWeights(genome);
		}

		if (randomDouble < newConnectionMutationRate) {
			addNewConnection(genome);
		}

		if (randomDouble < newNodeMutationRate) {
			addNewNode(genome);
		}
	}

	private void mutateWeights(Genome genome) {
		List<Gene> genes = genome.getGenes();
		for (Gene gene : genes) {
			if (random.nextBoolean()) {
				double weight = gene.getWeight();
				// Gaussian is used because this appears to be preferred in A.I.
				// What specific perks it brings I don't know, but it is recommended so I'll use it
				double mutation = random.nextGaussian() * 2;
				gene.setWeight(weight + mutation);
			}
		}
	}

	private void addNewConnection(Genome genome) {
		List<Node> nodes = genome.getNodes();
		// Select random nodes
		Node node1 = nodes.get(random.nextInt(nodes.size()));
		Node node2 = nodes.get(random.nextInt(nodes.size()));

		// If the randomly selected nodes are inputs & outputs only, return
		// This is needed because inputs and outputs can't be connected with their own type.
		if (node1.getType() == Node.NodeType.INPUT && node2.getType() == Node.NodeType.INPUT) {
			return;
		}

		if (node1.getType() == Node.NodeType.OUTPUT && node2.getType() == Node.NodeType.OUTPUT) {
			return;
		}

		// A node cannot create a connection with itself
		if (node1.getId() == node2.getId()) {
			return;
		}

		if (node1.getType() == Node.NodeType.HIDDEN && node2.getType() == Node.NodeType.INPUT) {
			Node temp = node1;
			node1 = node2;
			node2 = temp;
		}

		// We don't want duplicate connections
		if (genome.hasConnection(node1.getId(), node2.getId())) {
			return;
		}

		// Create a new gene and add it to the genome
		int inputNode = node1.getId();
		int outputNode = node2.getId();
		double weight = random.nextGaussian();
		int innovationNumber = innovationCalculator.getInnovationNumber(inputNode, outputNode);
		Gene newGene = new Gene(inputNode, outputNode, weight, true, innovationNumber);
		genome.getGenes().add(newGene);
	}

	private void addNewNode(Genome genome) {
		List<Gene> genes = genome.getGenes();
		List<Node> nodes = genome.getNodes();
		if (genes.isEmpty()) {
			return;
		}

		// Select a random gene for modification and disable it
		Gene geneToSplit = genes.get(random.nextInt(genes.size()));
		geneToSplit.setEnabled(false);

		// Create a new node
		int newNodeId = nodes.size() + 1;
		Node newNode = new Node(newNodeId, Node.NodeType.HIDDEN);
		nodes.add(newNode);

		// Create 2 new genes & add them to the genome:
		// newGene1 is a connection from the old inputNode to the new Hidden node.
		// newGene2 is a connection from the new Hidden node to the old outputNode
		int inputNode = geneToSplit.getInputNode();
		int outputNode = newNodeId;
		double weight = 1.0;
		int innovationNumber = innovationCalculator.getInnovationNumber(inputNode, outputNode);
		Gene newGene1 = new Gene(inputNode, outputNode, weight, true, innovationNumber);
		genes.add(newGene1);

		inputNode = newNodeId;
		outputNode = geneToSplit.getOutputNode();
		weight = geneToSplit.getWeight();
		innovationNumber = innovationCalculator.getInnovationNumber(inputNode, outputNode);
		Gene newGene2 = new Gene(inputNode, outputNode, weight, true, innovationNumber);
		genes.add(newGene2);
	}
}
