package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Gene;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Node;

/*- This class is responsible for crossing genomes
 */

public class Crossoverseer {

	private static Crossoverseer instance = new Crossoverseer();

	private Crossoverseer() {
	}

	public static Crossoverseer getInstance() {
		return instance;
	}

	
	public Genome crossover(Genome parent1, Genome parent2) {
	    // Make sure parent1 is the fitter parent
	    if (parent2.getFitness() > parent1.getFitness()) {
	        Genome temp = parent1;
	        parent1 = parent2;
	        parent2 = temp;
	    }

	    // Create a new child genome
	    Genome child = new Genome();
	    Map<Integer, Node> childNodesMap = new HashMap<>();

	    // Iterate through the parent1's nodes and inherit them
	    for (Node node : parent1.getNodes()) {
	        Node childNode = new Node(node.getId(), node.getType());
	        childNodesMap.put(childNode.getId(), childNode);
	    }

	    // Iterate through the parent1's and parent2's genes
	    int index1 = 0, index2 = 0;
	    while (index1 < parent1.getGenes().size() || index2 < parent2.getGenes().size()) {
	        Gene gene1 = index1 < parent1.getGenes().size() ? parent1.getGenes().get(index1) : null;
	        Gene gene2 = index2 < parent2.getGenes().size() ? parent2.getGenes().get(index2) : null;

	        if (gene1 == null) {
	            break;
	        } else if (gene2 == null || gene1.getInnovationNumber() < gene2.getInnovationNumber()) {
	            // Inherit gene1
	            child.getGenes().add(new Gene(gene1));
	            index1++;
	        } else if (gene1.getInnovationNumber() > gene2.getInnovationNumber()) {
	            // Inherit gene2 if parent2 is also fit
	            if (parent1.getFitness() == parent2.getFitness()) {
	                child.getGenes().add(new Gene(gene2));
	            }
	            index2++;
	        } else {
	            // Inherit either gene1 or gene2 randomly if they have the same innovation number
	            if (Math.random() < 0.5) {
	                child.getGenes().add(new Gene(gene1));
	            } else {
	                child.getGenes().add(new Gene(gene2));
	            }
	            index1++;
	            index2++;
	        }
	    }

	    // Set the child's nodes
	    
	    List<Node> nodesList = new ArrayList<>(childNodesMap.values());
	    for(Node node : nodesList) {
	    	child.addNode(node);
	    }

	    return child;
	}
}
