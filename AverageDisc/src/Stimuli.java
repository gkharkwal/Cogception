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
	private final int [] numDiscsOpts = {4, 16};
	
	private final int [] sdDiscsOpts = {2, 5};

	private final int [] testDiscSizeOpts = {10, 13, 16, 20, 25, 32, 40}; 
						// 7 numbers between -1 to 1 on a log scale
	
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
		
		n = (n/28 > 0? n/28: 1);
		for (int i=0; i<n; i++)
			for (int j=0; j<2; j++)
				for (int k=0; k<2; k++)
					for (int p=0; p<7; p++)
						trials.add(new Trial(numDiscsOpts[j], sdDiscsOpts[k], testDiscSizeOpts[p]));
		
		Collections.shuffle(trials);
	
		currentCount = 0;
	} // end constructor
	
	// returns next trial
	public Trial nextTrial(){
		if (currentCount >= numTrials)
			return null;
		
		return (Trial)trials.get(currentCount ++);
	}// end nextTrial
}