package ntou.cs.java.rickychien;

import java.util.Random;
import java.util.Scanner;

/**
 * Test Class is the main class to test GuessNumber function correctly work.
 * This class create a game interface to help user test GuessNumber.
 * 
 * @author Ricky
 * @version 1.0
 */
public class Test {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Random generator = new Random();
		GuessNumber game = new GuessNumber();
		String choice = "y";
		
		while (true) {
			game.setSecretNum(generator.nextInt(1000) + 1);
			
			//Create a interactive interface and check if user match the number.
			while (true) {
				System.out.print("Guess a number between 1 and 1000: ");
				game.setGuessNum(input.nextInt());

				if (game.checkMatch() > 0) {
					System.out.println("Too high. Try again.");
				} else if (game.checkMatch() < 0) {
					System.out.println("Too low. Try again.");
				} else {
					System.out.println("Congratulations. You guessed the number!");
					break;
				}
			}

			//Check number of guess and output message.
			if (game.getGuessCounter() < 10) {
				System.out.println("Either you know the secret or you got luckly!");
			} else if (game.getGuessCounter() == 10) {
				System.out.println("Aha! You know the secret!");
			} else {
				System.out.println("You should be able to do better!");
			}
			
			//Check whether user entered answer is correct.
			while (true) {
				System.out.print("Do you want to try again? (Enter y/n):");
				choice = input.next();
				if (!(choice.equals("y") || choice.equals("n"))) {
					System.out.println("Your entered keyword is incorrect, please enter a correct keyword.");
				} else {
					break;
				}
			}
			
			//If user enter "n", leave the game.
			if(choice.equals("n")) {
				break;
			}
		}
	}
}
