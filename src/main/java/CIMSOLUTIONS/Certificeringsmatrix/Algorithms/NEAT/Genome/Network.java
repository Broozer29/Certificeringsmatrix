package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.util.ArrayList;
import java.util.List;

/*- This class is the resulting network of inputs, outputs and hidden nodes.
 *  Essentially; this class is the neural network
 */
public class Network {

	List<Node> inputNodes = new ArrayList<Node>();
	List<Node> hiddenNodes = new ArrayList<Node>();
	List<Node> outputNodes = new ArrayList<Node>();

	public Network() {
	}

	public void addInput(Node input) {
		if (!inputNodes.contains(input)) {
			inputNodes.add(input);
		}
	}

	public void addOutput(Node output) {
		if (!outputNodes.contains(output)) {
			outputNodes.add(output);
		}
	}

	public void addHidden(Node hidden) {
		if (!hiddenNodes.contains(hidden)) {
			hiddenNodes.add(hidden);
		}
	}

	public List<Node> getInputNodes() {
		return inputNodes;
	}

	public List<Node> getHiddenNodes() {
		return hiddenNodes;
	}

	public List<Node> getOutputNodes() {
		return outputNodes;
	}
	
}
