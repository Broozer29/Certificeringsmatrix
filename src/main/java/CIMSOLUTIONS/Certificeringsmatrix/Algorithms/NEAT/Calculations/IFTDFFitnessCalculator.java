package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;

public class IFTDFFitnessCalculator {
	private LinkedHashMap<String, Double> wordScores;
	private List<String> biasedWords;
	private int wordsForFitnessCalculationAmount;

	public IFTDFFitnessCalculator(LinkedHashMap<String, Double> wordScores, List<String> biasedWords,
			int wordsForFitnessCalculationCount) {
		this.wordScores = wordScores;
		this.biasedWords = biasedWords;
		this.wordsForFitnessCalculationAmount = wordsForFitnessCalculationCount;
	}

	/*- This method calculates the fitness of a Genome based on the following:
	 *		The "wordsForFitnessCalculationCount" amount of words that are Biased Words. (75% or higher gets a greatly increased score)
	 *			Note: The above criteria makes the assumption the words and scores are sorted in descending order. 
	 *		The amount of bonus points added to a Biased word. (The lower the amount of added bonus points, the better)
	 */		
	public double calculateFitness(Genome genome) {
		// Adjust the wordscores in a Genome
		// The wordscores MUST be ordered in descending order. This happens in
		// adjustWordScores.
		LinkedHashMap<String, Double> adjustedWordScores = genome.adjustWordScores(wordScores, biasedWords);
		// Select the X amount of highest scoring words and use those for fitness calculation
		LinkedHashMap<String, Double> topAdjustedWordScores = new LinkedHashMap<>();
		int count = 0;
		for (Map.Entry<String, Double> entry : adjustedWordScores.entrySet()) {
			if (count >= wordsForFitnessCalculationAmount) {
				break;
			}

			topAdjustedWordScores.put(entry.getKey(), entry.getValue());
			count++;
		}
		// Calculate the percentage of words from biasedWords present in topAdjustedWordScores

		int matchingWords = 0;
		for (String word : biasedWords) {
			if (topAdjustedWordScores.containsKey(word)) {
				matchingWords++;
			}
		}

		double matchPercentage = (double) matchingWords / biasedWords.size();

		// Calculate the total bonus points added
		double totalBonusPoints = 0;
		for (Map.Entry<String, Double> entry : topAdjustedWordScores.entrySet()) {
			String word = entry.getKey();
			double adjustedScore = entry.getValue();
			double originalScore = wordScores.get(word);
			if (biasedWords.contains(word)) {
				totalBonusPoints += (adjustedScore - originalScore);
			}
		}

		// Calculate the fitness score based on the given criteria
		// Note: the fitness calculation is completely arbitrary. However, as long as there is
		// a difference between
		// genomes that have a higher and lower than 75% match score, the algorithm will work.
		double baseFitness = 1 / (1 + totalBonusPoints);

		if (matchPercentage >= 0.75) {
			return baseFitness;
		} else {
			return matchPercentage * baseFitness;
		}
	}
}