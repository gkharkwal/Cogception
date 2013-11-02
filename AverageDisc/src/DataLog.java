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
	
	// mean disc size
	private int meanDiscSize;
	
	// list of data
	private ArrayList data;
	// cannot declare object type for backward-compatibility
	
	// Default constructor
	public DataLog(){
		data = new ArrayList();
	} // end constructor
	
	// setters:
	public void setNumTrials(int num){ numTrials = num; }
	public void setIsDemo(boolean id){ isDemo = id; }
	public void setMeanDiscSize(int msize) { meanDiscSize = msize; }
	
	// add datum to the list of data
	public void addDatum(Datum d){
		data.add(d);		
	} // end addDatum
	
	
	// generates and returns a csv file
	public String generateLog(){
		String s = "TrialNum,RunType,NumDiscs,AverageSize,StandardDev,TestSize,Response\n";
		
		for (int i=0; i<numTrials; i++){
			s += Integer.toString(i+1);
			s += ",";
			
			if (isDemo)	s += "demo";
			else			s += "expt";
			s += ",";
			
			Datum d = (Datum)data.get(i);
			
			s += d.getTrial().getNumDiscs();
			s += ",";
			
			s += Integer.toString(meanDiscSize);
			s += ",";
			
			s += d.getTrial().getSDDiscs();
			s += ",";
			
			s += d.getTrial().getTestDiscSize();
			s += ",";
			
			s += d.getResponse();

			s += "\n";
		}
		
		//System.out.println(s);
		
		return s;
		
	} // end generateLog
	
} // end class
