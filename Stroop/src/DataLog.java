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


public class DataLog {
	// number of trials
	private int numTrials;
	
	// demo or expt
	private boolean isDemo;
	
	// list of data
	private ArrayList data;
	// cannot declare object type for backward-compatibility
	
	// Default constructor
	public DataLog(){
		data = new ArrayList();
	} // end constructor
	
	// getters:
	public int getNumTrials(){ return numTrials; }

	public boolean getIsDemo(){ return isDemo;	}
	
	// setters:
	public void setNumTrials(int num){ numTrials = num; }
	
	public void setIsDemo(boolean id){ isDemo = id; }
	
	// add datum to the list of data
	public void addDatum(Datum d){
		data.add(d);		
	} // end addDatum
	
	
	// generates and returns a csv file
	public String generateLog(){
		String s = "TrialNum,RunType,Condition,Text,Color,ReactionTime,Answer\n";
		
		for (int i=0; i<numTrials; i++){
			s += Integer.toString(i+1);
			s += ",";
			
			if (isDemo)	s += "demo";
			else			s += "expt";
			s += ",";
			
			Datum d = (Datum)data.get(i);
			
			if (d.getTrial().getCondition())	s += "match";
			else if (d.getTrial().getText().equals("-")) s += "neutral";
			else s += "no_match";
			
			s += ",";
			
			s += d.getTrial().getText();
			s += ",";
			
			s += d.getTrial().getColorText();
			s += ",";
			
			s += Long.toString(d.getRT());
			s += ",";
			
			s += Boolean.toString(d.getAns());
			
			s += "\n";
		}
		
		//System.out.println(s);
		
		return s;
		
	} // end generateLog
	
} // end class
