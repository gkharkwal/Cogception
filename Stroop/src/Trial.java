/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */

import java.awt.Color;

public class Trial {
	// text
	private String text;
	
	// color of text
	private Color color;
	
	// condition
	private boolean match;
	
	// Constructor:
	public Trial(String t, Color c, boolean m){
		text  = t;
		color = c;
		match = m;
	}//end constructor
	
	// getters:
	public String getText(){ return text; }
	
	public Color getColor(){ return color; }
	
	public boolean getCondition(){ return match; }

	// setters:
	public void setText(String t){ text = t; }
	
	public void setColor(Color c){ color = c; }
	
	public void setCondition(boolean m){ match = m; }
	
	
	// returns name of color
	public String getColorText(){
		String name = "";
		if (color.equals(Color.red))
			name = "RED";
		else if (color.equals(Color.green))
			name = "GREEN";
		else if (color.equals(Color.blue))
			name = "BLUE";
		else if (color.equals(Color.black))
			name = "BLACK";
			
		return name;
	}// end getColorText
	
} // end class
