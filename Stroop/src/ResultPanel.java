/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */

import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.*;

	
public class ResultPanel extends Panel{
	// text to print
	private String str;
	
	// default constructor
	ResultPanel() { 
		super(); 
	} // end constructor
	
	// setters:
	public void setText(String newString){ str = newString; }
	
	// paints the panel
	public void paint(Graphics g1){
		Graphics2D g = (Graphics2D) g1;
		
		Dimension _d = getSize();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.white);
		g.fillRect(0, 0, _d.width, _d.height);
		g.setColor(Color.black);
		
		if (str != null){
			g.setColor(Color.red);
			Font font = new Font("Sans Serif", Font.BOLD, 16);
			FontMetrics fm = g.getFontMetrics(font);
			
			AttributedString a_str = new AttributedString(str);
			
			a_str.addAttribute(TextAttribute.FONT, font);
			
			g.drawString(a_str.getIterator(), (getSize().width - fm.stringWidth(str))/2, 
					(int) (((getSize().height - fm.getHeight())/4) + fm.getAscent()));
			
		}
	} // end paint

} // end class