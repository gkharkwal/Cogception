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
import java.util.Random;

//import org.apache.http.client.HttpClient;

public class AverageDisc extends Applet 
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
	
	private int numDiscs;
	
	private int sdDiscs = 5; // default value, used to generate masks
	
	private ArrayList discs;
	
	private ArrayList[] masks;
	
	private final int NUM_MASKS = 4;
	
	private final int NUM_MASK_DISCS = 100;
	
	private final int DISC_SIZE_MEAN = 20;
	
	private final int GRID_SIZE = 300;
	
	// Ugh, java 1.4 doesn't have enums!
	// Have to use a lot of booleans instead!
	
	private boolean drawStimulus;
	private boolean drawMask;
	private boolean drawTest;
	private boolean drawQuestion;
	
	// init
	public void init(){
		// size would be set by the html
		setSize(800,600);
		
		String str = getParameter("numTrials");
		numTrials = (str != null)? Integer.parseInt(str): 28;
		
		str = getParameter("runType");
		isDemo = (str != null)? str.equalsIgnoreCase("demo"): false;
		
		addKeyListener(this);
		
		stimuli = new Stimuli(numTrials);
		
		dataLog = new DataLog();
		
		dataLog.setNumTrials(numTrials);
		dataLog.setIsDemo(isDemo);
		dataLog.setMeanDiscSize(DISC_SIZE_MEAN);
		
	    randGen = new Random();
	    randGen.setSeed(System.currentTimeMillis());
	    
	    discs = new ArrayList();
	    
	    masks = new ArrayList[NUM_MASKS];
	    for (int i=0; i<NUM_MASKS; i++){
	    	masks[i] = new ArrayList();
	    }
	    
		currentCount = -1;;
		
		t = new Thread(this);
		threadSuspended = false;
		t.start();
	} // end init
	
	
	// start
	public void start(){
		if (currentCount == -1){
			runTrial = false;
			
			generateMasks();
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

				drawStimulus = true;
				repaint();
				// wait for it...
				try {
			        Thread.sleep(500);
			    }
			    catch (InterruptedException e) {
			    }
			    drawStimulus = false;
			    
			    // draw a blank screen
			    repaint();
			    // wait for it...
				try {
					if (isDemo)
						Thread.sleep(500);
					else
						Thread.sleep(300);
			    }
			    catch (InterruptedException e) {
			    }
			    
		    	drawMask = true;
			    repaint();
			    // wait for it...
				try {
			        Thread.sleep(100);
			    }
			    catch (InterruptedException e) {
			    }
			    drawMask = false;
			    
			    // draw a blank screen
			    repaint();
			    // wait for it...
				try {
					Thread.sleep(500);
			    }
			    catch (InterruptedException e) {
			    }
			    
			    drawTest = true;
			    repaint();
			    // wait for it...
				try {
					if (isDemo)
						Thread.sleep(500);
					else
						Thread.sleep(300);
			    }
			    catch (InterruptedException e) {
			    }
			    drawTest = false;
			    
			    // draw a blank screen
			    repaint();
			    // wait for it...
				try {
			        Thread.sleep(500);
			    }
			    catch (InterruptedException e) {
			    }
			    
			    drawQuestion = true;
			    repaint();
			    
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
				drawQuestion = false;
				
				
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
	
	private void generateDiscs(int n, ArrayList discs){
		int numRows = (int) Math.sqrt(n);
		int cellSize = GRID_SIZE / numRows;
		
		// reseed the random generator
		randGen.setSeed(System.currentTimeMillis());
		
		// clear the list of discs
		discs.clear();
		
		// set discs' radii first
		int radiusSum = 0;
		for (int i=0; i<n-1; i++){
			Disc d = new Disc();
			d.setRadius(DISC_SIZE_MEAN + (int)(sdDiscs*randGen.nextGaussian()));
			radiusSum += d.getRadius();
			discs.add(d);
		}
		// nth disc should have a radius such that the mean is constant
		Disc d = new Disc();
		d.setRadius(DISC_SIZE_MEAN*n - radiusSum);
		discs.add(d);
		
		double theta = Math.toRadians(randGen.nextInt(360));
		//double theta = Math.toRadians(0);
		theta = randGen.nextInt(2) == 0? theta: -1*theta;
		
		// coordinates of the center of the screen
		int c_x = getSize().width/2;
		int c_y = getSize().height/2;
		
		// set discs' center.
		for (int i=0; i<n; i++){
			//System.out.print(i + ":\t");
			int x, y;
			
			// assuming that the top-left corner of the grid is at (0,0)
			// and the disc is at the center of the cell
			x = cellSize/2 + i%numRows*cellSize;
			y = cellSize/2 + i/numRows*cellSize;
			
			// translating the cell such that the whole grid is now centered at the center of the screen
			x = x - numRows/2*cellSize - numRows%2*cellSize/2 + c_x;
			y = y - numRows/2*cellSize - numRows%2*cellSize/2 + c_y;
			
			
			// adding noise now
			int freeSpace = cellSize/2 - ((Disc) discs.get(i)).getRadius();
			x = x + (freeSpace/2 + (int)(freeSpace/2*randGen.nextGaussian()));
			y = y + (freeSpace/2 + (int)(freeSpace/2*randGen.nextGaussian()));
			
			// the result still looks too much like a square grid
			// solution: 
			// 1) rotate the coordinates in a manner that reflects the rotation of the whole grid about
			// its center.
			// 2) ``explode'' it, so that the grid is more circular than square.
			
			int j=0;
			for (; j<numRows/2; j++){
				if (i%numRows == j || i%numRows == numRows - j-1 || i/numRows == j || i/numRows == numRows - j-1)
					break;
			}
			
			double cornerDist = Math.sqrt(2) * (GRID_SIZE/2 - cellSize/2 - j*cellSize);
			
			double dist = Math.sqrt((x-c_x)*(x-c_x) + (y-c_y)*(y-c_y));
			
			double offset = (cornerDist - dist) + ((cornerDist - dist)/2 * randGen.nextGaussian());

			
			double initialAngle = Math.atan2((y-c_y), (x-c_x));
			
			x = c_x + (int) ((dist+offset)*Math.cos(initialAngle + theta));
			y = c_y + (int) ((dist+offset)*Math.sin(initialAngle + theta));

			// set disc coordinates
			((Disc) discs.get(i)).setX(x);
			((Disc) discs.get(i)).setY(y);
		}
	}
	
	private void generateMasks(){
		for (int i=0; i<NUM_MASKS; i++){
			do{
				generateDiscs(NUM_MASK_DISCS, masks[i]);
			} while (!validateMask(masks[i]));
		}
	}
	
	private boolean validateDiscs(int n, ArrayList discs){
		int numRows = (int) Math.sqrt(n);
		
		// warning: c-esque (obfuscated) code below
		for (int i=0; i<n; i++){
			// check if the disc is too big or too small
			if (!validate((Disc) discs.get(i)))
				return false;
			
			// check if the disc intersects with its neighbours
			if (i%numRows != 0 && !validate((Disc)discs.get(i), (Disc)discs.get(i-1)))
				return false;
			if (i%numRows != numRows-1 && !validate((Disc)discs.get(i), (Disc)discs.get(i+1)))
				return false;
			if (i/numRows != 0 && !validate((Disc)discs.get(i), (Disc)discs.get(i-numRows)))
				return false;
			if (i/numRows != numRows-1 && !validate((Disc)discs.get(i), (Disc)discs.get(i+numRows)))
				return false;
		}
		return true;
	}
	
	private boolean validateMask(ArrayList m){
		for (int i=0; i<NUM_MASK_DISCS; i++){
			if (!validate((Disc) m.get(i)))
				return false;
		}
		return true;
	}
	
	private boolean validate(Disc d){
		return (d.getRadius() > (DISC_SIZE_MEAN - 3*sdDiscs)) && (d.getRadius() < (DISC_SIZE_MEAN + 3*sdDiscs));
	}
	
	private boolean validate(Disc dI, Disc dJ){
		// check if the ith and the jth discs intersect
		double dist = Math.sqrt((dI.getX()-dJ.getX())*(dI.getX()-dJ.getX()) + (dI.getY()-dJ.getY())*(dI.getY()-dJ.getY()));
		return dist > dI.getRadius() + dJ.getRadius();
	}
	
	
	private void trialInit() {
		currentCount ++;		
		currentTrial = stimuli.nextTrial();
		
		numDiscs = currentTrial.getNumDiscs();
		sdDiscs = currentTrial.getSDDiscs();
		
		do{
			generateDiscs(numDiscs, discs);
		} while (!validateDiscs(numDiscs, discs));
		
		drawStimulus = drawTest = drawMask = drawQuestion = false;
		
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
				if (drawStimulus){
					// first screen
					for (int i=0; i<numDiscs; i++){
						Disc d = (Disc) discs.get(i);
						g.fillOval(d.getX()-d.getRadius(), d.getY()-d.getRadius(), 2*d.getRadius(), 2*d.getRadius());
					}
				}
				else if (drawMask){
					// mask
					int maskIndex = randGen.nextInt(NUM_MASKS);
					for (int i=0; i<NUM_MASK_DISCS; i++){
						Disc d = (Disc) masks[maskIndex].get(i);
						g.fillOval(d.getX()-d.getRadius(), d.getY()-d.getRadius(), 2*d.getRadius(), 2*d.getRadius());
					}
				}
				else if (drawTest){
					// show test disc
					int tsize = currentTrial.getTestDiscSize();
					g.fillOval(_d.width/2-tsize, _d.height/2-tsize, 2*tsize, 2*tsize);
				}
				else if (drawQuestion){
					// ask for response
					txt = "Was test disc size smaller or larger than average disc size?";
					AttributedString a_str = new AttributedString(txt);
					a_str.addAttribute(TextAttribute.FONT, font);
					
					g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
							(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));
					
					txt = "Press \"S\" or \"L\"";
					a_str = new AttributedString(txt);
					a_str.addAttribute(TextAttribute.FONT, font);
					
					g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
							(int) (((_d.height - fm.getHeight())/2) + 3*fm.getAscent()));
				}
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
		case 's':
		case 'S':
		case 'l':
		case 'L':
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

} // end class