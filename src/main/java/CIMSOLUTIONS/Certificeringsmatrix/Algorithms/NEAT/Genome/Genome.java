package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.util.ArrayList;
import java.util.List;

public class Genome {

	List<Gene> genes = new ArrayList<Gene>();
	List<Node> nodes = new ArrayList<Node>();

	public Genome() {

	}

	public void addGene(Gene gene) {
		if (!this.genes.contains(gene)) {
			this.genes.add(gene);
		}
	}
	
	public void addNode(Node node) {
		if(!this.nodes.contains(node)) {
			this.nodes.add(node);
		}
	}
	
	
}
