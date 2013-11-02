/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Stimuli {
	private final int [] riskyProbs = {70, 80, 90, 80, 70, 90};
	private final int [] riskyVals = {1000, 900, 100, 200, 60, 500};
	
	private final int [] certainProbs = {100, 100, 100, 100, 100, 100};
	private final int [] certainVals = {680, 700, 80, 140, 35, 420};
	
	// fields
	private int numTrials;
	
	private ArrayList trials;
	// for backward compatibility with java 1.4.2 -- can't specify type of object in list

	private int currentCount;
	
	// default constructor
	public Stimuli() {};
	
	// constructor
	public Stimuli(int n) {
		numTrials = n;
		
		trials = new ArrayList();
		
		Random randGen = new Random();
		randGen.setSeed(System.currentTimeMillis());
		
		boolean positiveFirst = randGen.nextBoolean();
		
		ArrayList positive = new ArrayList();
		ArrayList negative = new ArrayList();
		
		for (int i=0; i<n; i++){
			boolean isARisky = randGen.nextBoolean();
			if (i < n/2){
				if (isARisky)
					positive.add(new Trial(true, isARisky, riskyProbs[i], riskyVals[i], certainProbs[i], certainVals[i]));
				else
					positive.add(new Trial(true, isARisky, certainProbs[i], certainVals[i], riskyProbs[i], riskyVals[i]));
			} 
			else {
				if (isARisky)
					negative.add(new Trial(false, isARisky, riskyProbs[i-n/2], riskyVals[i-n/2], certainProbs[i-n/2], certainVals[i-n/2]));
				else
					negative.add(new Trial(false, isARisky, certainProbs[i-n/2], certainVals[i-n/2], riskyProbs[i-n/2], riskyVals[i-n/2]));
			}
		}
		
		Collections.shuffle(positive);
		Collections.shuffle(negative);
		
		if (positiveFirst){
			trials.addAll(positive);
			trials.addAll(negative);
		}
		else {
			trials.addAll(negative);
			trials.addAll(positive);
		}
	
		currentCount = 0;
	} // end constructor
	
	// returns next trial
	public Trial nextTrial(){
		if (currentCount >= numTrials)
			return null;
		
		return (Trial)trials.get(currentCount ++);
	}// end nextTrial
}