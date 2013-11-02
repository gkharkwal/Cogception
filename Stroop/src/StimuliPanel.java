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
import java.text.AttributedString;

public class StimuliPanel extends Panel{
	// color
	private Color color;
	
	// text
	private String text;
	
	// isDemo
	private boolean isDemo;
	
	// Default constructor
	public StimuliPanel(){ 
		super(); 
	} // end constructor
	
	// setters:
	public void setColor(Color newColor){ color = newColor; }
	
	public void setText(String s){ text = s; }
	
	public void setIsDemo(boolean id){ isDemo = id; }
	
	// paints the panel
	public void paint(Graphics g1){
		Graphics2D g = (Graphics2D) g1;
		
		Dimension _d = getSize();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.white);
		g.fillRect(0, 0, _d.width, _d.height);
		g.setColor(Color.black);
		
		long st = System.currentTimeMillis();
		while (System.currentTimeMillis() - st < 175);
		
		g.setColor(color);
		
		int rect_width = _d.width/3;
		int rect_height = _d.height/4;
		
		/*
		if (text.equals("-") && isDemo){
			// for demo
			g.fillRect((_d.width-rect_width)/2, _d.height/2, rect_width, rect_height);
		}
		else */ 
		{
			if (text.equals("-"))
				text = "XXXXXX";
			
			Font font = new Font("Times New Roman", Font.BOLD, 50);
			FontMetrics fm = g.getFontMetrics(font);
			
			AttributedString a_str = new AttributedString(text);
			
			a_str.addAttribute(TextAttribute.FONT, font);
			
			g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(text))/2, 
					(int) (((_d.height - fm.getHeight())/4) + fm.getAscent()));			
		}
		
	} // end paint
	
} // end class