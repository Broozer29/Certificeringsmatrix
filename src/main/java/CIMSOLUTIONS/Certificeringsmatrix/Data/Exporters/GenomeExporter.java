package CIMSOLUTIONS.Certificeringsmatrix.Data.Exporters;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;

/*- This class is responsible for exporting a Genome to a file
 * 
 */
public class GenomeExporter {

	private static GenomeExporter instance = new GenomeExporter();
	private GenomeExporter() {
	}

	public static GenomeExporter getInstance() {
		return instance;
	}

	//Writes the Genome as a serialized object to a file
	public void exportGenome(Genome genome, String filename) throws IOException {
		try (FileOutputStream fileOut = new FileOutputStream(filename);
				ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

			System.out.println("      >Writing biased words of the following size: " + genome.getBiasedWords().size());
			System.out.println("      >Total bonus points of: " + genome.getAverageBonusPoints());
			objectOut.writeObject(genome);
			System.out.println("      >Genome object serialized and saved to " + filename);

		} catch (IOException e) {
			throw new IOException("Error writing Genome to file", e);
		}

		String GenomeWordScores = "Best Performing Genome Word Scores.txt";
		try (FileWriter writer = new FileWriter(GenomeWordScores)) {
			for (Map.Entry<String, Double> entry : genome.getScores().entrySet()) {
				if (entry.getValue() != 0.0) {
					writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
				}
			}

		} catch (IOException e) {
			System.out.println("An error occurred while writing to " + GenomeWordScores);
			e.printStackTrace();
		}
	}
}
