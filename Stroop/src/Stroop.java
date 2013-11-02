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
import java.util.ArrayList;
import java.util.Random;

//import org.apache.http.client.HttpClient;

public class Stroop extends Applet {
	
	// ------params------- //
	// number of expt trials
	private int numTrials;

	// is the current run a demo
	//private boolean _isDemo = true;
	private boolean isDemo;
	
	// number of practice trials
	private int numPracTrials;
	
	// -------the panels------ //
	
	// this is where the stimulus would be shown
	private StimuliPanel s_panel;
	
	// this is where the response text would be displayed
	private ResponsePanel i_panel;
	
	// this is where the "result" for the demo would be shown
	private ResultPanel r_panel;
	
	// the stimuli
	private Stimuli stimuli;
	
	// the log of data
	private DataLog dataLog;
	
	// ------local variables------ //
	
	// the current trial
	private Trial currentTrial;
	
	// current trial Number
	private int currentCount;
	
	// variables to get the RT
	private long startTime;
	private long endTime;
	
	// the result of the current trial
	private boolean result;
	
	// boolean -- check for space?
	private boolean checkSpace;
	
	// boolean -- acknowledged end of practice?
	private boolean acknowledged;
	
	// -------methods------- //
	
	// init
	public void init(){
		// size would be set by the html
		setSize(800,600);
		
		String str = getParameter("numTrials");
		numTrials = (str != null)? Integer.parseInt(str): 10;
		
		str = getParameter("runType");
		isDemo = (str != null)? str.equalsIgnoreCase("demo"): false;
		//setSize(500,400); _numTrials=1; _isDemo=false;
		
		if (isDemo) {
			numPracTrials = 0;
			acknowledged = true;
		}
		else { 
			numPracTrials = 0;
			acknowledged = false;
		}
		
		dataLog = new DataLog();
		
		dataLog.setNumTrials(numTrials);
		dataLog.setIsDemo(isDemo);
		
		s_panel = new StimuliPanel();
		i_panel = new ResponsePanel();
		r_panel = new ResultPanel();
		
		s_panel.setIsDemo(isDemo);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;

		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.PAGE_END;
		add(i_panel, c);
		// the text for the response (which key to press)
		
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 1;
		add(s_panel, c);
		// this is where the stimulus would be seen
		
		c.anchor = GridBagConstraints.PAGE_START;
		c.gridx = 0;
		c.gridy = 0;
		add(r_panel, c);
		// to show results in the demo version
		
		stimuli = new Stimuli(numTrials, numPracTrials, isDemo);
	} // end init
	
	
	// start
	public void start(){
		checkSpace = true;
		currentCount = -1;
		currentTrial = stimuli.nextTrial();
		
		s_panel.setColor(currentTrial.getColor());
		s_panel.setText(currentTrial.getText());
		
		s_panel.repaint();
	} // end start
	
	
	//stop
	public void stop(){
		try{
	        // redirect to that finish page
			getAppletContext().showDocument(new URL(getCodeBase(),"finis.php"), "_self");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} 	
		//System.exit(0);
	}
	
	// set up the experiment
	private void _setUp(){
		i_panel.setState(true);
		i_panel.repaint();
		
		currentCount = 0;
		currentTrial = stimuli.nextTrial();
		
		s_panel.setColor(currentTrial.getColor());
		s_panel.setText(currentTrial.getText());
		
		s_panel.repaint();		 
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
	
	// draw next Trial
	public void _drawNextTrial(){
		currentTrial = stimuli.nextTrial();
		
		if (currentTrial != null){
			if (isDemo){
				if (result == true)
					r_panel.setText("Correct!");
				else
					r_panel.setText("Incorrect!");
				
				r_panel.repaint();
			}
			s_panel.setColor(currentTrial.getColor());
			s_panel.setText(currentTrial.getText());
			s_panel.repaint();
			
			i_panel.repaint();
			
			repaint();
			
			startTime = System.currentTimeMillis(); // reset clock
		}
	}
	
	// checks for keyDown events
	public boolean keyDown(Event e, int key){
		boolean validKey = false;
		
		if (e.id == Event.KEY_PRESS){
			Color lastColor = currentTrial.getColor();
			
			// check if a valid key is pressed
			switch (key){
				case 32: // Spacebar
					if (checkSpace){
						if (currentCount == -1){
							_setUp();
							startTime = System.currentTimeMillis(); // start clock
						} 
						else if (currentCount == numPracTrials && acknowledged == false){
							i_panel.setState(true);
							_drawNextTrial();
						}
						checkSpace = false;
					}
					break;				
				case '1':
					if (!checkSpace){
					endTime = System.currentTimeMillis(); // end clock
					
					if (lastColor.equals(Color.red)) // red
						result = true;
					else
						result = false;
					validKey = true;
					}
					break;
					
				case '2':
					if (!checkSpace){
					endTime = System.currentTimeMillis(); // end clock
					
					if (lastColor.equals(Color.green)) // green
						result = true;
					else
						result = false;
					validKey = true;
					}
					break;
					
				case '3':
					if (!checkSpace){
					endTime = System.currentTimeMillis(); // end clock
					
					if (lastColor.equals(Color.blue)) // blue
						result = true;
					else
						result = false;
					validKey = true;
					}
					break;
					
				case '4':
					if (!checkSpace){
					endTime = System.currentTimeMillis(); // end clock
					
					if (lastColor.equals(Color.black)) // black
						result = true;
					else
						result = false;
					validKey = true;
					}
					break;
			} // end switch
			
			if (validKey == true){
				// a valid response was pressed
				if (currentCount >= numPracTrials)
					dataLog.addDatum(new Datum(currentTrial, (endTime - startTime), result));
				
				++currentCount;
				if (currentCount == numPracTrials && acknowledged == false){
					checkSpace = true;
					
					s_panel.setColor(Color.black);
					s_panel.setText("End of Practice trials.");
					s_panel.repaint();
					
					i_panel.setState(false);
					i_panel.repaint();
					repaint();
				}
				else if (currentCount == numTrials+numPracTrials){
					_sendOutput();
					stop();
				}
				else {					
					_drawNextTrial();
				}
			} // end validKey check
		} // end KEY_PRESS check 
		
		return validKey;
	} // end keyDown

} // end class
