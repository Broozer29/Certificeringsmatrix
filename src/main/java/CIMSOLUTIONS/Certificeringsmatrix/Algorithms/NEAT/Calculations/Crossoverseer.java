package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Gene;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Node;

/*- This class is responsible for the crossing of 2 Genomes into a singular new Genome
 */

public class Crossoverseer {

	public Crossoverseer() {
	}

	public Genome crossover(Genome parent1, Genome parent2) {
		// Make sure parent1 is the fitter parent, the original NEAT research paper claims this
		// is required.
		if (parent2.getFitness() > parent1.getFitness()) {
			Genome temp = parent1;
			parent1 = parent2;
			parent2 = temp;
		}

		// Create a new child genome
		Genome child = new Genome();
		List<Node> childInputNodes = new ArrayList<Node>();

		// Iterate through the parent1's nodes and inherit them
		for (Node node : parent1.getInputNodes()) {
			Node childNode;
			childNode = new Node(node.getId(), node.getType(), node.getWord());
			childInputNodes.add(childNode);
		}

		// Iterate over the genes of both parents, until all genes are added to the child
		int index1 = 0;
		int index2 = 0;
		while (index1 < parent1.getGenes().size() || index2 < parent2.getGenes().size()) {
			Gene gene1 = null;
			Gene gene2 = null;
			if (index1 < parent1.getGenes().size()) {
				gene1 = parent1.getGenes().get(index1);
			}

			if (index2 < parent2.getGenes().size()) {
				gene2 = parent2.getGenes().get(index2);
			}

			if (gene1 == null) {
				break;
			} else if (gene2 == null || gene1.getInnovationNumber() < gene2.getInnovationNumber()) {
				// Inherit gene1
				child.addGene(gene1);
				index1++;
			} else if (gene1.getInnovationNumber() > gene2.getInnovationNumber()) {
				// Inherit gene2 if parent2 has the exact same fitness.
				if (parent1.getFitness() == parent2.getFitness()) {
					child.addGene(gene2);
				}
				index2++;
			} else {
				// Inherit either gene1 or gene2 randomly if they have the same innovation number
				if (Math.random() < 0.5) {
					child.addGene(gene1);
				} else {
					child.addGene(gene2);
				}
				index1++;
				index2++;
			}
		}

		// Set the input nodes of the child
		for (Node node : childInputNodes) {
			child.addInputNode(node);
		}

		// Copy the output nodes of the fittest parent to the child
		for (Node node : parent1.getOutputNodes()) {
			child.addOutputNode(node);
		}
		// Copy the biased words of the fittest parent, though both should work
		child.setBiasedWords(parent1.getBiasedWords());
		return child;
	}
}
