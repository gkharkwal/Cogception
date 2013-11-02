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
	// response
	private double response;
	
	// ball position
	private double ballPosition;
	
	// orientation
	private int orientation;
	
	private Trial trial;
	
	// Default constructor
	public Datum(){
		// empty
	} // end constructor
	
	// constructor
	public Datum(double r, double bp, int o, Trial t){
		response = r;
		ballPosition = bp;
		trial = t;
		orientation = o;
	} // end constructor
	
	// getters:
	public double getResponse() { return response; }
	public double getBallPosition() { return ballPosition; }
	public Trial getTrial() { return trial; }
	public int getOrientation() { return orientation; }

} // end class
