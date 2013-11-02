/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */

import java.text.DecimalFormat;
import java.util.ArrayList;


public class DataLog {
	// number of trials
	private int numTrials;
	
	// demo or expt
	private boolean isDemo;
	
	// list of data
	private ArrayList _data;
	// cannot declare object type for backward-compatibility
	
	// Default constructor
	public DataLog(){
		_data = new ArrayList();
	} // end constructor
	
	// setters:
	public void setNumTrials(int num){ numTrials = num; }
	
	public void setIsDemo(boolean id){ isDemo = id; }
	
	// add datum to the list of data
	public void addDatum(Datum datum){
		_data.add(datum);		
	} // end addDatum
	
	
	// generates and returns a csv file
	public String generateLog(){
		String s = "TrialNum,RunType,AspectRatio,UpDown,InitialOrientation,Base,Height,Prediction,Response\n";
		
		for (int i=0; i<numTrials; i++){
			s += Integer.toString(i+1);
			s += ",";
			
			if (isDemo)	s += "demo";
			else		s += "expt";
			s += ",";
			
			Datum d = (Datum)_data.get(i);
			
			DecimalFormat df = new DecimalFormat("##.##");
			
			s += df.format(d.getAspectRatio());
			s += ",";
			
			s += d.getBaseDown() == true? "Down,": "Up,";
			
			s += d.getIsVertical() == true? "Vertical,": "Horizontal,";
			
			s += Integer.toString(d.getBaseWidth());
			s += ",";
			
			s += Integer.toString(d.getHeight());
			s += ",";
			
			s += df.format(d.getPrediction());
			s += ",";
			
			s += df.format(d.getResponse());
			s += "\n";
		}

		return s;
		
	} // end generateLog
	
} // end class