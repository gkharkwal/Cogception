/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */

public class Block {
	public boolean e1_elongated;
	public boolean e2_elongated;
	
	public boolean e1_wide;
	public boolean e2_wide;
	
	public boolean e1_filled;
	public boolean e2_filled;
	
	public boolean parity;
	
	public int complexity;
	
	Block(boolean[][] params, boolean p, int c){
		e1_elongated = params[0][0];
		e1_wide		  = params[0][1];
		e1_filled	  = params[0][2];
		
		e2_elongated = params[1][0];
		e2_wide		  = params[1][1];
		e2_filled	  = params[1][2];
		
		parity = p;
		complexity = c;
	}
	
	public void print(){
		System.out.print("example1:( " + e1_elongated+ ", " + e1_wide + ", " + e1_filled + ")");
		System.out.print(" example2:( " + e2_elongated+ ", " + e2_wide + ", " + e2_filled + ")");
		System.out.println(" parity: " + parity);
	}
}
