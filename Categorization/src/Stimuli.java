/**
 * Code created for Cognition and Perception labs in Fall '12 and Spring '13.
 * The project was sanctioned by the Department of Psychology, and guided by Manish Singh.
 *  
 * The @author - Gaurav Kharkwal - would like to claim copyright on this code, 
 * but leaves it open for academic use.
 * Taking this code, modifying it, and subsequently claiming it as their own would earn the 
 * person bad karma and is thus not recommended. 
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Stimuli {
											//elongated, wide, filled
	final boolean[][][] complexity6List = { { {false, false, false}, {true, true, true} }, 
											{ {false, false, true}, {true, true, false} }, 
											{ {false, true, false}, {true, false, true} }, 
											{ {false, true, true}, {true, false, false} } };
	
	final boolean[][][] complexity5List = { { {false, true, true}, {true, true, false} },
											{ {false, true, true}, {true, false, true} },
											{ {false, true, false}, {false, false, true} },
											{ {false, true, false}, {true, true, true} },
											{ {false, true, true}, {false, false, false} },
											{ {false, true, false}, {true, false, false} },
											{ {false, false, true}, {true, true, true} },
											{ {false, false, true}, {true, true, false} },
											{ {false, false, false}, {true, true, false} },
											{ {false, false, false}, {true, false, true} },
											{ {true, true, true}, {true, false, false} },
											{ {true, true, false}, {true, false, true} } };
	
	final boolean[][][] complexity2List = { { {false, true, true}, {false, true, false} },
											{ {false, true, true}, {false, false, true} },
											{ {false, true, true}, {true, true, true} },
											{ {true, true, false}, {true, false, false} },
											{ {true, false, true}, {true, false, false} },
											{ {false, true, false}, {false, false, false} },
											{ {false, true, false}, {true, true, false} },
											{ {false, false, true}, {true, true, true} },
											{ {false, false, true}, {false, false, false} },
											{ {false, false, false}, {true, false, false} },
											{ {true, true, true}, {true, true, false} },
											{ {true, true, true}, {true, false, true} } };
	
	int numBlocks;
	int currentCount;
	
	ArrayList trials; // for backward compatibility with java 1.4.2 -- can't specify type of object in list
	
	Random randGen;
	
	Stimuli(int n){
		trials = new ArrayList();
		
		// 3 complexity X 2 parity, and 8 per block
		// n should be a multiple of 48
		
		ArrayList c2 = new ArrayList();
		ArrayList c5 = new ArrayList();
		ArrayList c6 = new ArrayList();
		
		randGen = new Random();
		
		n = (n/8 > 0? n/8: 1);
		numBlocks = n;
		
		Integer next;
		
		for (int i=0; i<n; i++){
			if (i%6 == 0 || i%6 == 1){
				// pick one from complexity2List
				while(true){
					next = new Integer(randGen.nextInt(12));
					if (!c2.contains(next)){
						c2.add(next);
						break;
					}
				}
				
				if (i%2 == 0)
					trials.add(new Block(complexity2List[next.intValue()], true, 2));
				else
					trials.add(new Block(complexity2List[next.intValue()], false, 2));
			}
			else if (i%6 == 2 || i%6 == 3){
				// pick one from complexity5List
				while(true){
					next = new Integer(randGen.nextInt(12));
					if (!c5.contains(next)){
						c5.add(next);
						break;
					}
				}
				
				if (i%2 == 0)
					trials.add(new Block(complexity5List[next.intValue()], true, 5));
				else
					trials.add(new Block(complexity5List[next.intValue()], false, 5));
			}
			else {
				// pick one from complexity6List
				while(true){
					next = new Integer(randGen.nextInt(4));
					if (!c6.contains(next)){
						c6.add(next);
						break;
					}
				}
				
				if (i%2 == 0)
					trials.add(new Block(complexity6List[next.intValue()], true, 6));
				else
					trials.add(new Block(complexity6List[next.intValue()], false, 6));
			}
		}
		
		Collections.shuffle(trials);
		
		currentCount = 0;
	}
	
	public Block nextBlock(){
		if (currentCount >= numBlocks)
			return null;
		
		return (Block)trials.get(currentCount ++);
	}
}
