package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations.InnovationNumberCalculator;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.Sorting.LinkedHashMapSorter;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Competence;

public class Genome implements Serializable {

	private static final long serialVersionUID = 1L;
	/*- Serializable requires a versionID to check wether or not the object is compatible with current code
	 *  If this class gets changed and is not compatible with exported Genomes, then this ID must be updated
	 */

	private List<Gene> genes = new ArrayList<Gene>();
	private List<Node> inputNodes = new ArrayList<Node>();
	private List<Node> outputNodes = new ArrayList<Node>();
	private List<Node> hiddenNodes = new ArrayList<Node>();
	private LinkedHashMap<String, Double> scores = new LinkedHashMap<String, Double>();
	private Map<String, Double> adjustments = new HashMap<>();
	private List<String> biasedWords = new ArrayList<String>();
	private double fitness;

	public Genome() {
	}

	/*- Create a Genome with defined inputs & outputs. Required for the random initial population */
	public Genome(int inputSize, int outputSize, List<String> words, List<String> biasedWords) {
		this.inputNodes = new ArrayList<Node>();
		this.outputNodes = new ArrayList<Node>();
		this.genes = new ArrayList<Gene>();
		this.biasedWords = biasedWords;
		InnovationNumberCalculator innovationCalculator = InnovationNumberCalculator.getInstance();

		// Create & add input nodes
		for (int i = 0; i < inputSize; i++) {
			inputNodes.add(new Node(i + 1, Node.NodeType.INPUT, words.get(i)));
		}

		// Create & add output nodes
		for (int i = 0; i < outputSize; i++) {
			outputNodes.add(new Node(i + 1, Node.NodeType.OUTPUT));
		}

		// Create connections between ALL input and ALL output nodes
		for (int i = 1; i <= inputSize; i++) {
			for (int j = 1; j <= outputSize; j++) {
				int innovationNumber = innovationCalculator.getInnovationNumber(i, j);
				double randomWeight = Math.random() * 2 - 1;
				genes.add(new Gene(i, j, randomWeight, true, innovationNumber));
			}
		}
	}

	/*- Create a copy of an existing Genome, required for situations where offspring is attempted to be made, but no second Genome is found*/
	public Genome(Genome genome) {
		this.inputNodes = genome.getInputNodes();
		this.outputNodes = genome.getOutputNodes();
		this.hiddenNodes = genome.getHiddenNodes();
		this.genes = genome.getGenes();
		this.adjustments = genome.getAdjustments();
		this.fitness = genome.getFitness();
		this.scores = genome.getScores();
		this.biasedWords = genome.getBiasedWords();
	}


	/*- Transforms X amount of highest scoring words of the Genome into competences and return them */
	public List<Competence> createCompetences(int maximumAmount) {
		scores = LinkedHashMapSorter.getInstance().sortByValueDescending(scores);
		List<Competence> competences = new ArrayList<Competence>();
		int index = 0;
		for (Map.Entry<String, Double> entry : scores.entrySet()) {
			if (index >= maximumAmount) {
				break;
			}
			Competence newComp = new Competence(entry.getKey(), entry.getValue());
			competences.add(newComp);
			index++;
		}

		return competences;
	}

	/*- Used to determine wether the connection between 2 nodes already exists within the Genome */
	public boolean hasConnection(int inputNodeId, int outputNodeId) {
		for (Gene gene : genes) {
			if (gene.getInputNode() == inputNodeId && gene.getOutputNode() == outputNodeId) {
				return true;
			}
		}
		return false;
	}

	/*- This method is responsible for changing the scores of words provided by the IF-TDF algorithm
	 * 	Essentially, this method is where the mutations of the Genome are applied to the wordScores before being graded in the 
	 *  Fitness calculator
	 */
	public LinkedHashMap<String, Double> adjustWordScores(LinkedHashMap<String, Double> wordScores,
			List<String> biasedWords) {
		LinkedHashMap<String, Double> adjustedScores = new LinkedHashMap<>();

		for (Map.Entry<String, Double> entry : wordScores.entrySet()) {
			String word = entry.getKey();
			double originalScore = entry.getValue();
			double bonus = 0;

			if (biasedWords.contains(word)) {
				for (Gene gene : genes) {
					int inputNodeId = gene.getInputNode();
					int outputNodeId = gene.getOutputNode();

					Node inputNode = inputNodes.get(inputNodeId - 1);
					Node outputNode = outputNodes.get(outputNodeId - 1);
					if (inputNode.getType() != Node.NodeType.HIDDEN) {
						if (inputNode.getWord().equals(word) && outputNode.getType() == Node.NodeType.OUTPUT) {
							bonus += gene.getWeight();
						}
					}
				}
			}
			adjustedScores.put(word, originalScore + bonus);
		}

		this.scores = LinkedHashMapSorter.getInstance().sortByValueDescending(adjustedScores);

		return scores;
	}

	public List<String> getBiasedWords() {
		return this.biasedWords;
	}

	public void setScores(LinkedHashMap<String, Double> scores) {
		this.scores = scores;
	}

	public void addGene(Gene gene) {
		if (!this.genes.contains(gene)) {
			this.genes.add(gene);
		}
	}

	public void addOutputNode(Node node) {
		if (!this.outputNodes.contains(node)) {
			this.outputNodes.add(node);
		}
	}

	public void addInputNode(Node node) {
		if (!this.inputNodes.contains(node)) {
			this.inputNodes.add(node);
		}
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness2) {
		this.fitness = fitness2;
	}

	public List<Node> getInputNodes() {
		return this.inputNodes;
	}

	public List<Node> getOutputNodes() {
		return this.outputNodes;
	}

	public List<Gene> getGenes() {
		return genes;
	}

	public Map<String, Double> getAdjustments() {
		return this.adjustments;
	}

	public LinkedHashMap<String, Double> getScores() {
		return scores;
	}

	public void setAdjustment(String word, double adjustment) {
		adjustments.put(word, adjustment);
	}

	public Double getAdjustment(String word) {
		return adjustments.getOrDefault(word, 0.0);
	}

	public List<Node> getHiddenNodes() {
		return hiddenNodes;
	}

	public void addHiddenNode(Node node) {
		if (!this.hiddenNodes.contains(node)) {
			this.hiddenNodes.add(node);
		}
	}

}
