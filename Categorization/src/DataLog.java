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
	int numTrials;
	boolean isDemo;
	ArrayList data;
	
	DataLog(int n, boolean id){
		numTrials = n;
		isDemo = id;
		data = new ArrayList();
	}
	
	public void addDatum(Datum d){
		data.add(d);
	}
	
	public String generateLog(){
		String s = "TrialNum,RunType,BlockNum,Parity,Complexity,S1_Elongated,S1_Wide,S1_Filled,S2_Elongated,S2_Wide,S2_Filled,Ship_Elongated,Ship_Wide,Ship_Filled,Response,Result\n";
		
		for (int i=0; i<numTrials; i++){
			s += Integer.toString(i+1); // trial number
			s += ",";
			
			s += (isDemo)? "demo,": "expt,"; // runType
			
			s += Integer.toString(i/8 + 1); // block number
			s += ",";
			
			Datum d = ((Datum) data.get(i));
			Block b = d.block;
			
			s += (b.parity)? "up,": "down,"; // parity
			
			s += Integer.toString(b.complexity); // complexity
			s += ",";
			
			s += Boolean.toString(b.e1_elongated);
			s += ",";
			
			s += Boolean.toString(b.e1_wide);
			s += ",";

			s += Boolean.toString(b.e1_filled);
			s += ",";
			
			s += Boolean.toString(b.e2_elongated);
			s += ",";
			
			s += Boolean.toString(b.e2_wide);
			s += ",";

			s += Boolean.toString(b.e2_filled);
			s += ",";
			
			s += Boolean.toString(d.ship_elongated);
			s += ",";
			
			s += Boolean.toString(d.ship_wide);
			s += ",";

			s += Boolean.toString(d.ship_filled);
			s += ",";
			
			s += d.response;
			s += ",";
			
			s += Boolean.toString(d.result);
			s += "\n";
		}
		
		return s;
	}
}
