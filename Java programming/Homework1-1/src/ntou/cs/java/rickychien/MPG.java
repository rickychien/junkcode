package ntou.cs.java.rickychien;

import java.util.Scanner;

/**
 * MPG Class is the main function of application.
 * @version 1.1 16 Mar 2011
 * @author RickyChien
 */
public class MPG {
	Scanner input = new Scanner(System.in);
	/* Save total miles  */
	private int totalMiles = 0;
	/* Save total gallons */
	private int totalGallons = 0;
	/* Save miles */
	int miles;
	/* Save gallons */
	int gallons;
	
	/* Use console mode deal with UI interact */
	public void showConsoleUI() {
		System.out.print("Enter miles (-1 to quit): ");
		while((miles = input.nextInt()) != -1) {
			System.out.print("Enter gallons: ");
			gallons = input.nextInt();
			System.out.printf("MPG this tankful: %.2f\n", calcThisMPG(miles, gallons));
			System.out.printf("MPG this tankful: %.2f\n", calcTotalMPG(miles, gallons));
			System.out.print("Enter miles (-1 to quit): ");
		}
	}
	
	/* Calculate current miles per gallon */
	public float calcThisMPG(int miles, int gallons) {
		return (float)miles / gallons;
	}

	/* Calculate total miles per gallon */
	public float calcTotalMPG(int miles, int gallons) {
		totalMiles += miles;
		totalGallons += gallons;
		return (float)totalMiles / totalGallons;
	}
}
