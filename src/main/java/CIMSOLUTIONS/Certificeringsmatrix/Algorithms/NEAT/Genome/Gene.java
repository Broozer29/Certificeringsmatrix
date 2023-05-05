package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.io.Serializable;

public class Gene implements Serializable {

	private static final long serialVersionUID = 1L;
	/*- Serializable requires a versionID to check wether or not the object is compatible with current code
	 *  If this class gets changed and is not compatible with exported Genomes & Genes, then this ID must be updated
	 */

	private int inputNode;
	private int outputNode;
	private double weight;
	private boolean isEnabled;
	private int innovationNumber;

	public Gene(int inputNode, int outputNode, double weight, boolean isEnabled, int innovationNumber) {
		this.inputNode = inputNode;
		this.outputNode = outputNode;
		this.weight = weight;
		this.isEnabled = isEnabled;
		this.innovationNumber = innovationNumber;
	}

	public Gene(Gene gene) {
		this.inputNode = gene.getInputNode();
		this.outputNode = gene.getOutputNode();
		this.weight = gene.getWeight();
		this.isEnabled = gene.isEnabled();
		this.innovationNumber = gene.getInnovationNumber();
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

	// Since we don't want to decrease the scores of biased words, I enforced them to
	// always be positive. Allowing decreases in scores might help the algorithm provide
	// fitter Genomes. Requires testing
//	public double getWeight() {
//		return Math.abs(weight);
//	}

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

	public int getInnovationNumber() {
		return innovationNumber;
	}

	public void setInnovationNumber(int innovation) {
		this.innovationNumber = innovation;
	}

}
