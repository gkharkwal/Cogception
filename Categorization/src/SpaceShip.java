/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;

public class SpaceShip extends Applet{
	public boolean elongated;
	public boolean wide;
	public boolean filled;
	
	public int x, y;
	
	int backLength = 20; // pixels
	int width, frontLength;
	
	SpaceShip(boolean e, boolean w, boolean f){
		elongated = e;
		wide = w;
		filled = f;
		
		width = (wide)? 2*backLength: backLength;
		frontLength = (elongated)? 5*backLength/2: backLength;
	}
	
	public boolean isDefinition(boolean e, boolean w, boolean f){
		return (elongated==e && wide==w && filled==f);
	}
	
	public void paint(Graphics g){
		if (filled)
			g.setColor( Color.black );
		else
			g.setColor( Color.gray );
		
		{
			if (elongated){
				int xpoints[] = {x, x-2*backLength/3, x-width, x-width, x-2*backLength/3, x+2*backLength/3, 
						x+width, x+width, x+2*backLength/3, x};
				int ypoints[] = {y+frontLength, y, y-backLength/5, y-4*backLength/5, y-backLength, y-backLength, 
						y-4*backLength/5, y-backLength/5, y, y+frontLength};
	
				g.fillPolygon(xpoints, ypoints, xpoints.length);
			}
			else {
				int xpoints[] = {x-backLength/3, x-2*backLength/3, x-width, x-width, x-2*backLength/3, x+2*backLength/3, 
									x+width, x+width, x+2*backLength/3, x+backLength/3, x-backLength/3};
				int ypoints[] = {y+frontLength, y, y-backLength/5, y-4*backLength/5, y-backLength, y-backLength, 
									y-4*backLength/5, y-backLength/5, y, y+frontLength, y+frontLength};
				
				g.fillPolygon(xpoints, ypoints, xpoints.length);
				
			}
		}
	}
}
