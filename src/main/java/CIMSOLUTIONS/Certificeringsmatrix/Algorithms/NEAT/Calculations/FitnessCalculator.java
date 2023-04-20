package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.Random;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;

/*- This class is responsible for calculating the fitness of a genome
 * 
 */
public class FitnessCalculator {

	public double evaluate(Genome genome) {
        Random random = new Random();
        double min = 0.1;
        double max = 20.0;
        double randomDouble = min + (max - min) * random.nextDouble();
		return randomDouble;
	}
}
