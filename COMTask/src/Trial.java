/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */


public class Trial {
	private double aspectRatio;
	
	private boolean baseDown;
	
	private boolean initialPosition; // true == at the base, false == at the top

	
	public Trial(){}
	
	public Trial(double ar, boolean bd, boolean ip){
		aspectRatio = ar;
		baseDown 	= bd;
		initialPosition	= ip;
	}
	
	// getters:
	public double getAspectRatio(){ return aspectRatio; }
	
	public boolean getBaseDown(){ return baseDown; }
	
	public boolean getInitialPosition(){ return initialPosition; }
}
