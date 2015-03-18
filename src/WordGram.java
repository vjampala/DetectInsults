import java.util.ArrayList;
import java.util.List;

import hultig.sumo.HNgram;

public class WordGram {
	private HNgram model;
	
	public WordGram(int n, String filename) {
		model = new HNgram(filename, n);

	}
	
	public List<Double> getLogProbabilityOfInsult(List<String> test, double weight) {
		List<Double> results = new ArrayList<Double>();
		for(int i = 0; i < test.size(); i++) {
			results.add(weight * model.probabilidade(test.get(i)));
		}
		return results;
	}
	
	public double getLogProbabilityOfInsult(String test, double weight) {
		return weight * model.probabilidade(test);
	}

}
