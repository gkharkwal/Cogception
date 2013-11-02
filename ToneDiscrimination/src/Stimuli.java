/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Stimuli {
	// the set of tones is fixed;
	private final String[] _demoTones = {"600", "700", "800", "900", "1100", "1200", "1300", "1400"};
	private final String[] _mainTones = {"980", "985", "990", "995", "1005", "1010", "1015", "1020"};
	private final String _standardTone = "1000";
	
	// number of trials to generate
	private int numTrials;
	
	// list of trials
	private ArrayList trials;
	// for backward compatibility with java 1.4.2 -- can't specify type of object in list
	
	// current count
	private int currentCount;
	
	// demo or expt
	private boolean isDemo;
	
	// Constructor
	public Stimuli(int nt, boolean id){		
		numTrials = nt;
		
		isDemo = id;
		
		trials = new ArrayList();
		
		ArrayList pracTrials = null;
		
		if (isDemo){
			for (int i=0; i<numTrials; i++){
				int dtIndex = i%(_demoTones.length);
				trials.add(new Trial(_standardTone, _demoTones[dtIndex]));
			}// end for
		}
		else {			
			// create trials
			for (int i=0; i<numTrials; i++){
				int mtIndex = i%(_mainTones.length);				
				trials.add(new Trial(_standardTone, _mainTones[ mtIndex]));
			} // end for
		} // end if-else
		
		Collections.shuffle(trials);

		// set current count to 0
		currentCount = 0;
	} //end constructor
	
	// returns next trial
	public Trial nextTrial(){
		if (currentCount >= numTrials)
			return null;
		
		return (Trial)trials.get(currentCount ++);
	}// end nextTrial
	
}//end class