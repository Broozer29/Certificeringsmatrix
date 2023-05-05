package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.io.Serializable;

public class Node implements Serializable{

	/*- Serializable requires a versionID to check wether or not the object is compatible with current code
	 *  If this class gets changed and is not compatible with exported Genomes & Nodes, then this ID must be updated
	 */
	private static final long serialVersionUID = 1L;

	public enum NodeType {
		INPUT, HIDDEN, OUTPUT;
	}

	private String word;

	int id;
	NodeType type;
	double value;

	public Node() {

	}

	// For the creation of output nodes and hidden nodes
	public Node(int id, NodeType type) {
		this.id = id;
		this.type = type;
	}

	// For the creation of input nodes
	public Node(int id, NodeType type, String word) {
		this.id = id;
		this.type = type;
		this.word = word;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getWord() {
		return this.word;
	}

}
