package ntou.cs.java.rickychien;

import java.util.Scanner;

/**
 * ProductRetail Class process application logical part.
 * @version 1.1 16 Mar 2011
 * @author RickyChien
 */
public class ProductRetail {
	/* Save retailValue of each product */
	private float[] retailValue = new float[5];
	
	/* Use the console mode UI to interact with user */
	public void showConsoleUI() {
		Scanner input = new Scanner(System.in);
		ProductRetail prm = new ProductRetail();
		/* Save the user inputed product number */
		int productNO;
		/* Save the user inputed quantity sold */
		int quantitySold;
		
		System.out.print("Enter product number (1-5) (0 to stop): ");
		while((productNO = input.nextInt()) != 0) {
			System.out.print("Enter quantity sold: ");
			quantitySold = input.nextInt();
			prm.calcData(productNO, quantitySold);
			System.out.print("Enter product number (1-5) (0 to stop): ");
		}
		prm.displayData();
	}
	
	/* Calculate retailValue and save in retailVaule array */
	public void calcData(int productNO, int quantitySold) {
		switch(productNO) {
			case 1:
				retailValue[productNO - 1] = 2.98f * quantitySold;
				break;
			case 2:
				retailValue[productNO - 1] = 4.5f * quantitySold;
				break;
			case 3:
				retailValue[productNO - 1] = 9.98f * quantitySold;
				break;
			case 4:
				retailValue[productNO - 1] = 4.49f * quantitySold;
				break;
			case 5:
				retailValue[productNO - 1] = 6.87f * quantitySold;
				break;
			default:
				System.out.println("No such product number, please input again!");
		}
	}

	/* Display the retailValue data in screen */
	public void displayData() {
		for(int i = 0; i < retailValue.length; i++) {
			System.out.printf("Product %d: %.2f\n" ,(i + 1), retailValue[i]);
		}
	}
}
