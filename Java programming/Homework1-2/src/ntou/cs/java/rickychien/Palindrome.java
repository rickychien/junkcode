package ntou.cs.java.rickychien;

import java.util.Scanner;

/**
 * Palindrome Class is the main function of application.
 * @version 1.1 16 Mar 2011
 * @author RickyChien
 */
public class Palindrome {
	/* Use the console UI to interact with user */
	public void showConsoleUI() {
		Scanner input = new Scanner(System.in);
		/* Using string type integer to deal with this problem is easier */
		String integer;
		
		while(true) {
			System.out.print("Enter a positive integer: ");
			integer = input.next();
			try {
				// If integer string parseInt encounter NumberFormatException means it's out-of-bound.
				Integer.parseInt(integer);
				
				// Determine odd number.
				if(integer.length() % 2 == 0) {
					System.out.println("The length of " + integer + " is not an odd number, please input again!");
					
				// Determine whether the integer is a palindrome.
				} else {
					Palindrome palindrome = new Palindrome();
					if(palindrome.isPalindrome(integer)) {
						System.out.println(integer + " is a palindrome!!!");
					} else {
						System.out.println(integer + " is \"not\" a palindrome!!!");
					}
				}
			} catch (NumberFormatException e) {
				System.out.println(integer + " is out-of-bound, please input again!");
			}
		}
	}
	
	/* isPalindrome check whether the integer(String type) is a palindrome. */
	public boolean isPalindrome(String integerStr) {
		int len = integerStr.length();
		
		for(int i = 0; i < len / 2; i++) {
			if(integerStr.charAt(i) != integerStr.charAt(len - i - 1)) {
				return false;
			}
		}
		return true;
	}
}
