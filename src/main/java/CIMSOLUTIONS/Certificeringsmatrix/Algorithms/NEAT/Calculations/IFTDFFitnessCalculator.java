package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;

public class IFTDFFitnessCalculator {
    private LinkedHashMap<String, Double> wordScores;
    private List<String> biasedWords;

    public IFTDFFitnessCalculator(LinkedHashMap<String, Double> wordScores, List<String> biasedWords) {
        this.wordScores = wordScores;
        this.biasedWords = biasedWords;
    }

    public double calculateFitness(Genome genome) {
        LinkedHashMap<String, Double> adjustedWordScores = genome.adjustWordScores(wordScores, biasedWords);

        // Sort by adjusted scores and keep only top 30 entries
        LinkedHashMap<String, Double> topAdjustedWordScores = adjustedWordScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(30)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        // Calculate the percentage of words from biasedWords present in topAdjustedWordScores
        int matchingWords = 0;
        for (String word : biasedWords) {
            if (topAdjustedWordScores.containsKey(word)) {
                matchingWords++;
            }
        }

        double matchPercentage = (double) matchingWords / biasedWords.size();
//        System.out.println("Match percentage: " + matchPercentage);
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

        System.out.println(matchPercentage);
        // Calculate the fitness score based on the given criteria
        if (matchPercentage >= 0.75) {
            return 1 / (1 + totalBonusPoints);
        } else {
            return 0;
        }
    }
}