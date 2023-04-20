package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.HashMap;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.ConnectionPair;

public class InnovationNumberCalculator {
	private int nextInnovationNumber;
	private Map<ConnectionPair, Integer> geneInnovations;
	private static InnovationNumberCalculator instance = new InnovationNumberCalculator();

	private InnovationNumberCalculator() {
		nextInnovationNumber = 1;
		geneInnovations = new HashMap<>();
	}

	public static InnovationNumberCalculator getInstance() {
		return instance;
	}

    public int getInnovationNumber(int inputNodeId, int outputNodeId) {
        ConnectionPair connection = new ConnectionPair(inputNodeId, outputNodeId);
        Integer innovationNumber = geneInnovations.get(connection);

        if (innovationNumber == null) {
            innovationNumber = nextInnovationNumber++;
            geneInnovations.put(connection, innovationNumber);
        }

        return innovationNumber;
    }


}