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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
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

public class COMTask extends Applet 
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
	
	private Thread t = null;
	
	// is the thread suspended?
	private boolean threadSuspended;
	
	// has the user given response?
	private boolean gotResponse;
	
	// run the trial
	private boolean runTrial;
	
	// current trial
	private Trial currentTrial;
	
	// area of the trapezoid
	private final int SIZE = 15000;
	
	private int[] xpoints = {0, 0, 0, 0}; 
	// bad code alert!
	// hack. used because the array is of a fixed size.
	
	private int[] ypoints = {0, 0, 0, 0};
	// ditto!
	
	// width of the base
	private int baseWidth;
	
	// height of the trapezoid
	private int height;
	
	// base angle of the trapezoid
	private int BASE_ANGLE = 82;
	
	// y-coord of the COM
	private int trueValue;
	
	// user response
	private int response;
	
	// init
	public void init(){
		// size would be set by the html
		setSize(800,600);
		
		String str = getParameter("numTrials");
		numTrials = (str != null)? Integer.parseInt(str): 16;
		
		str = getParameter("runType");
		isDemo = (str != null)? str.equalsIgnoreCase("demo"): true;
		
		addKeyListener(this);
		
		stimuli = new Stimuli(numTrials);
		
		dataLog = new DataLog();
		
		dataLog.setNumTrials(numTrials);
		dataLog.setIsDemo(isDemo);
		
	    randGen = new Random();
	    
		currentCount = -1;
		
		t = new Thread(this);
		threadSuspended = false;
		t.start();
	} // end init
	
	
	// start
	public void start(){
		runTrial = false;
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
				
				while (true){
					if (threadSuspended == false){					
						if (gotResponse)
							break;
						
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
						
						repaint();
						
						try {
					        Thread.sleep(50);
					    } // Wait 50 milliseconds
					    catch (InterruptedException e) {
					    } // Ignore interruptions
						
					}
				} // infinite loop ends
				
				try {
			        Thread.sleep(500);
			    } // Wait half a second
			    catch (InterruptedException e) {
			    } // Ignore interruptions
			    
				trialEnd();
			}
			else {
//				repaint();
				
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
		
		gotResponse = false;
		
		double aspectRatio = currentTrial.getAspectRatio();
		
		// let b be the base of the trapezoid, and h the height.
		// the area of the trap is given by: 1/2 * h * (b + a)
		// Also, b = a + 2h/tan(theta)
		// Therefore, area = 1/2 * ar*b * (2b - 2ar*b/tan(theta))
		//                 = (ar - ar^2/tan(theta)) * b^2
		
		double coeff = (aspectRatio - aspectRatio*aspectRatio/Math.tan(Math.toRadians(BASE_ANGLE)));
		int b = (int) (Math.sqrt(SIZE/coeff));
		int a = (int) (b - 2*aspectRatio*b/Math.tan(Math.toRadians(BASE_ANGLE)));
		
		if (currentTrial.getBaseDown() == true)
			baseWidth = b;
		else
			baseWidth = a;
		
		height = (int) (aspectRatio * b);
		
		// computing the COM
		// assuming the base is the bigger side
		// and the origin lies at the bottom-left vertex
		int com_x = baseWidth/2;
		int com_y = height/3 * (2*a + b)/(a + b);
		
		// if the smaller side is the base, change the y-coordinate of the COM to reflect that.
		if (currentTrial.getBaseDown() == false)
			com_y = height - com_y;
		
		trueValue = com_y;

		if (currentTrial.getInitialPosition())
			response = 4;
		else
			response = height-4;

		constructPolygon();
		repaint();
	}
	
	private void constructPolygon(){
		int hypotenuse = (int) (height / Math.sin(Math.toRadians(BASE_ANGLE)));
		
		xpoints[0] = getSize().width/2;
		ypoints[0] = 3*getSize().height/4;

		xpoints[1] = xpoints[0] + baseWidth;
		ypoints[1] = ypoints[0];

		if (currentTrial.getBaseDown()){
			xpoints[2] = xpoints[1] + (int)(hypotenuse * Math.cos(Math.toRadians(180 - BASE_ANGLE)));
			ypoints[2] = ypoints[1] + (int)(hypotenuse * Math.sin(-1*Math.toRadians(180 - BASE_ANGLE)));
			
			xpoints[3] = xpoints[0] + (int)(hypotenuse * Math.cos(Math.toRadians(BASE_ANGLE)));
			ypoints[3] = ypoints[0] + (int)(hypotenuse * Math.sin(-1*Math.toRadians(BASE_ANGLE)));
		}
		else {
			xpoints[2] = xpoints[1] + (int)(hypotenuse * Math.cos(Math.toRadians(BASE_ANGLE)));
			ypoints[2] = ypoints[1] + (int)(hypotenuse * Math.sin(-1*Math.toRadians(BASE_ANGLE)));
			
			xpoints[3] = xpoints[0] + (int)(hypotenuse * Math.cos(Math.toRadians(180 - BASE_ANGLE)));
			ypoints[3] = ypoints[0] + (int)(hypotenuse * Math.sin(-1*Math.toRadians(180 - BASE_ANGLE)));
		}
	}

	private void trialEnd(){
		dataLog.addDatum(new Datum(response, currentTrial.getAspectRatio(), currentTrial.getBaseDown(), 
									currentTrial.getInitialPosition(), trueValue, baseWidth, height));
		
		if (currentCount == numTrials - 1){
			_sendOutput();
			stop();
		}
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

		Font font = new Font("Times New Roman", Font.BOLD, 24);
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
			if (runTrial == true){
				// center of screen
				int c_x = _d.width/2;
				int c_y = 3*_d.height/4;
				
				// draw table first
				ig.setColor(Color.gray);
				ig.fillRect(c_x - 20, c_y, c_x+20, 15);
				ig.fillRect(c_x-5, c_y+10, 10, c_y-10);
				
				ig.setColor(Color.black);
				ig.fillPolygon(xpoints, ypoints, 4);
				
				ig.setColor(Color.white);
				ig.fillOval(c_x+baseWidth/2-2, c_y-response-2, 4, 4);
				
				ig.setColor(Color.black);
				AttributedString a_str = new AttributedString("\',\' = UP              \'.\' = DOWN");
				a_str.addAttribute(TextAttribute.FONT, sFont);
				
				ig.drawString(a_str.getIterator(), 3*(_d.width - sfm.stringWidth(txt))/5, 
						(int) ((13*(_d.height - sfm.getHeight())/15) + sfm.getAscent()));
				
				a_str = new AttributedString("Press Spacebar when done.");
				a_str.addAttribute(TextAttribute.FONT, sFont);
				
				ig.drawString(a_str.getIterator(), 3*(_d.width - sfm.stringWidth(txt))/5, 
						(int) ((14*(_d.height - sfm.getHeight())/15) + sfm.getAscent()));
			}
			else {
				long st = System.currentTimeMillis();
				while (System.currentTimeMillis() - st < 250);
				
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
		
		if (key == KeyEvent.VK_SPACE) { // spacebar
			if (runTrial == true){
				// user gave a response
				runTrial = false;
				gotResponse = true;
			}
			else {
				runTrial = true;
			}	
		}
		
		if (threadSuspended){
			threadSuspended = false;
			synchronized(this){
				notify();
			}
		}

	}

	public void keyTyped(KeyEvent e) {
		int key = e.getKeyChar();
		
		if (key == ',' || key == KeyEvent.VK_UP){
			if (response + 5 < height-4)
				response += 5;
			else
				response = height-4;
		}
		if (key == '.' || key == KeyEvent.VK_DOWN){
			if (response - 5 > 4)
				response -= 5;
			else
				response = 4;
		}
		
		if (threadSuspended){
			threadSuspended = false;
			synchronized(this){
				notify();
			}
		}
	}

} // end class