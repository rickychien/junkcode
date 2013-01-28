package ntou.cs.java.rickychien;

/**
 * GuessNumber Class is a game that user guess a random number.
 * 
 * @author Ricky 
 * @version 1.0
 */
public class GuessNumber {
	
	private int secretNum;
	private int guessNum;
	private int guessCounter = 0;
	
	/* Set the secret number */
	public void setSecretNum(int num) {
		secretNum = num;
	}
	
	/* Set the guess number */
	public void setGuessNum(int num) {
		++guessCounter;
		guessNum = num;
	}
	
	/* Get the number of guess */
	public int getGuessCounter() {
		return guessCounter;
	}
	
	/* Check whether the number is match
	 * 
	 * guessNum less than secretNum return < 0
	 * guessNum higher than secretNum return > 0
	 * guessNum equal to secretNum return 0 
	 * */
	public int checkMatch() {
		return guessNum - secretNum;
	}
	
}

