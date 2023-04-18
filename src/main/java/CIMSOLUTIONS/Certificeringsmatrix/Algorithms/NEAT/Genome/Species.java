package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome;

import java.util.ArrayList;
import java.util.List;

public class Species {
    List<Genome> genomes = new ArrayList<Genome>();
    
    public Species() {
    }
    
    public void addGenome(Genome genome) {
    	if(!genomes.contains(genome)) {
    		genomes.add(genome);
    	}
    }
    
    public List<Genome> getGenomes(){
    	return genomes;
    }
    
}
