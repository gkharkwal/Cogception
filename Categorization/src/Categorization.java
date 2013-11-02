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
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Categorization extends Applet
	implements Runnable, KeyListener{
	
	int numTrials;
	boolean isDemo;
	
	Thread t;
	boolean threadSuspended;
	
	Font font, sFont;

	ArrayList ships;
	int e1_index, e2_index;
	int ship_index;
	
	boolean attack, welcome;
	int laserEnd;
	boolean explode;
	int explosionRadius;
	
	Stimuli stimuli;
	DataLog dataLog;
	
	int currentCount;
	Block currentBlock;
	
	boolean learningPhase;
	boolean clear;
	boolean experimentOver;
	
	public void init(){
		setSize(800,600);
		
		String str = getParameter("numTrials");
		numTrials = (str != null)? Integer.parseInt(str): 4;
		
		if (numTrials % 8 != 0){
			numTrials = (numTrials/8 + 1) * 8;
		}
		
		str = getParameter("runType");
		isDemo = (str != null)? str.equalsIgnoreCase("demo"): true;

		font = new Font("Times New Roman", Font.BOLD, 24);
		sFont = new Font("Times New Roman", Font.BOLD, 16);
		 
		ships = new ArrayList();
		ships.add(new SpaceShip(true, true, true));
		ships.add(new SpaceShip(true, true, false));
		ships.add(new SpaceShip(true, false, true));
		ships.add(new SpaceShip(true, false, false));
		ships.add(new SpaceShip(false, true, true));
		ships.add(new SpaceShip(false, true, false));
		ships.add(new SpaceShip(false, false, true));
		ships.add(new SpaceShip(false, false, false));
		
		experimentOver = true;
		
		stimuli = new Stimuli(numTrials);
		dataLog = new DataLog(numTrials, isDemo);
		currentCount = -1;
		 
		addKeyListener(this);
		
		t = new Thread(this);
		threadSuspended = false;
		t.start();
	}
	
	public void stop(){
		if (currentCount == numTrials - 1){
			try{
		        // redirect to that finish page
				getAppletContext().showDocument(new URL(getCodeBase(),"finis.php"), "_self");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} 	
		}
	}
	
	public void run() {
		while (true){
			if (experimentOver){
				repaint();
				
				threadSuspended = true;
				synchronized( this ) {
            	   while ( threadSuspended ) {
            		   try {
                    	 wait();
            		   }
            		   catch (InterruptedException e) {
							e.printStackTrace();
            		   }
            	   }
				}
			}
			else {
				clear = false;
				{
					// show learning phase
					learningPhase = true;
					currentBlock = stimuli.nextBlock();
	
					for (int i=0; i<8; i++){
						if( ((SpaceShip)ships.get(i)).isDefinition
								(currentBlock.e1_elongated, currentBlock.e1_wide, currentBlock.e1_filled) ){
							e1_index = i;
						}
						else if( ((SpaceShip)ships.get(i)).isDefinition
								(currentBlock.e2_elongated, currentBlock.e2_wide, currentBlock.e2_filled) ){
							e2_index = i;
						}
					}
					
					int[] xPosNonExample = {200, 400, 600, 200, 400, 600}; 
					if (currentBlock.parity){
						((SpaceShip) ships.get(e1_index)).x = 300;
						((SpaceShip) ships.get(e1_index)).y = 150;
						
						((SpaceShip) ships.get(e2_index)).x = 500;
						((SpaceShip) ships.get(e2_index)).y = 150;
						
						int counter = 0;
						for (int i=0; i<8; i++){
							if (i == e1_index || i == e2_index)
								continue;
							
							((SpaceShip) ships.get(i)).x = xPosNonExample[counter];
							((SpaceShip) ships.get(i)).y = (counter<3)? 400: 500;
							counter ++;
						}
					}
					else {
						((SpaceShip) ships.get(e1_index)).x = 300;
						((SpaceShip) ships.get(e1_index)).y = 450;
						
						((SpaceShip) ships.get(e2_index)).x = 500;
						((SpaceShip) ships.get(e2_index)).y = 450;
						
						int counter = 0;
						for (int i=0; i<8; i++){
							if (i == e1_index || i == e2_index)
								continue;
							
							((SpaceShip) ships.get(i)).x = xPosNonExample[counter];
							((SpaceShip) ships.get(i)).y = (counter<3)? 100: 200;
							counter ++;
						}
					}
					
					repaint();
					
					try {
				        Thread.sleep(10000);
				    } 
				    catch (InterruptedException e) {
				    } 
				    // show for 5 seconds 
				    
				    learningPhase = false;
				} // done with learning phase
				
				/*=========================================*/
				/*=========================================*/
				
				
				Collections.shuffle(ships); // randomize before each block
				for (int i=0; i<8; i++){
					// 8 separate `games'
					int x=getSize().width/2,
						y=100;
					ship_index = i;
					
					clear = false;
					attack = false;
					welcome = false;
					explode = false;
					
					((SpaceShip) ships.get(ship_index)).x = x;
					while(true){
						((SpaceShip) ships.get(ship_index)).y = y;
						repaint();
						
						if (attack){
							laserEnd = 600;
							while (laserEnd > y+20){
								laserEnd -= 30;
								repaint();
								try {
							        Thread.sleep(20);
							    } 
							    catch (InterruptedException e) {
							    }
							}
							
							explode = true;
							explosionRadius = 20;
							
							while (explosionRadius > 0){
								explosionRadius -= 5;
								repaint();
								try {
							        Thread.sleep(100);
							    } 
							    catch (InterruptedException e) {
							    }
							}
							break;
						}
						else if ( welcome ){
							while (y < 600){
								y += 15;
								((SpaceShip) ships.get(ship_index)).y = y;
								
								repaint();
								try {
							        Thread.sleep(20);
							    } 
							    catch (InterruptedException e) {
							    }
							}
							break;
						}
						else if (y > 600)
							break;
							
						
						y += 2;
						
						try {
					        Thread.sleep(20);
					    } 
					    catch (InterruptedException e) {
					    }
					}
				    
					trialEnd();
					
					clear = true;
					repaint();
	
					try {
				        Thread.sleep(500);
				    } 
				    catch (InterruptedException e) {
				    }
					////////////////////////////
					// GAME OVER!
					////////////////////////////
				}
			}
		}
	}
	
	private void trialEnd(){
		currentCount ++;
		
		Datum d = new Datum();
		d.block = currentBlock;
		d.ship_elongated = ((SpaceShip) ships.get(ship_index)).elongated;
		d.ship_wide = ((SpaceShip) ships.get(ship_index)).wide;
		d.ship_filled = ((SpaceShip) ships.get(ship_index)).filled;
		
		if (attack)
			d.response = "Attack";
		else if (welcome)
			d.response = "Welcome";
		else
			d.response = "None";
		
		if (currentBlock.parity){
			if( ((SpaceShip)ships.get(ship_index)).isDefinition
					(currentBlock.e1_elongated, currentBlock.e1_wide, currentBlock.e1_filled) || 
				((SpaceShip)ships.get(ship_index)).isDefinition
					(currentBlock.e2_elongated, currentBlock.e2_wide, currentBlock.e2_filled) ){
					// friendly ship
				if (welcome)
					d.result = true;
				else
					d.result = false;
			
			}
			else {
				// enemy ship
				if (attack)
					d.result = true;
				else
					d.result = false;
			}
		}
		else{
			if( ((SpaceShip)ships.get(ship_index)).isDefinition
					(currentBlock.e1_elongated, currentBlock.e1_wide, currentBlock.e1_filled) || 
				((SpaceShip)ships.get(ship_index)).isDefinition
					(currentBlock.e2_elongated, currentBlock.e2_wide, currentBlock.e2_filled) ){
				// enemy ship
				if (attack)
					d.result = true;
				else
					d.result = false;
			
			}
			else {
				// friendly ship
				if (welcome)
					d.result = true;
				else
					d.result = false;
			}
		}
		
		dataLog.addDatum(d);
		
		if (currentCount == numTrials-1){
			experimentOver = true;
			repaint();
			
			_sendOutput();
			stop();
		}
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	public void paint(Graphics g){
		Dimension _d = getSize();
		
		// create an off-screen image for double buffering
		BufferedImage image = new BufferedImage(_d.width, _d.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D ig = image.createGraphics();
		ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		ig.setColor(Color.white);
		ig.fillRect(0, 0, _d.width, _d.height);
		ig.setColor(Color.black);
		
		if (currentCount == -1 && experimentOver){
			FontMetrics fm = ig.getFontMetrics(font);
			
			String txt = "Press spacebar to begin Experiment.";
			if (isDemo)
				txt = "Press spacebar to begin Demo.";
			
			AttributedString a_str = new AttributedString(txt);
			a_str.addAttribute(TextAttribute.FONT, font);
			
			ig.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
					(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));			
		}
		else if (experimentOver){
			FontMetrics fm = ig.getFontMetrics(font);
			
			String s = "Experiment over.";
			AttributedString a_str = new AttributedString(s);
			a_str.addAttribute(TextAttribute.FONT, font);
			
			ig.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(s))/2, 
					(int) ((_d.height - fm.getHeight())/2 + fm.getAscent()));
		}
		else if (learningPhase){
			FontMetrics fm = ig.getFontMetrics(font);
			String a = "Allies:";
			String e = "Enemies:";
			
			AttributedString a_str = new AttributedString(a);
			a_str.addAttribute(TextAttribute.FONT, font);
			
			ig.drawString(a_str.getIterator(), (100 - fm.stringWidth(a)), 
					(int) ((50 - fm.getHeight()) + fm.getAscent()));
			
			a_str = new AttributedString(e);
			a_str.addAttribute(TextAttribute.FONT, font);
			
			ig.drawString(a_str.getIterator(), (100 - fm.stringWidth(a)), 
					(int) ((320 - fm.getHeight()) + fm.getAscent()));
			
			for (int i=0; i<8; i++){
				((SpaceShip) ships.get(i)).paint(ig);
			}
		}
		else if (!clear){
			if (attack){
				if (explode){
					ig.setColor(Color.orange);
					ig.fillOval(((SpaceShip) ships.get(ship_index)).x - explosionRadius, 
								((SpaceShip) ships.get(ship_index)).y - explosionRadius, 
								2*explosionRadius, 2*explosionRadius);
				}
				else{
					ig.setColor(Color.red);
					for (int i=0; i<4; i++){
						ig.drawLine(398+i, 600, 398+i, laserEnd);
					}
				}
			}
			else if (welcome){
				ig.setColor(Color.cyan);
				for (int i=0; i<20; i++){
					ig.drawLine(390+i, 600, 390+i, 0);
				}
			}
			
			if (!(attack && explode))
				((SpaceShip) ships.get(ship_index)).paint(ig);
			
			ig.setColor(Color.black);
			AttributedString a_str = new AttributedString("\',\' = ATTACK          \'.\' = WELCOME");
			a_str.addAttribute(TextAttribute.FONT, sFont);
			
			ig.drawString(a_str.getIterator(), 3*_d.width/5, (int) (13*_d.height/15));
		}
		
		g.drawImage(image, 0, 0, this);
	}

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub		
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyChar();
		
		if (key == ','){
			if (!attack && !welcome)
				attack = true;
		}
		if (key == '.'){
			if (!attack && !welcome)
				welcome = true;
		}
		if (key == KeyEvent.VK_SPACE) { // spacebar
			if (experimentOver)
				experimentOver = false;

			if (threadSuspended){
				threadSuspended = false;
				synchronized(this){
					notify();
				}
			}
		}
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	// sends output to the server -- a helper
	private void _sendOutput(){
		System.out.println(dataLog.generateLog());
		try {
			String filePath = getParameter("filePath");
			String msg = "fp="+filePath+"&output="+dataLog.generateLog();
			
			URL url = new URL(getCodeBase(),"writeData.php");
			HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setRequestMethod("POST");
			httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			httpCon.setUseCaches(false);
			httpCon.setDoInput(true);
			httpCon.setDoOutput(true);
			
			DataOutputStream out = new DataOutputStream( httpCon.getOutputStream() );
			out.writeBytes(msg);
			out.flush();
			out.close();
			
		    //Get Response
			InputStream is = httpCon.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    String line;
		    StringBuffer response = new StringBuffer(); 
		    while((line = rd.readLine()) != null) {
		    	response.append(line);
		        response.append('\r');
		    }
		    rd.close();
		    
		    System.out.println(response);
			
		    // terminate the connection
	        httpCon.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
