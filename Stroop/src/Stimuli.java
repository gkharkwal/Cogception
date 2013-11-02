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
	// the set of colors is fixed;
	private final Color[] colors = {Color.red, Color.green, Color.blue, Color.black};
	
	// A random number generator
	private Random randgen;
	
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
	public Stimuli(int nt, int npt, boolean id){		
		numTrials = nt;
		
		isDemo = id;
		
		randgen = new Random();
		
		trials = new ArrayList();
		
		ArrayList pracTrials = null;
		
		/*
		if (isDemo){
			for (int i=0; i<numTrials; i++){
				int colorIndex = i%(colors.length);
				trials.add(new Trial("-", colors[colorIndex], false));
			}// end for
		}
		else */ 
		{			
			// create trials
			for (int i=0; i<numTrials; i++){
				if (i < (numTrials/3)){
					// create "match" conditions
					int colorIndex = i%(colors.length);
					String text = _decodeColorName(colors[colorIndex]);
					
					trials.add(new Trial(text, colors[colorIndex], true));
				} 
				else if (i < (2*numTrials/3)) {
					// create "no match" conditions
					int colorIndex = i%(colors.length);
					int chosenName = randgen.nextInt(4);
					if (chosenName == colorIndex){ // match! d'oh!
						 // choose next color-name instead
						chosenName = (chosenName + 1)%(colors.length);
					}
					String text = _decodeColorName(colors[chosenName]);
					
					trials.add(new Trial(text, colors[colorIndex], false));
				}
				else {
					// create "neutral" conditions
					int colorIndex = i%(colors.length);
					
					trials.add(new Trial("-", colors[colorIndex], false));
				} // end if
			} // end for
			
			
			// create practice trials
			pracTrials = new ArrayList();
			for (int i=0; i< npt; i++){
				if (i < (npt/3)){
					// create "match" conditions
					int colorIndex = i%(colors.length);
					String text = _decodeColorName(colors[colorIndex]);
					
					pracTrials.add(new Trial(text, colors[colorIndex], true));
				} 
				else if (i < (2*npt/3)) {
					// create "no match" conditions
					int colorIndex = i%(colors.length);
					int chosenName = randgen.nextInt(4);
					if (chosenName == colorIndex){ // match! d'oh!
						 // choose next color-name instead
						chosenName = (chosenName + 1)%(colors.length);
					}
					String text = _decodeColorName(colors[chosenName]);
					
					pracTrials.add(new Trial(text, colors[colorIndex], false));
				}
				else {
					// create "neutral" conditions
					int colorIndex = i%(colors.length);
					
					pracTrials.add(new Trial("-", colors[colorIndex], false));
				} // end if
			} // end for
		} // end if-else

		
		numTrials += npt;
		
		Collections.shuffle(trials);
		
		if (pracTrials != null){
			Collections.shuffle(pracTrials);
			trials.addAll(0, pracTrials);
		}
		
		// set current count to -1
		currentCount = -1;
	} //end constructor
	
	
	// get color name
	private String _decodeColorName(Color c){
		String name = "";
		if (c.equals(Color.red))
			name = "RED";
		else if (c.equals(Color.green))
			name = "GREEN";
		else if (c.equals(Color.blue))
			name = "BLUE";
		else if (c.equals(Color.black))
			name = "BLACK";
			
		return name;
	} // end decodeColorName
	
	// returns next trial
	public Trial nextTrial(){
		if (currentCount == -1){
			// very first "trial"
			currentCount ++;
			String txt = null;
			if (isDemo)
				txt = "Begin Demo.";
			else
				//txt = "Begin Practice trials.";
				txt = "Begin Experiment.";
			
			return new Trial(txt, Color.black, false);
		}
		
		if (currentCount >= numTrials)
			return null;
		
		return (Trial)trials.get(currentCount ++);
	}// end nextTrial
	
}//end class