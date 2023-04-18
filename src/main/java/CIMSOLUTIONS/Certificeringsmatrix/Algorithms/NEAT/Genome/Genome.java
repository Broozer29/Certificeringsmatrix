package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Genome {

	private List<Gene> genes = new ArrayList<Gene>();
	private List<Node> nodes = new ArrayList<Node>();
	
	private int fitness;

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
	
	public int getFitness() {
		return fitness;
	}
	
	public List<Node> getNodes(){
		return this.nodes;
	}

	public List<Gene> getGenes() {
		return genes;
	}
	
}
