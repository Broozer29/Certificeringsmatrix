package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;

public class TFIDFFitnessCalculator {
	private LinkedHashMap<String, Double> wordScores;
	private List<String> biasedWords;
	private int wordsForFitnessCalculationAmount;

	public TFIDFFitnessCalculator(LinkedHashMap<String, Double> wordScores, List<String> biasedWords,
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

	    // Calculate the fitness score based on the given criteria
	    double baseFitness = 1 / (1 + totalBonusPoints);

	    if (matchPercentage >= 0.75) {
	        return baseFitness;
	    } else {
	        return matchPercentage * baseFitness;
	    }
	}
}