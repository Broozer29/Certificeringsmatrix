package CIMSOLUTIONS.Certificeringsmatrix.Data.Loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BiasedWordsLoader {
	
	private static BiasedWordsLoader instance = new BiasedWordsLoader();
	private List<String> biasedWords = new ArrayList<String>();
	
	private BiasedWordsLoader() {
		
	}
	
	public static BiasedWordsLoader getInstance() {
		return instance;
	}
	
	public List<String> getBiasedWords(){
		return biasedWords;
	}
	
	public void loadBiasedWords(){
		biasedWords = new ArrayList<String>();
        String filePath = "resources/Bias/BiasedWords.txt";
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
            	String[] words = line.split("\n");
                for (String word : words) {
                	String cleanedWord = word.replaceAll("[^a-zA-Z\\s]", "").trim().toLowerCase();;
                	biasedWords.add(cleanedWord);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
	}
}
