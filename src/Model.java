import hultig.sumo.HNgram;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.AttributeSource;

public class Model {
	private static final double UNIGRAM_WEIGHT = 0.33;
	private static final double CHARGRAM_46_WEIGHT = 0.17;
	private static final double CHARGRAM_13_WEIGHT = 0.17;
	private static final double BIGRAM_WEIGHT = 0.17;
	private static final double TRIGRAM_WEIGHT = 0.17;
	
	private static final String UNIGRAM_FILE = ""; // TODO
	private static final String BIGRAM_FILE = ""; // TODO
	private static final String TRIGRAM_FILE = ""; // TODO
	
	private static final String TRAINING_FILE = ""; // TODO
	private static final String TEST_FILE = ""; // TODO
	
	/*
	 * Final result
	 */
	public static void main(String[] args) {
		List<Integer> labels = new ArrayList<Integer>();
		List<String> trainingExamples = readExamples(TRAINING_FILE, null); // TODO
		List<String> testExamples = readExamples(TEST_FILE, labels);
		Stemmer s = new Stemmer();
		//List<Double> scores = computeScores(trainingExamples, testExamples);
		//printAccuracy(scores, labels);
		
		//WordGram unigram = new WordGram(3, "test.txt");
		//System.out.println(unigram.getLogProbabilityOfInsult("n n hi there", 1));
	}
	
	private static List<String> readExamples(String filename, List<Integer> labels) {
		// TODO
		return new ArrayList<String>();
	}
	
	// TODO logs?
	/*private static List<Double> computeScores(List<String> trainingExamples, List<String> testExamples) {
		List<Double> scores = new WordGram(1, UNIGRAM_FILE).getLogProbabilityOfInsult(testExamples, UNIGRAM_WEIGHT);
		WordGram bigram = new WordGram(2, BIGRAM_FILE);
		WordGram trigram = new WordGram(2, TRIGRAM_FILE);
		CharGram gram13 = new CharGram(1, 2, 3, trainingExamples);
		CharGram gram46 = new CharGram(4, 5, 6, trainingExamples);
		int size = testExamples.size();
		for(int i = 0; i < size; i++) {
			scores.set(i, scores.get(i) + bigram.getLogProbabilityOfInsult(testExamples.get(i), BIGRAM_WEIGHT));
		}
		for(int i = 0; i < size; i++) {
			scores.set(i, scores.get(i) + trigram.getLogProbabilityOfInsult(testExamples.get(i), TRIGRAM_WEIGHT));
		}
		for(int i = 0; i < size; i++) {
			scores.set(i, scores.get(i) + gram13.getLogProbabilityOfInsult(testExamples.get(i), CHARGRAM_13_WEIGHT));
		}
		for(int i = 0; i < size; i++) {
			scores.set(i, scores.get(i) + gram46.getLogProbabilityOfInsult(testExamples.get(i), CHARGRAM_46_WEIGHT));
		}
		return scores;
	}*/
	
	private static void printAccuracy(List<Double> scores, List<Integer> labels) {
		double count = 0;
		for(int i = 0; i < scores.size(); i++) {
			if (scores.get(i) >= 0.5 && labels.get(i) == 1) {
				count++;
			}
		}
		double accuracy = ((count * 1.0) / scores.size()) * 100;
		System.out.println("The accuracy of this model is " + accuracy + "%.");
	}

}
