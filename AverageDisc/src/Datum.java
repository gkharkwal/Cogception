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
	
	// response
	private char response;
	
	// Default constructor
	public Datum(){
		// empty
	} // end constructor
	
	// constructor
	public Datum(Trial t, char r){
		trial = t;
		response = r;
	} // end constructor
	
	// getters:
	public Trial getTrial(){ return trial; }
	public char getResponse() { return response; }

	// setters:
	public void setTrial(Trial t){ trial = t; }

} // end class
