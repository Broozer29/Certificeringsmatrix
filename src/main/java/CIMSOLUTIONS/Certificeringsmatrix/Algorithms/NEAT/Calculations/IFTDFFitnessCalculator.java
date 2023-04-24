//package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Calculations;
//
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import CIMSOLUTIONS.Certificeringsmatrix.Algorithms.NEAT.Genome.Genome;
//
//public class IFTDFFitnessCalculator implements FitnessCalculator {
//    private LinkedHashMap<String, Double> sortedWordScores;
//    private List<String> biasedWords;
//
//    public IFTDFFitnessCalculator(LinkedHashMap<String, Double> sortedWordScores, List<String> biasedWords) {
//        this.sortedWordScores = sortedWordScores;
//        this.biasedWords = biasedWords;
//    }
//
//    @Override
//    public double evaluate(Genome genome) {
//        // Apply the Genome to adjust the word scores based on BiasedWords
//        LinkedHashMap<String, Double> adjustedWordScores = genome.adjustWordScores(sortedWordScores, biasedWords);
//
//        // Select the top 30 adjusted word scores
//        LinkedHashMap<String, Double> adjustedTop30Scores = adjustedWordScores.entrySet().stream()
//                .limit(30)
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//
//        // Calculate the fitness score
//        int biasedWordCount = 0;
//        double totalScoreIncrease = 0;
//
//        for (String word : adjustedTop30Scores.keySet()) {
//            if (biasedWords.contains(word)) {
//                biasedWordCount++;
//                totalScoreIncrease += adjustedTop30Scores.get(word) - sortedWordScores.get(word);
//            }
//        }
//
//        double matchPercentage = (double) biasedWordCount / biasedWords.size() * 100;
//
//        if (matchPercentage >= 75) {
//            // Fitness is higher for lower total score increases
//            return 1 / (1 + totalScoreIncrease);
//        } else {
//            // Genome failed to achieve the minimum match percentage
//            return 0;
//        }
//    }
//}
