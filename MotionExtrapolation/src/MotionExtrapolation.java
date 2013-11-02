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
import java.util.Random;

//import org.apache.http.client.HttpClient;

public class MotionExtrapolation extends Applet 
	implements Runnable, KeyListener {
	
	// ------params------- //
	
	// number of expt trials
	private int numTrials;

	// is the current run a demo
	private boolean isDemo;

	// visible path length
	private final int visiblePathLength = 240; // fixed
	
	// velocity
	private final int velocity = 12; // fixed
	
	// radius of solid disc
	private int discRadius;
	
	// path radius
	private int pathRadius;
	
	// vertical range of start position
	private int orientationRange; // fixed
	
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
	
	// boolean -- check for click
	private boolean runTrial;
	
	private Trial currentTrial;
	
	// Thread
	private Thread t = null;
	
	// boolean -- is the thread suspended?
	private boolean threadSuspended;
	
	// mouse position
	private int mx, my;
	
	// circle radius
	private int r = 10;
	
	// circle position
	private int x, y;
	
	// arc position
	private int arcX, arcY;
	
	// arc Angle
	private double arcTheta;
	
	// circle movement angle
	private double theta;
	
	// circle velocity in radians;
	private double velocityRadians;
	
	// curved path center
	private int pc_x, pc_y;
	
	// circle start position angle
	private double startTheta;
	
	// counter for drawing path
	private int counter;
	
	// boolean -- check if the ball can move
	private boolean carryOn;
	
	// boolean -- has response been collected
	private boolean gotResponse;
	
	// boolean -- convexity up or down
	private boolean convexUp;
	
	boolean showBall;
	
	// init
	public void init(){
		// size would be set by the html
		setSize(800,600);
		
		String str = getParameter("numTrials");
		numTrials = (str != null)? Integer.parseInt(str): 4;
		
		str = getParameter("runType");
		isDemo = (str != null)? str.equalsIgnoreCase("demo"): false;
		
		//str = getParameter("orientationRange");
		//orientationRange = (str != null)? Integer.parseInt(str): 30;
		
		if (isDemo)
			orientationRange = 0;
		else
			orientationRange = 45;
		
		stimuli = new Stimuli(numTrials);
		
		dataLog = new DataLog();
		
		dataLog.setNumTrials(numTrials);
		dataLog.setIsDemo(isDemo);
		dataLog.setVelocity(velocity);
		dataLog.setPathLength(visiblePathLength);
		dataLog.setOrientationRange(orientationRange);
		
	    //addMouseListener( this );
	    //addMouseMotionListener( this );
	    addKeyListener(this);
	
	    randGen = new Random();
	    
		runTrial = false;
		currentCount = -1;
		

		t = new Thread(this);
		threadSuspended = false;
		t.start();
	} // end init
	
	
	// start
	public void start(){
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
				
				showBall = true;
				repaint();
				try {
			        Thread.sleep(500);
			    } catch (InterruptedException e) {}
			    
			    showBall = false;
			    repaint();
			    try {
			        Thread.sleep(200);
			    } catch (InterruptedException e) {}
			    
			    showBall = true;
				repaint();
				try {
			        Thread.sleep(500);
			    } catch (InterruptedException e) {}
				
				while (true){
					if (threadSuspended == false){
						
						if (gotResponse && checkStop())
							break;
						
						//System.out.println(x + " " + y + " " + arcX + " " + arcY + " " + my);
						
						if (!gotResponse && x + velocity*Math.cos(theta) > getSize().width/2+2*r){
							// the ball's now hidden by the half-disc
							// wait for subject to respond
							carryOn = false;
						}
						
						if (carryOn == true){
							// keep drawing
							
							if (pathRadius == 0){
								// linear path
								x = getSize().width/2 + (int)((visiblePathLength - counter*velocity)*Math.cos(startTheta));
								y = getSize().height/2 + (int)((visiblePathLength - counter*velocity)*Math.sin(startTheta));
							}
							else {
								// curved path
								if (convexUp){
									x = pc_x + (int) (pathRadius*Math.cos(startTheta - counter*velocityRadians));
									y = pc_y + (int) (pathRadius*Math.sin(startTheta - counter*velocityRadians));
								}
								else {
									x = pc_x + (int) (pathRadius*Math.cos(startTheta + counter*velocityRadians));
									y = pc_y + (int) (pathRadius*Math.sin(startTheta + counter*velocityRadians));
								}
							}
							counter += 1;
						}
						else {
							// subject is giving a response
							arcY = my;
							arcTheta = Math.asin((double)(getSize().height/2-arcY)/(discRadius+10));
							arcX = getSize().width/2 + (int) ((discRadius+10)*Math.cos(arcTheta));
							//System.out.println(arcY + " : " + arcTheta + " : " + arcX + " : " + (getSize().height/2 + (discRadius+10)));
						}
						
					    threadSuspended = true;
						
						repaint();
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
						
						try {
					        Thread.sleep(50);
					    } // Wait 100 milliseconds
					    catch (InterruptedException e) {
					    } // Ignore interruptions
						
					}
				}
				
				try {
			        Thread.sleep(1000);
			    } // Wait a second
			    catch (InterruptedException e) {
			    } // Ignore interruptions
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
		
		pathRadius = currentTrial.getPathRadius();
		
		discRadius = currentTrial.getDiscRadius();
		
		convexUp = currentTrial.getConvexUp();
		
		// allow the ball to move
		carryOn = true;
		
		// no response yet
		gotResponse = false;
		
		velocityRadians = (pathRadius == 0)? velocity: velocity*1.0/pathRadius;
		
		
		// initial position of the arc ("mitt")
		arcX = getSize().width/2 + discRadius + 10;
		arcY = getSize().height/2;
		arcTheta = 0;
		
		my = arcY; // initializing the mouse y variable.
		
		// initialize counter to 0
		counter = 0;
		
		// randomly choose angle between -orientationRange to +orientationRange in intervals of 5
		theta = Math.toRadians(randGen.nextInt(2*orientationRange/5+1)*5 - orientationRange);
		//System.out.println(Math.toDegrees(theta) + " " + theta);
		
		// initial position of the circle
		int c_x = getSize().width/2;
		int c_y = getSize().height/2;
		if (pathRadius == 0){
			// linear path
			if (theta < 0)
				startTheta = Math.PI + theta;
			else 
				startTheta = theta - Math.PI;
			
			x = c_x + (int) (visiblePathLength*Math.cos(startTheta));
			y = c_y + (int) (visiblePathLength*Math.sin(startTheta));
			// explanation: if the circle is moving at an angle of +15, the starting position
			// will be at an angle of 180+15 wrt the center of the screen.
		}
		else {
			// curved path
			convexUp = (randGen.nextInt(2) == 1)? true: false;
			
			// center of curved path
			double t; // t is the angle at which the center of the path lies wrt the center of the screen.
			if (convexUp)
				t = Math.PI/2 + theta;
			else
				t = -Math.PI/2 + theta;
			t = -t; // the y-axis is inverted
			pc_x = c_x + (int)(pathRadius*Math.cos(t));
			pc_y = c_y + (int)(pathRadius*Math.sin(t));
			
			 // length of the visible path in radians
			double visiblePathLenRadians = visiblePathLength*1.0/pathRadius;
			if (convexUp)
				startTheta = (t + Math.PI) + visiblePathLenRadians;
			else
				startTheta = (t - Math.PI) - visiblePathLenRadians;
			
			// start positions
			x = pc_x + (int) (pathRadius*Math.cos(startTheta));
			y = pc_y + (int) (pathRadius*Math.sin(startTheta));
		}
		
	}

	private void trialEnd(){
		dataLog.addDatum(new Datum(-1*Math.toDegrees(Math.atan2(arcY-getSize().height/2, arcX-getSize().width/2)), 
									-1*Math.toDegrees(Math.atan2(y-getSize().height/2, x-getSize().width/2)), 
									(int) Math.ceil(Math.toDegrees(theta)), currentTrial));
		runTrial = false;

		repaint();
		
		if (currentCount == numTrials - 1){
			_sendOutput();
			stop();
		}
	}
	
	private boolean checkStop(){
		// check if the ball and the disc intersect
		
		return (Math.sqrt(Math.pow(x-getSize().width/2, 2)+Math.pow(y-getSize().height/2, 2)) > discRadius+r+10)? true: false;
	}

	public void update (Graphics g){
		paint(g);
	}
	
	// paints the panel
	public void paint(Graphics g1){
		Graphics2D g = (Graphics2D) g1;
		
		Dimension _d = getSize();
		
		// create an off-screen image for double buffering
		BufferedImage image = new BufferedImage(_d.width, _d.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D ig = image.createGraphics();
		ig.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		ig.setColor(Color.white);
		ig.fillRect(0, 0, _d.width, _d.height);
		ig.setColor(Color.black);
		
		Font font = new Font("Times New Roman", Font.BOLD, 25);
		FontMetrics fm = ig.getFontMetrics(font);
		
		Font sFont = new Font("Times New Roman", Font.BOLD, 16);
		FontMetrics sfm = ig.getFontMetrics(sFont);
		
		String txt = "";
		
		if (currentCount == -1){
			txt = "Press spacebar to begin Experiment.";
			if (isDemo)
				txt = "Press spacebar to begin Demo.";
			
			AttributedString a_str = new AttributedString(txt);
			a_str.addAttribute(TextAttribute.FONT, font);
			
			ig.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
					(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));			
		}
		else {
			if (runTrial == false){
				long st = System.currentTimeMillis();
				while (System.currentTimeMillis() - st < 500);
				
				if (currentCount < numTrials-1){
					// Ask subject to click mouse for next trial
					txt = "Press spacebar for next trial.";
				}
				else {
					txt = "Experiment over.";
				}
				
				AttributedString a_str = new AttributedString(txt);
				a_str.addAttribute(TextAttribute.FONT, font);
				
				ig.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
						(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));
				
				// draw trial counter
//				txt = "Trial "+Integer.toString(_currentCount+1)+" of "+Integer.toString(_numTrials)+".";
//				a_str = new AttributedString(txt);
//				a_str.addAttribute(TextAttribute.FONT, sFont);
//				
//				g.drawString(a_str.getIterator(), (_d.width - sfm.stringWidth(txt)), 
//						(int) ((_d.height - sfm.getHeight()) + sfm.getAscent()));
			}
			else {
				// draw the trial
				
				// draw arc
				ig.fillArc(arcX-2*r, arcY-2*r, 5*r, 5*r, (int) (Math.toDegrees(arcTheta)) - 60, 120);
				ig.setColor(getBackground());
				ig.fillArc(arcX-2*(r-1), arcY-2*(r-1), 5*(r-1), 5*(r-1), (int) (Math.toDegrees(arcTheta)) - 75, 150);
				ig.setColor(getForeground());
				
				if (showBall)
					// draw circle
					ig.fillOval(x-r, y-r, 2*r, 2*r);
				
				// draw "gap"
				ig.setColor(Color.gray);
				ig.fillArc(_d.width/2-discRadius, _d.height/2-discRadius, 2*discRadius, 2*discRadius, 270, 180);
				
				ig.setColor(Color.black);
				AttributedString a_str = new AttributedString("\',\' = UP           \'.\' = DOWN");
				a_str.addAttribute(TextAttribute.FONT, sFont);
				
				ig.drawString(a_str.getIterator(), 5*(_d.width - sfm.stringWidth(txt))/7, 
						(int) ((13*(_d.height - sfm.getHeight())/15) + sfm.getAscent()));
				
				a_str = new AttributedString("Press Spacebar when done.");
				a_str.addAttribute(TextAttribute.FONT, sFont);
				
				ig.drawString(a_str.getIterator(), 5*(_d.width - sfm.stringWidth(txt))/7, 
						(int) ((14*(_d.height - sfm.getHeight())/15) + sfm.getAscent()));
				
				// draw trial counter
//				txt = "Trial "+Integer.toString(_currentCount+1)+" of "+Integer.toString(_numTrials)+".";
//				AttributedString a_str = new AttributedString(txt);
//				a_str.addAttribute(TextAttribute.FONT, sFont);
//				
//				g.drawString(a_str.getIterator(), (_d.width - sfm.stringWidth(txt)), 
//						(int) ((_d.height - sfm.getHeight()) + sfm.getAscent()));

			}
			
		}// end if-else
		
		g.drawImage(image, 0, 0, this);
		
		if (threadSuspended){
			threadSuspended = false;
			synchronized(this){
				notify();
			}
		}

	} // end paint

	public boolean keyDown(Event e, int key){		
		return true;
	}
	
	public void keyPressed(KeyEvent e) {

	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyChar();
		
		if (key == KeyEvent.VK_SPACE) { // spacebar
			if (runTrial == false){
				runTrial = true;
				
				if (threadSuspended){
					threadSuspended = false;
					synchronized(this){
						notify();
					}
				}
			}
			
			if (carryOn == false){
				carryOn = true;
				gotResponse = true;
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		int key = e.getKeyChar();
		
		if (key == ',' || key == KeyEvent.VK_UP){ 
			if (my <= getSize().height/2 - (discRadius+10))
				my = getSize().height/2 - (discRadius+10);
			else
				my -= 5;
		}
		if (key == '.' || key == KeyEvent.VK_DOWN){
			if (my >= getSize().height/2 + (discRadius+10))
				my = getSize().height/2 + (discRadius+10);
			else
				my += 5;
		}
		
		if (threadSuspended){
			threadSuspended = false;
			synchronized(this){
				notify();
			}
		}
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

} // end class
	