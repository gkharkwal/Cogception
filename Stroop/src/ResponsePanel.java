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

// prints the response options
public class ResponsePanel extends Panel{
	private boolean state;
	
	// Default constructor
	ResponsePanel() { 
		super();
		
		state = false; // inactive
	} // end constructor
	
	public void setState(boolean s){ state = s; }
	
	// paints the panel
	public void paint(Graphics g1){
		Graphics2D g = (Graphics2D) g1;
		
		Dimension _d = getSize();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.white);
		g.fillRect(0, 0, _d.width, _d.height);
		g.setColor(Color.black);
		
		if (state){			
			g.setColor(Color.black);
			Font font = new Font("Times New Roman", Font.BOLD, 24);
			FontMetrics fm = g.getFontMetrics(font);
			
			String str = new String("Response");
			AttributedString a_str = new AttributedString(str);
			
			a_str.addAttribute(TextAttribute.FONT, font);
			
			g.drawString(a_str.getIterator(), (getSize().width - fm.stringWidth(str))/2, 
					(int) (((getSize().height - fm.getHeight())/4) + fm.getAscent()));
			
			String
				r1 = new String("1 - Red"),
				r2 = new String("2 - Green"),
				r3 = new String("3 - Blue"),
				r4 = new String("4 - Black");
			
			font = new Font("Times New Roman", Font.BOLD, 16);
			fm = g.getFontMetrics(font);
			
			g.drawString(r1, (int) getSize().width/3 - fm.stringWidth(r3)/2, 
							(int) getSize().height/2);
			g.drawString(r2, (int) 2*getSize().width/3 - fm.stringWidth(r2)/2, 
							(int) getSize().height/2);
			g.drawString(r3, (int) getSize().width/3 - fm.stringWidth(r3)/2, 
							(int) 2*getSize().height/3);
			g.drawString(r4, (int) 2*getSize().width/3 - fm.stringWidth(r2)/2, 
							(int) 2*getSize().height/3);
		} 
		else {
			g.setColor(getBackground());
			g.drawRect(0, 0, getSize().width, getSize().height);
			
			g.setColor(Color.black);
			Font font = new Font("Times New Roman", Font.BOLD, 24);
			FontMetrics fm = g.getFontMetrics(font);
			
			String str = new String("Press Spacebar to continue.");
			AttributedString a_str = new AttributedString(str);
			
			a_str.addAttribute(TextAttribute.FONT, font);
			
			g.drawString(a_str.getIterator(), (getSize().width - fm.stringWidth(str))/2, 
					(int) (((getSize().height - fm.getHeight())/4) + fm.getAscent()));
		}
		
	} // end paint
	
} // end constructor