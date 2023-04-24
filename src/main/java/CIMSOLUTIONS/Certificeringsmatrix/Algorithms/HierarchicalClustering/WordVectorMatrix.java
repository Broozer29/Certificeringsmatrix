package CIMSOLUTIONS.Certificeringsmatrix.Algorithms.HierarchicalClustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

import CIMSOLUTIONS.Certificeringsmatrix.Data.Storage.StorageManager;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Competence;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Document;
import CIMSOLUTIONS.Certificeringsmatrix.DomainObjects.Role;

public class WordVectorMatrix {

	private static WordVectorMatrix instance = new WordVectorMatrix();
	private Word2Vec wordVectorMatrix = null;
	//A high similarity means words have to be closely related to each other. Values betweeon 0.0 and 1.0
	private double similarityThreshold = 0.8;

	private WordVectorMatrix() {

	}

	public static WordVectorMatrix getInstance() {
		return instance;
	}

	// Initializes the matrix containing all vectors of all the words from all the
	// Documents.
	public void createWordVectorMatrix() {
		List<Document> allDocuments = StorageManager.getInstance().getAllDocuments();

		List<String> sentences = new ArrayList<>();
		for (Document document : allDocuments) {
			sentences.add(String.join(" ", document.getSentencesWithinDocument()));

		}

		SentenceIterator iter = new CollectionSentenceIterator(sentences);

		TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
		tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

		wordVectorMatrix = new Word2Vec.Builder().minWordFrequency(1).layerSize(100).seed(42).windowSize(5)
				.iterate(iter).tokenizerFactory(tokenizerFactory).build();

		wordVectorMatrix.fit();
	}

	public Collection<String> getNearestWords(String word, int amount) {
		return wordVectorMatrix.wordsNearest(word, amount);
	}

	// This method returns the similarity between 2 strings that contain a single word each
	public Double getSimilarity(String firstWord, String secondWord) {
		return wordVectorMatrix.similarity(firstWord, secondWord);
	}

	// This method returns the similarity between 2 strings that contain more than 1 word
	public Double getSimilarityOfMultiTermWords(String firstMultiTermWord, String secondMultiTermWord) {
		INDArray multiTermWord1Vectors = getAverageVector(wordVectorMatrix, firstMultiTermWord);
		INDArray multiTermWord2Vectors = getAverageVector(wordVectorMatrix, secondMultiTermWord);

		// The similarity() of Word2Vec doesn't support INDArrays, so the ND4j library is used
		// to calculate the distance between the vectors
		if (multiTermWord1Vectors != null && multiTermWord2Vectors != null) {
			return Transforms.cosineSim(multiTermWord1Vectors, multiTermWord2Vectors);
		} else
			return null;
	}

	// Required for getSimilarityOfDoubleWords.
	// Calculates the average of vectors from the term. For example: add the vectors of
	// "Java" and "Developer" into a single vector.
	private INDArray getAverageVector(Word2Vec word2Vec, String multiWordTerm) {
		String[] words = multiWordTerm.split(" ");
		INDArray[] wordVectors = new INDArray[words.length];

		for (int i = 0; i < words.length; i++) {
			if (word2Vec.hasWord(words[i])) {
				wordVectors[i] = word2Vec.getWordVectorMatrix(words[i]);
			} else {
				return null;
			}
		}

		return Nd4j.mean(Nd4j.vstack(wordVectors), 0);
	}

	//Adds a competence to a role if it meets the similarityThreshold
	public void addCompetencesToRoles(List<Competence> competences, List<Role> roles) {
		for (Competence competence : competences) {
			for (Role role : roles) {
				String[] splittedWords = role.getRole().split("\\s+");
				int wordCount = splittedWords.length;
				if (wordCount > 1) {
					Double score = getSimilarityOfMultiTermWords(role.getRole(), competence.getCompetence());
					if (score > similarityThreshold) {
						role.addCompetence(competence);
					}
				} else {
					Double score = getSimilarity(role.getRole(), competence.getCompetence());
					if (score > similarityThreshold) {
						role.addCompetence(competence);
					}
				}
			}
		}
	}
}
