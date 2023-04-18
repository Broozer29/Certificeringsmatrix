package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

public class Gene {
	int inputNode;
	int outputNode;
	double weight;
	boolean isEnabled;
	int innovationNumber;

	public Gene() {
		
	}
	
	public Gene(int inputNode, int outputNode, double weight, boolean isEnabled, int innovationNumber) {
		this.inputNode = inputNode;
		this.outputNode = outputNode;
		this.weight = weight;
		this.isEnabled = isEnabled;
		this.innovationNumber = innovationNumber;
	}
	
	public int getInputNode() {
		return inputNode;
	}

	public void setInputNode(int inputNode) {
		this.inputNode = inputNode;
	}

	public int getOutputNode() {
		return outputNode;
	}

	public void setOutputNode(int outputNode) {
		this.outputNode = outputNode;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public int getInnovation() {
		return innovationNumber;
	}

	public void setInnovation(int innovation) {
		this.innovationNumber = innovation;
	}

}
