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
	
	// aspect ratio
	private double aspectRatio;
	
	// is the base on the table
	private boolean baseDown;
	
	// initial orientation
	private boolean initialPosition; // true == at the base, false == at the top
	
	// angle of unstable equilibrium
	private int trueValue;

	// length of the base
	private int base;

	// height of the object
	private int height;
	
	// Default constructor
	public Datum(){
		// empty
	} // end constructor
	
	// constructor
	public Datum(double r, double ar, boolean bd, boolean ip, int tv, int b, int h){
		response = r;
		aspectRatio = ar;
		baseDown = bd;
		initialPosition = ip;
		trueValue = tv;
		base = b;
		height = h;
	} // end constructor
	
	// getters:
	public double getResponse() { return response; }
	public double getAspectRatio() { return aspectRatio; }
	public boolean getBaseDown() { return baseDown; }
	public boolean getInitialPosition() { return initialPosition; }
	public int getTrueValue() { return trueValue; }
	public int getBaseWidth() { return base; }
	public int getHeight() { return height; }
	

} // end class
