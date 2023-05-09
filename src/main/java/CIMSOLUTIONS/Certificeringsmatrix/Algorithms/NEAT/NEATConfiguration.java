package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT;

/*- This class holds all configurations required for creating a NEATInstance. 
 *  ALL these variables MUST be set to a different value in order for the NEAT algorithm to function
 */
public class NEATConfiguration {
	
	private int neuronInputSize = 0; // Number of input neurons (must be equal to the total amount of unique words)
	private int neuronOutputSize = 0; // Number of output neurons
	private int populationSize = 0; // Size of the population
	private int generations = 0; // Number of generations to evolve
	private int speciesSharingThreshold = 0; // Sharing threshold for speciation
	private int topXWordsUsedForFitnessCalculation = 0; // The amount of highest scoring words used in the fitness

	
	private double excessGeneImportance = 0.0;
	private double disjointGeneImportance = 0.0;
	private double weightImportance = 0.0;
	
	private double weightMutationRate = 0.0;
	private double newConnectionMutationRate = 0.00;
	private double newNodeMutationRate = 0.00;
	
	private double mutationStrength = 0.0;
	
	
	public NEATConfiguration() {
		
	}
	
	// Sets default configurations for a NEAT instance
	public void setDefaultNEATConfigurations() {
		setNeuronOutputSize(5);
		setPopulationSize(10);
		setGenerations(5);
		setSpeciesSharingThreshold(3);
		setTopXWordsUsedForFitnessCalculation(30);

		setExcessGeneImportance(1.0);
		setDisjointGeneImportance(0.4);
		setWeightImportance(0.4);

		setWeightMutationRate(0.8);
		setNewConnectionMutationRate(0.2);
		setNewNodeMutationRate(0.2);

		setMutationStrength(1.0);
	}


	public int getNeuronInputSize() {
		return neuronInputSize;
	}


	public void setNeuronInputSize(int neuronInputSize) {
		this.neuronInputSize = neuronInputSize;
	}


	public int getNeuronOutputSize() {
		return neuronOutputSize;
	}


	public void setNeuronOutputSize(int neuronOutputSize) {
		this.neuronOutputSize = neuronOutputSize;
	}


	public int getPopulationSize() {
		return populationSize;
	}


	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}


	public int getGenerations() {
		return generations;
	}


	public void setGenerations(int generations) {
		this.generations = generations;
	}


	public int getSpeciesSharingThreshold() {
		return speciesSharingThreshold;
	}


	public void setSpeciesSharingThreshold(int speciesSharingThreshold) {
		this.speciesSharingThreshold = speciesSharingThreshold;
	}


	public int getTopXWordsUsedForFitnessCalculation() {
		return topXWordsUsedForFitnessCalculation;
	}


	public void setTopXWordsUsedForFitnessCalculation(int topXWordsUsedForFitnessCalculation) {
		this.topXWordsUsedForFitnessCalculation = topXWordsUsedForFitnessCalculation;
	}


	public double getExcessGeneImportance() {
		return excessGeneImportance;
	}


	public void setExcessGeneImportance(double excessGeneImportance) {
		this.excessGeneImportance = excessGeneImportance;
	}


	public double getDisjointGeneImportance() {
		return disjointGeneImportance;
	}


	public void setDisjointGeneImportance(double disjointGeneImportance) {
		this.disjointGeneImportance = disjointGeneImportance;
	}


	public double getWeightImportance() {
		return weightImportance;
	}


	public void setWeightImportance(double weightImportance) {
		this.weightImportance = weightImportance;
	}


	public double getWeightMutationRate() {
		return weightMutationRate;
	}


	public void setWeightMutationRate(double weightMutationRate) {
		this.weightMutationRate = weightMutationRate;
	}


	public double getNewConnectionMutationRate() {
		return newConnectionMutationRate;
	}


	public void setNewConnectionMutationRate(double newConnectionMutationRate) {
		this.newConnectionMutationRate = newConnectionMutationRate;
	}


	public double getNewNodeMutationRate() {
		return newNodeMutationRate;
	}


	public void setNewNodeMutationRate(double newNodeMutationRate) {
		this.newNodeMutationRate = newNodeMutationRate;
	}


	public double getMutationStrength() {
		return mutationStrength;
	}


	public void setMutationStrength(double mutationStrength) {
		this.mutationStrength = mutationStrength;
	}
	
	
	

}
