package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;

public class GenomeFitnessCalculator {
	private LinkedHashMap<String, Double> wordScores;
	private List<String> biasedWords;
	private int wordsForFitnessCalculationAmount;
	private int bonusPointsWeightSubtractor;

	public GenomeFitnessCalculator(LinkedHashMap<String, Double> wordScores, List<String> biasedWords,
			int wordsForFitnessCalculationCount, int bonusPointsScaler) {
		this.wordScores = wordScores;
		this.biasedWords = biasedWords;
		this.wordsForFitnessCalculationAmount = wordsForFitnessCalculationCount;
		this.bonusPointsWeightSubtractor = bonusPointsScaler;
	}

	/*- This method calculates the fitness of a Genome based on the following:
	 *		The "wordsForFitnessCalculationCount" amount of words in "adjustedWordScores" that are Biased Words. (75% or higher gets a greatly increased score)	
	 *		The amount of bonus points added to a Biased word. (The lower the amount of added bonus points, the higher the score)
	 * NOTE: A Genome CAN recieve a negative score. This happens because it is possible for Genomes to subtract points from words as well. 
	 * 		This behaviour allows Genomes to correct themselves, but also allows negative scores.
	 */
	
	public double calculateFitness(Genome genome) {
	    // Adjust the wordscores in a Genome
	    LinkedHashMap<String, Double> adjustedWordScores = genome.adjustWordScores(wordScores, biasedWords);

	    // Calculate the percentage of words from topAdjustedWordScores present in biasedWords
	    int matchingWords = 0;
	    int totalWords = 0;
	    for (Map.Entry<String, Double> entry : adjustedWordScores.entrySet()) {
	        if (totalWords >= wordsForFitnessCalculationAmount) {
	            break;
	        }

	        if (biasedWords.contains(entry.getKey())) {
	            matchingWords++;
	        }
	        totalWords++;
	    }

	    double matchPercentage = (double) matchingWords / totalWords;
//	    System.out.println("Match percentage :" + matchPercentage + " Matching words: " + matchingWords
//	            + " Total words: " + totalWords);

	    // Calculate the total bonus points added
	    double totalBonusPoints = 0;
	    for (Map.Entry<String, Double> entry : adjustedWordScores.entrySet()) {
	        String word = entry.getKey();
	        double adjustedScore = entry.getValue();
	        double originalScore = wordScores.get(word);
	        if (biasedWords.contains(word)) {
	            totalBonusPoints += (adjustedScore - originalScore);
	        }
	    }

	    //The scaling factor is how much the Genome gets punished for adding unnecesary bonus points
	    
	    double baseFitness = 1 / (1 + Math.abs(totalBonusPoints / bonusPointsWeightSubtractor));

	    double finalFitness;
	    if (matchPercentage >= 0.75) {
	        finalFitness = baseFitness;
	    } else {
	        finalFitness = matchPercentage * baseFitness;
	    }

	    // Ensure the final fitness score is non-negative
	    finalFitness += Math.abs(Math.min(0, finalFitness));

	    return finalFitness;
	}
}