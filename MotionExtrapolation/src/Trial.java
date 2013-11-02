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
	private int pathRadius;
	
	private int discRadius;
	
	private boolean convexUp; // true == up, false == down

	
	public Trial(){}
	
	public Trial(int pr, int dr, boolean c){
		pathRadius = pr;
		discRadius 	= dr;
		convexUp	= c;
	}
	
	// getters:
	public int getPathRadius(){ return pathRadius; }
	
	public int getDiscRadius(){ return discRadius; }
	
	public boolean getConvexUp(){ return convexUp; }
}
