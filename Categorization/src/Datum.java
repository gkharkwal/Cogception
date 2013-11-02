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
	public Block block;
	public String response;
	public boolean result;
	public boolean ship_elongated;
	public boolean ship_wide;
	public boolean ship_filled;
	
	Datum(){
	}
	
	Datum(Block b, boolean e, boolean w, boolean f, String resp, boolean res){
		block = b;
		ship_elongated = e;
		ship_wide = w;
		ship_filled = f;
		response = resp;
		result = res;
	}
}
