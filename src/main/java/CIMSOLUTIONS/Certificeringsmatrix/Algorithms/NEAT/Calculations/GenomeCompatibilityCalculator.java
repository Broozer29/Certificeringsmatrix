package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.List;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Gene;
import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;

/*- This class calculates the compatability between genomes, or in other words, calculates how similar they are
 * 	The calculator has 3 importance parameters. These are parameters that can be tuned during the learning process.
 */
public class GenomeCompatibilityCalculator {
	private double excessGeneImportance;
	private double disjointGeneImportance;
	private double weightImportance;

	public GenomeCompatibilityCalculator(double c1, double c2, double c3) {
		this.excessGeneImportance = c1;
		this.disjointGeneImportance = c2;
		this.weightImportance = c3;
	}

	public double calculateCompatibilityDistance(Genome genome1, Genome genome2) {
		// Get all genes belonging to the genomes
		List<Gene> genes1 = genome1.getGenes();
		List<Gene> genes2 = genome2.getGenes();

		int disjointCount = 0;
		int excessCount = 0;
		int matchingCount = 0;
		double weightDifferenceSum = 0;
		int gene1Index = 0;
		int gene2Index = 0;

		// Iterate over both genes simultaneously
		while (gene1Index < genes1.size() && gene2Index < genes2.size()) {
			Gene gene1 = genes1.get(gene1Index);
			Gene gene2 = genes2.get(gene2Index);

			// Count the amount of connections BOTH genes have
			if (gene1.getInnovationNumber() == gene2.getInnovationNumber()) {
				matchingCount++;
				weightDifferenceSum += Math.abs(gene1.getWeight() - gene2.getWeight());
				gene1Index++;
				gene2Index++;
				// If gene2 has a higher innovation number, increase the disjount
			} else if (gene1.getInnovationNumber() < gene2.getInnovationNumber()) {
				disjointCount++;
				// Increase the counter of gene1 so the current innovation iterator stays aligned with
				// the other gene.
				gene1Index++;
			}
			// If gene1 has a higher innovation number, increase the disjoint
			else {
				disjointCount++;
				// Increase the counter of gene2 so the current innovation iterator stays aligned with
				// the other gene.
				gene2Index++;
			}
		}

		// Calculate the remaining genes from both lists that weren't iterated, these are
		// excess genes by default.
		excessCount = (genes1.size() - gene1Index) + (genes2.size() - gene2Index);
		double averageWeightDifference = 0;
		if (matchingCount > 0) {
			averageWeightDifference = weightDifferenceSum / matchingCount;
		}

		// I don't fully understand the math behind the normalizationFactor, but it helps the
		// NEAT algorithm recognize similar genomes despite having different sizes, so it is implemented

		int normalizationFactor = Math.max(genes1.size(), genes2.size());
		// If n is 0, use 1 instead, this is needed to prevent a division of 0
		normalizationFactor = Math.max(normalizationFactor, 1);

		return excessGeneImportance * excessCount / normalizationFactor
				+ disjointGeneImportance * disjointCount / normalizationFactor
				+ weightImportance * averageWeightDifference;
	}
}
