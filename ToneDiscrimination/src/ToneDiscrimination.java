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
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//import org.apache.http.client.HttpClient;

public class ToneDiscrimination extends Applet 
	implements Runnable, KeyListener {
	
	// ------params------- //
	// number of expt trials
	private int numTrials;

	// is the current run a demo
	private boolean isDemo;
	
	// -- helper classes -- //
	
	// the stimuli
	private Stimuli stimuli;

	// the log of data
	private DataLog dataLog;
	
	// ------ tones ------ //
	
	private final String[] _demoTones = {"600", "700", "800", "900", "1100", "1200", "1300", "1400"};
	private final String[] _mainTones = {"980", "985", "990", "995", "1005", "1010", "1015", "1020"};
	private final String _standardTone = "1000";
	
	// tones
	private HashMap tones;
	
	// ------local variables------ //
	
	// the current trial
	private Trial currentTrial;
	
	// current trial Number
	private int currentCount;
	
	// response
	private char response;
	
	// standard tone
	private AudioClip standard;
	
	// comparison tone
	private AudioClip comparison;
	
	Thread t;
	
	boolean threadSuspended;
	
	boolean runTrial;
	
	boolean getResponse;
	
	boolean playStandard;
	
	boolean playComparison;
	
	// init
	public void init(){
		// size would be set by the html
		setSize(800,600);
		
		String str = getParameter("numTrials");
		numTrials = (str != null)? Integer.parseInt(str): 3;
		
		str = getParameter("runType");
		isDemo = (str != null)? str.equalsIgnoreCase("demo"): true;
		
		//setSize(640,480); _numTrials=3; _isDemo=false;
		
		tones = new HashMap();
		tones.put(_standardTone, this.getAudioClip(getDocumentBase(), "Tones/"+_standardTone+".wav"));
		if (isDemo) {
			for (int i=0; i<_demoTones.length; i++){
				tones.put(_demoTones[i], this.getAudioClip(getDocumentBase(), "Tones/"+_demoTones[i]+".wav"));
			}
		}
		else {
			for (int i=0; i<_mainTones.length; i++){
				tones.put(_mainTones[i], this.getAudioClip(getDocumentBase(), "Tones/"+_mainTones[i]+".wav"));
			}
		}
		
		addKeyListener(this);
		
		dataLog = new DataLog();
		
		dataLog.setNumTrials(numTrials);
		dataLog.setIsDemo(isDemo);
		
		currentCount = -1;
		
		stimuli = new Stimuli(numTrials, isDemo);
		
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
				
				playStandard = true;
				repaint();
				// wait for it...
				try {
			        Thread.sleep(1500);
			    }
			    catch (InterruptedException e) {
			    } // Ignore interruptions
			    playStandard = false;
			    
			    playComparison = true;
			    repaint();
				// wait for it...
			    try {
			        Thread.sleep(1500);
			    }
			    catch (InterruptedException e) {
			    } // Ignore interruptions
			    playComparison = false;
			    
			    getResponse = true;
			    // wait for response
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
			    getResponse = false;
				
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
	
	// initialize trial
	private void trialInit(){		
		currentCount++;
		currentTrial = stimuli.nextTrial();
		
		standard = (AudioClip)tones.get(currentTrial.getStandard()); // artifact
		comparison = (AudioClip)tones.get(currentTrial.getComparison());

		playStandard = false;
		playComparison = false;
		getResponse = false;
	}
	
	// end Trial
	public void trialEnd(){
		dataLog.addDatum(new Datum(currentTrial, response));
		if (currentCount == numTrials - 1){
			repaint();
			_sendOutput();
			stop();
		}
	}
	
	public void update(Graphics g){
		paint(g);
	}

	// paints the panel
	public void paint(Graphics g1){
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
			txt = "Press Spacebar to begin Experiment.";
			if (isDemo)
				txt = "Press Spacebar to begin Demo.";
			
			AttributedString a_str = new AttributedString(txt);
			a_str.addAttribute(TextAttribute.FONT, font);
			
			g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
					(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));			
		}
		else {
			if (runTrial == true){
				AttributedString a_str;
				
				if (playStandard){
					// Draw "Standard" and play it.
					txt = "1";
					a_str = new AttributedString(txt);
					a_str.addAttribute(TextAttribute.FONT, font);
					
					g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
							(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));
					
		//			txt = "Trial "+Integer.toString(currentCount+1)+" of "+Integer.toString(numTrials)+".";
		//			a_str = new AttributedString(txt);
		//			a_str.addAttribute(TextAttribute.FONT, sFont);
		//			
		//			g.drawString(a_str.getIterator(), (_d.width - sfm.stringWidth(txt)), 
		//					(int) ((_d.height - sfm.getHeight()) + sfm.getAscent()));
					
					standard.play();
				}
				if (playComparison){
					// Draw "Comparison" and play it.
					txt = "2";
					
					a_str = new AttributedString(txt);
					a_str.addAttribute(TextAttribute.FONT, font);
					
					g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
							(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));
					
		//			txt = "Trial "+Integer.toString(currentCount+1)+" of "+Integer.toString(numTrials)+".";
		//			a_str = new AttributedString(txt);
		//			a_str.addAttribute(TextAttribute.FONT, sFont);
		//			
		//			g.drawString(a_str.getIterator(), (_d.width - sfm.stringWidth(txt)), 
		//					(int) ((_d.height - sfm.getHeight()) + sfm.getAscent()));
					
					comparison.play();
				}
				if (getResponse){
					txt = "Was Tone 2 higher or lower than Tone 1?";
					a_str = new AttributedString(txt);
					a_str.addAttribute(TextAttribute.FONT, font);
					
					g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
							(int) (((_d.height - fm.getHeight())/2) + fm.getAscent()));
					
					txt = "Press \"H\" or \"L.\"";
					a_str = new AttributedString(txt);
					a_str.addAttribute(TextAttribute.FONT, font);
					
					g.drawString(a_str.getIterator(), (_d.width - fm.stringWidth(txt))/2, 
							(int) (((_d.height - fm.getHeight())/2) + 3*fm.getAscent()));
					
		//			txt = "Trial "+Integer.toString(currentCount+1)+" of "+Integer.toString(numTrials)+".";
		//			a_str = new AttributedString(txt);
		//			a_str.addAttribute(TextAttribute.FONT, sFont);
		//			
		//			g.drawString(a_str.getIterator(), (_d.width - sfm.stringWidth(txt)), 
		//					(int) ((_d.height - sfm.getHeight()) + sfm.getAscent()));
					
				}
			}
			else {
				if (currentCount < numTrials-1){
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

	public void keyReleased(KeyEvent e){
		int key = e.getKeyChar();
		
		// check if a valid key is pressed
		switch (key){
			case 32: // Spacebar
				if (!getResponse){
					if (runTrial == false){
						runTrial = true;
	
						if (threadSuspended){
							threadSuspended = false;
							synchronized(this){
								notify();
							}
						}
					}
					else
						runTrial = false;
				}
				break;				
			case 'h':
			case 'H':
			case 'l':
			case 'L':
				if (runTrial && getResponse){
					response = (char) (key|32);
					runTrial = false;

					if (threadSuspended){
						threadSuspended = false;
						synchronized(this){
							notify();
						}
					}
				}
				break;
		} // end switch 	
		
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

	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

} // end class
