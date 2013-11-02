/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */


public class Trial {
	
	private boolean isBlockPositive;
	private boolean isARisky;
	
	private int choiceAProb;
	private int choiceAVal;
	
	private int choiceBProb;
	private int choiceBVal;
		
	public Trial(){}
	
	public Trial(boolean ibp, boolean iar, int cap, int cav, int cbp, int cbv){
		isBlockPositive = ibp;
		isARisky		= iar;
		choiceAProb		= cap;
		choiceAVal		= cav;
		choiceBProb		= cbp;
		choiceBVal		= cbv;
	}
	
	// getters:
	public boolean getIsBlockPositive(){ return isBlockPositive; }
	public boolean getIsARisky(){ return isARisky; }
	public int getChoiceAProb() { return choiceAProb; }
	public int getChoiceAVal() { return choiceAVal; }
	public int getChoiceBProb() { return choiceBProb; }
	public int getChoiceBVal() { return choiceBVal; }
}
