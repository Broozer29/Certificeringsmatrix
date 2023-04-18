package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

public class Node {

	public enum NodeType {
		INPUT, HIDDEN, OUTPUT;
	}

	int id;
	NodeType type;
	double value;

	public Node() {

	}

	public Node(int id, NodeType type, double value) {
		this.id = id;
		this.type = type;
		this.value = value;
	}

	public Node(int id, NodeType type) {
		this.id = id;
		this.type = type;
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

}
