import java.applet.Applet;
import java.applet.AudioClip;
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
import java.util.HashMap;
import java.util.Random;

//import org.apache.http.client.HttpClient;

public class ToneDiscrimination extends Applet {
	
	// ------params------- //
	// number of expt trials
	private int _numTrials;

	// is the current run a demo
	private boolean _isDemo;
	
	// number of practice trials
	private int _numPracTrials;
	
	// -------the panels------ //
	
	// this is where the stimulus would be shown
	private StimuliPanel _s_panel;
	
	// this is where the response text would be displayed
	// private ResponsePanel _i_panel;
	
	// the stimuli
	private Stimuli _stimuli;

	// the log of data
	private DataLog _dl;
	
	// ------ tones -------------- //
	
	private final String[] _demoTones = {"600", "700", "800", "900", "1100", "1200", "1300", "1400"};
	private final String[] _mainTones = {"980", "985", "990", "995", "1005", "1010", "1015", "1020"};
	private final String _standardTone = "1000";
	
	// tones
	private HashMap _tones;
	
	// ------local variables------ //
	
	// the current trial
	private Trial _currentTrial;
	
	// current trial Number
	private int _currentCount;
	
	// response
	private char _response;
	
	// boolean -- check for space?
	private boolean _checkSpace;
	

	
	// init
	public void init(){
		// size would be set by the html
		
		String str = getParameter("numTrials");
		_numTrials = (str != null)? Integer.parseInt(str): 5;
		
		str = getParameter("runType");
		_isDemo = (str != null)? str.equalsIgnoreCase("demo"): true;
		
		//setSize(640,480); _numTrials=3; _isDemo=false;
		
		_tones = new HashMap();
		_tones.put(_standardTone, this.getAudioClip(getDocumentBase(), "Tones/"+_standardTone+".wav"));
		if (_isDemo) {
			for (int i=0; i<_demoTones.length; i++){
				_tones.put(_demoTones[i], this.getAudioClip(getDocumentBase(), "Tones/"+_demoTones[i]+".wav"));
			}
		}
		else {
			for (int i=0; i<_mainTones.length; i++){
				_tones.put(_mainTones[i], this.getAudioClip(getDocumentBase(), "Tones/"+_mainTones[i]+".wav"));
			}
		}
		
		_dl = new DataLog();
		
		_dl.setNumTrials(_numTrials);
		_dl.setIsDemo(_isDemo);
		
		_s_panel = new StimuliPanel();
		
		_s_panel.setIsDemo(_isDemo);
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		
		c.anchor = GridBagConstraints.CENTER;
		c.gridx = 0;
		c.gridy = 1;
		add(_s_panel, c);
		// this is where the stimulus would be seen
		
		_stimuli = new Stimuli(_numTrials, _numPracTrials, _isDemo);
	} // end init
	
	
	// start
	public void start(){
		_checkSpace = true;
		_currentCount = -1;
		
		_s_panel.setTrialNum(-1);
		_s_panel.setTrialsCount(_numTrials);
		_s_panel.setIsDemo(_isDemo);
		
		_s_panel.repaint();
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
		_currentCount = 0;
		_currentTrial = _stimuli.nextTrial();
		
		_s_panel.setTrialNum(_currentCount);
		_s_panel.setTrialsCount(_numTrials);
		_s_panel.setIsDemo(_isDemo);
		_s_panel.setStandard((AudioClip)_tones.get(_currentTrial.getStandard()));
		_s_panel.setComparison((AudioClip)_tones.get(_currentTrial.getComparison()));

		_s_panel.repaint();
	}
	
	// draw next Trial
	public void _drawNextTrial(){
		_currentTrial = _stimuli.nextTrial();
		
		if (_currentTrial != null){		
			_s_panel.setTrialNum(_currentCount);
			_s_panel.setTrialsCount(_numTrials);
			_s_panel.setIsDemo(_isDemo);
			_s_panel.setStandard((AudioClip)_tones.get(_currentTrial.getStandard()));
			_s_panel.setComparison((AudioClip)_tones.get(_currentTrial.getComparison()));
			
			_s_panel.repaint();
		}
	}
	
	// sends output to the server -- a helper
	private void _sendOutput(){
		System.out.println(_dl.generateLog());
		try {
			String filePath = getParameter("filePath");
			String msg = "fp="+filePath+"&output="+_dl.generateLog();
			
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

	
	// checks for keyDown events
	public boolean keyDown(Event e, int key){
		boolean validKey = false;
		if (e.id == Event.KEY_PRESS){		
			// check if a valid key is pressed
			switch (key){
				case 32: // Spacebar
					if (_checkSpace){
						if (_currentCount == -1){
							_setUp();
						}
						_checkSpace = false;
					}
					break;				
				case 'h':
					_response = 'h';
					validKey = true;
					break;					
				case 'l':
					_response = 'l';
					validKey = true;
					break;
			} // end switch
			
			if (validKey){
				_dl.addDatum(new Datum(_currentTrial, _response));
					
				++_currentCount;
				if (_currentCount == _numTrials){
						_sendOutput();
						stop();
				}
				else {					
						_drawNextTrial();
				}
			}
		} // end KEY_PRESS check 
		
		return validKey;
	} // end keyDown
	
	

} // end class
