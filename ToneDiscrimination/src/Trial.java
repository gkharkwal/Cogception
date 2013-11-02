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
	// standard frequency
	private String standard;
	
	// comparison frequency
	private String comparison;
	
	
	// Constructor:
	public Trial(String s, String c){
		standard = s;
		comparison = c;
	}//end constructor
	
	// getters:
	public String getStandard(){ return standard; }
	
	public String getComparison(){ return comparison; }

	// setters:
	public void setStandard(String s){ standard = s; }
	
	public void setComparison(String c){ comparison = c; }

} // end class
