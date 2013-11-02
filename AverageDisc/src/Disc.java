/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */

public class Disc {
	private int x;
	private int y;
	private int radius;
	
	Disc(){
		x = y = 0;
		radius = 1;
	}
	
	// getters:
	public int getX(){ return x; }
	public int getY(){ return y; }
	public int getRadius(){ return radius; }
	
	// setters:
	public void setX(int x){ this.x = x; }
	public void setY(int y){ this.y = y; }
	public void setRadius(int r){ radius = r; }
}
