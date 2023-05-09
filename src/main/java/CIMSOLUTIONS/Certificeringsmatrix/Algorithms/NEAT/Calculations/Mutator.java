package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Gene;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Node;

/*- This class is responsible for creating random mutations on Genomes
 * 
 */
public class Mutator {
	private Random random = new Random();
	private InnovationNumberCalculator innovationCalculator = InnovationNumberCalculator.getInstance();

	// Chances of this type of mutations to occur
	private double weightMutationRate;
	private double newConnectionMutationRate;
	private double newNodeMutationRate;

	// The size of the change in weight. Increasing or decreasing this modifies the size of
	// the change applied by weight mutation
	private double mutationStrength;

	public Mutator(double weightMutationRate, double newConnectionMutationRate, double newNodeMutationRate, double mutationStrength) {
		this.weightMutationRate = weightMutationRate;
		this.newConnectionMutationRate = newConnectionMutationRate;
		this.newNodeMutationRate = newNodeMutationRate;
		this.mutationStrength = mutationStrength;
	}

	/*- Select a random mutation, or no mutation at all.
	 * It is possible for all mutations to occur at once, this enables the possibility for large mutations in one generation
	 */
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

	// Calculate the amount by which a weight should be changed.
	// The higher the mutationStrength, the larger the changes are
	private void mutateWeights(Genome genome) {
		List<Gene> genes = genome.getGenes();
		for (Gene gene : genes) {
			if (random.nextBoolean()) {
				double weight = gene.getWeight();
				// Gaussian is used because this appears to be preferred in A.I.
				// What specific perks it brings I don't know, but it is recommended so I'll use it
				double mutation = random.nextGaussian() * mutationStrength;
				gene.setWeight(weight + mutation);
			}
		}
	}

	   // Adds a new connection between 2 existing nodes
    private void addNewConnection(Genome genome) {
        List<Node> allNodes = new ArrayList<>();
        allNodes.addAll(genome.getInputNodes());
        allNodes.addAll(genome.getOutputNodes());
        allNodes.addAll(genome.getHiddenNodes());

        Node node1 = null;
        Node node2 = null;
		boolean possibleConnectionFound = false;

        // Continue to search for a valid connection until one is found
        while (!possibleConnectionFound) {
            node1 = allNodes.get(random.nextInt(allNodes.size()));
            node2 = allNodes.get(random.nextInt(allNodes.size()));

            // Check if the nodes are not the same and if the connection doesn't already exist
            if (node1.getId() != node2.getId() && !genome.hasConnection(node1.getId(), node2.getId())) {
                possibleConnectionFound = true;
            }
        }

        // If the selected nodes are hidden and input, swap them to maintain the proper connection direction
        if (node1.getType() == Node.NodeType.HIDDEN && node2.getType() == Node.NodeType.INPUT) {
            Node temp = node1;
            node1 = node2;
            node2 = temp;
        }

        int inputNode = node1.getId();
        int outputNode = node2.getId();
        double weight = random.nextGaussian(); /*- Generate a random weight using Gaussian distribution, this is to get a proper distribution of random doubles*/
        int innovationNumber = innovationCalculator.getInnovationNumber(inputNode, outputNode);
        Gene newGene = new Gene(inputNode, outputNode, weight, true, innovationNumber);
        genome.addGene(newGene);
    }

	// Creates a new node inbetween existing nodes
//    private void addNewNode(Genome genome) {
//        List<Gene> genes = genome.getGenes();
//        if (genes.isEmpty()) {
//            return;
//        }
//
//        //Select a random Gene to split and disable it
//        Gene geneToSplit = genes.get(random.nextInt(genes.size()));
//        geneToSplit.setEnabled(false);
//
//        //Create a new hidden node
//        int newInputNodeID = genome.getInputNodes().size() + 1;
//        Node newNode = new Node(newInputNodeID, Node.NodeType.HIDDEN);
//        genome.addHiddenNode(newNode);
//
//        //Create new connection Genes that go from and to the new Node
//        int inputNode = geneToSplit.getInputNode();
//        int outputNode = geneToSplit.getOutputNode();
//        double weight = 1.0;
//        int innovationNumber = innovationCalculator.getInnovationNumber(inputNode, newInputNodeID);
//        Gene newGene1 = new Gene(inputNode, newInputNodeID, weight, true, innovationNumber);
//        genes.add(newGene1);
//
//        inputNode = newInputNodeID;
//        weight = geneToSplit.getWeight();
//        innovationNumber = innovationCalculator.getInnovationNumber(inputNode, outputNode);
//        Gene newGene2 = new Gene(inputNode, outputNode, weight, true, innovationNumber);
//        genes.add(newGene2);
//    }
    
    private void addNewNode(Genome genome) {
		List<Gene> genes = genome.getGenes();
		List<Node> inputNodes = genome.getInputNodes();
		List<Node> outputNodes = genome.getOutputNodes();
		if (genes.isEmpty()) {
			return;
		}

		// Select a random gene for modification and disable it
		Gene geneToSplit = genes.get(random.nextInt(genes.size()));
		geneToSplit.setEnabled(false);

		// Create a new node
		int newInputNodeID = inputNodes.size() + 1;
		Node newNode = new Node(newInputNodeID, Node.NodeType.HIDDEN);
		inputNodes.add(newNode);

		// Create 2 new genes & add them to the genome:
		// newGene1 is a connection from the old inputNode to the new Hidden node.
		// newGene2 is a connection from the new Hidden node to the old outputNode
		int inputNode = geneToSplit.getInputNode();
		int outputNode = outputNodes.size();
		double weight = 1.0;
		int innovationNumber = innovationCalculator.getInnovationNumber(inputNode, outputNode);
		Gene newGene1 = new Gene(inputNode, outputNode, weight, true, innovationNumber);
		genes.add(newGene1);

		inputNode = newInputNodeID;
		outputNode = geneToSplit.getOutputNode();
		weight = geneToSplit.getWeight();
		innovationNumber = innovationCalculator.getInnovationNumber(inputNode, outputNode);
		Gene newGene2 = new Gene(inputNode, outputNode, weight, true, innovationNumber);
		genes.add(newGene2);
	}
}
