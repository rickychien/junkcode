package ntou.cs.java.rickychien;

import java.util.Scanner;

public class Test {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		while (true) {
			System.out.println("--- Welcome to Poker Game ---");
			System.out.println("This is a one on one poker game");
			System.out.println("Human vs Computer");
			System.out.println("Are you ready to fight?");
			System.out.println("1) New Game");
			System.out.println("2) Exit");
			System.out.println("Enter a number and press enter to fight!");
			System.out.print("Make your choice: ");

			switch (input.nextInt()) {
			case 1:
				// New Game
				Card[] user;
				Card[] com;
				Poker pokerGame = new Poker();
				
				pokerGame.suffleCard();
				user = pokerGame.dealCard();
				com = pokerGame.dealCard();
				
				System.out.println("--- Your Cards ---");
				for(Card card : user) {
					System.out.println(card.toStirng());
				}
				System.out.println("It's " + pokerGame.getCardType(user));
				
				System.out.println("--- Computer's Cards ---");
				for(Card card : com) {
					System.out.println(card.toStirng());
				}
				System.out.println("It's " + pokerGame.getCardType(com));
				
				if (pokerGame.compareDecks(user, com)) {
					System.out.println("User Win!!");
				} else {
					System.out.println("Computer Win!!");
				}
				break;
			case 2:
				// Exit
				return;
			default:
				// Error input
				System.out.println("Please enter a corrent option, try again!");
			}
		}
	}
}
