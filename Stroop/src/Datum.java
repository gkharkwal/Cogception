/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */


public class Datum {
	// reusing the Trial class to store trial info
	private Trial trial;
	
	// reaction time
	private long _rt;
	
	// did they get the trial right or wrong
	private boolean _ans;
	
	// Default constructor
	public Datum(){
		// empty
	} // end constructor
	
	// constructor
	public Datum(Trial t, long rt, boolean ans){
		trial = t;
		_rt    = rt;
		_ans   = ans;
	} // end constructor
	
	// getters:
	public Trial getTrial(){ return trial; }
	
	public long getRT(){ return _rt; }
	
	public boolean getAns(){ return _ans; }
	
	// setters:
	public void setTrial(Trial t){ trial = t; }
	
	public void setRT(long rt){ _rt = rt; }
	
	public void setAns(boolean ans){ _ans = ans; }

} // end class
