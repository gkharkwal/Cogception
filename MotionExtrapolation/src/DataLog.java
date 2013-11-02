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
	
	// velocity
	private int velocity;
	
	// path length
	private int pathLength;
	
	// vertical range of start position
	private int orientationRange;
	
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
	
	public void setVelocity(int v) { velocity = v; }
	
	public void setPathLength(int pl) { pathLength = pl; }
	
	public void setOrientationRange(int or) { orientationRange = or; }
	
	// add datum to the list of data
	public void addDatum(Datum datum){
		_data.add(datum);		
	} // end addDatum
	
	
	// generates and returns a csv file
	public String generateLog(){
		String s = "TrialNum,RunType,PathLength,PathCurvature,Velocity,DiscRadius,OrientationRange,ConvexityDirection,Orientation,BallPosition,Response,Offset\n";
		
		for (int i=0; i<numTrials; i++){
			s += Integer.toString(i+1);
			s += ",";
			
			if (isDemo)	s += "demo";
			else			s += "expt";
			s += ",";
			
			Datum d = (Datum)_data.get(i);
			
			s += Integer.toString(pathLength);
			s += ",";
			
			switch(d.getTrial().getPathRadius()){
				case 0:
					s += "Zero,";
					break;
				case 260:
					s += "Low,";
					break;
				case 520:
					s += "High,";
					break;
			}
			//s += Integer.toString(d.getTrial().getPathRadius());
			//s += ",";
			
			s += Integer.toString(velocity);
			s += ",";
			
			s += Integer.toString(d.getTrial().getDiscRadius());
			s += ",";
			
			s += Integer.toString(orientationRange);
			s += ",";
			
			s += d.getTrial().getConvexUp() == true? "Up,": "Down,";
			
			s += Integer.toString(d.getOrientation());
			s += ",";
			
			DecimalFormat df = new DecimalFormat("##.##");
			
			s += df.format(d.getBallPosition());
			s += ",";
			
			s += df.format(d.getResponse());
			s += ",";
			
			s += df.format(d.getBallPosition() - d.getResponse());
			s += "\n";
		}
		
		//System.out.println(s);
		
		return s;
		
	} // end generateLog
	
} // end class