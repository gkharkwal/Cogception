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

public class DecisionMaking extends Applet
	implements Runnable, KeyListener {
		
		// ------params------- //
		
		// number of expt trials
		private int numTrials;

		// is the current run a demo
		private boolean isDemo;
		
		//---- helper class ----//
		
		// the log of data
		private DataLog dataLog;
		
		// the stimuli
		private Stimuli stimuli;
		
		//----local variables----//
		
		// random generator
		private Random randGen;
		
		// current trial Number
		private int currentCount;
		
		// run the trial
		private boolean runTrial;
		
		// current trial
		private Trial currentTrial;
		
		private Thread t;
		
		private boolean threadSuspended;
		
		// user response
		private char response;
		
		// collect response
		private boolean getResponse;
				
		// init
		public void init(){
			// size would be set by the html
			setSize(800,600);
			
			//String str = getParameter("numTrials");
			//numTrials = (str != null)? Integer.parseInt(str): 28;
			numTrials = 12;
			
			String str = getParameter("runType");
			isDemo = (str != null)? str.equalsIgnoreCase("demo"): false;
			
			addKeyListener(this);
			
			stimuli = new Stimuli(numTrials);
			
			dataLog = new DataLog();
			
			dataLog.setNumTrials(numTrials);
			dataLog.setIsDemo(isDemo);
			
		    randGen = new Random();
		    randGen.setSeed(System.currentTimeMillis());
		    
			currentCount = -1;;
			
			t = new Thread(this);
			threadSuspended = false;
			t.start();
		} // end init
		
		
		// start
		public void start(){
			if (currentCount == -1){
				runTrial = false;
			}
		} // end start
		
		//stop
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
				if (runTrial == true){
					trialInit();
				    
				   	// now collect response
					getResponse = true;
				    // wait for response
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
					
					trialEnd();
				}
				else {
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
				} // end if-else runTrial
			} // end infinite loop
		}
		
		private void trialInit() {
			currentCount ++;		
			currentTrial = stimuli.nextTrial();
			
			repaint();
		}

		private void trialEnd(){
			dataLog.addDatum(new Datum(currentTrial, response));
			if (currentCount == numTrials - 1){
				repaint();
				_sendOutput();
				stop();
			}
		}
		
		public void update (Graphics g){
			paint(g);
		}

		// paints the panel
		public void paint(Graphics g1){
			// Note: double buffering only makes things harder here.
			//       the displays need to be flashed for a certain period of time
			// 		 with double buffering, only the last image will be seen.
			//		 the alternative is to create a thread and add lots of conditionals.
			
			Graphics2D g = (Graphics2D) g1;
			
			Dimension _d = getSize();
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g.setColor(Color.white);
			g.fillRect(0, 0, _d.width, _d.height);
			g.setColor(Color.black);

			Font font = new Font("Times New Roman", Font.BOLD, 24);
			FontMetrics fm = g.getFontMetrics(font);
			
			Font sFont = new Font("Times New Roman", Font.BOLD, 16);
			FontMetrics sfm = g.getFontMetrics(sFont);
			
			String choiceA = "Choice A:";
			String choiceB = "Choice B:";

			String txt = "";
			
			if (currentCount == -1){
				txt = "Press spacebar to begin Experiment.";
				if (isDemo)
					txt = "Press spacebar to begin Demo.";
				
				AttributedString a_str = new AttributedString(txt);
				a_str.addAttribute(TextAttribute.FONT, font);
				
				g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
						(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));			
			}
			else {
				long st;
				
				if (runTrial == true){
						
						//choice A text
						g.setColor(Color.blue);
						
						AttributedString a_str = new AttributedString(choiceA);
						a_str.addAttribute(TextAttribute.FONT, font);
						
						g.drawString(a_str.getIterator(), _d.width/4, 
								(int) (_d.height/4 + fm.getAscent()));
						
						g.setColor(Color.black);
						
						txt = Integer.toString(currentTrial.getChoiceAProb()) + "% chance of ";
						if (currentTrial.getIsBlockPositive())
							txt += "winning ";
						else
							txt += "losing ";
						txt += ("$" + Integer.toString(currentTrial.getChoiceAVal()) + ".");
						
						a_str = new AttributedString(txt);
						a_str.addAttribute(TextAttribute.FONT, font);
						
						g.drawString(a_str.getIterator(), _d.width/4, 
								(int) (_d.height/4 + 3*fm.getAscent()));
						
						//choice B text
						g.setColor(Color.blue);
						
						a_str = new AttributedString(choiceB);
						a_str.addAttribute(TextAttribute.FONT, font);
						
						g.drawString(a_str.getIterator(), _d.width/4, 
								(int) (_d.height/2 + fm.getAscent()));
						
						g.setColor(Color.black);
						
						txt = Integer.toString(currentTrial.getChoiceBProb()) + "% chance of ";
						if (currentTrial.getIsBlockPositive())
							txt += "winning ";
						else
							txt += "losing ";
						txt += ("$" + Integer.toString(currentTrial.getChoiceBVal()) + ".");
						
						a_str = new AttributedString(txt);
						a_str.addAttribute(TextAttribute.FONT, font);
						
						g.drawString(a_str.getIterator(), _d.width/4, 
								(int) (_d.height/2 + 3*fm.getAscent()));
						
						// instructions
						txt = "Press \"A\" or \"B.\"";
						a_str = new AttributedString(txt);
						a_str.addAttribute(TextAttribute.FONT, sFont);
						
						g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
								(int) (3*_d.height/4 + 3*sfm.getAscent()));
				}
				else {
					if (currentCount < numTrials-1){
						// Ask subject to click mouse for next trial
						txt = "Press spacebar for next trial.";
					}
					else {
						txt = "Experiment over.";
					}
					
					AttributedString a_str = new AttributedString(txt);
					a_str.addAttribute(TextAttribute.FONT, font);
					
					g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
							(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));
				}
			}// end if-else

		} // end paint

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

		public void keyPressed(KeyEvent e) {

		}

		public void keyReleased(KeyEvent e) {
			int key = e.getKeyChar();
			
			switch (key){
			case KeyEvent.VK_SPACE:
				if (!getResponse){
					if (runTrial == false)
						runTrial = true;
					else
						runTrial = false;
					
					if (threadSuspended){
						threadSuspended = false;
						synchronized(this){
							notify();
						}
					}
				}
				break;
			case 'a':
			case 'A':
			case 'b':
			case 'B':
				if (runTrial && getResponse){
					response = (char) (key|32);
					runTrial = false;
					getResponse = false;
					
					if (threadSuspended){
						threadSuspended = false;
						synchronized(this){
							notify();
						}
					}
				}
				break;
			}
		}

		public void keyTyped(KeyEvent e) {
			int key = e.getKeyChar();
		}
}