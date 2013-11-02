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
	private int numDiscs;
	
	private int sdDiscs;
	
	private int testDiscSize;
		
	public Trial(){}
	
	public Trial(int n, int sd, int tsize){
		numDiscs = n;
		sdDiscs = sd;
		testDiscSize = tsize;
	}
	
	// getters:
	public int getNumDiscs(){ return numDiscs; }
	public int getSDDiscs() { return sdDiscs; }
	public int getTestDiscSize(){ return testDiscSize; }
}
