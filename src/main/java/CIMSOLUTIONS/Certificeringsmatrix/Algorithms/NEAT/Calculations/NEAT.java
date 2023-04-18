package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.ArrayList;
import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Species;

public class NEAT {
	private List<Genome> population = new ArrayList<Genome>();
	private List<Species> species = new ArrayList<Species>();
	
	private static NEAT instance = new NEAT();
	
	private NEAT() {
	}
	
	public static NEAT getInstance() {
		return instance;
	}
	
	
	
}
