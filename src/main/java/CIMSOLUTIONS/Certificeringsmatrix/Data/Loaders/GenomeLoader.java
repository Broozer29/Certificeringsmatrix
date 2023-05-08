package CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders;

/*- This class is responsible for importing an existing Genome
 * 
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;

public class GenomeLoader {
	
	public GenomeLoader() {

	}
	
	public Genome loadBestPerformingGenome(String filename) throws IOException, ClassNotFoundException {
		try (FileInputStream fileIn = new FileInputStream(filename);
				ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

			Genome genome = (Genome) objectIn.readObject();
			return genome;

		} catch (IOException | ClassNotFoundException e) {
			throw new IOException("Error reading Genome from file", e);
		}

	}
}
