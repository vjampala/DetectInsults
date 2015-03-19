import java.util.List;

import ca.uottawa.balie.CharacterNGram;


public class CharGram {
	private static final double A_WEIGHT = -1; // TODO
	private static final double B_WEIGHT = -1; // TODO
	private static final double C_WEIGHT = -1; // TODO
	
	private CharacterNGram aGram;
	private CharacterNGram bGram;
	private CharacterNGram cGram;
	
	
	public CharGram(int a, int b, int c,  List<String> data) {
		aGram = new CharacterNGram(a);
		bGram = new CharacterNGram(b);
		cGram = new CharacterNGram(c);
		
		for(String s: data) {
			aGram.Feed(s);
			bGram.Feed(s);
			cGram.Feed(s);
		}
	}
	
	public double getLogProbabilityOfInsult(String test, double weight) {
		// TODO
		return 0;
	}
}
