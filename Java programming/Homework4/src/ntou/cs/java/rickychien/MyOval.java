package ntou.cs.java.rickychien;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/*
 * MyOval Class 定義隨機形狀大小的橢圓
 */
public class MyOval extends MyShape {
	MyOval() {
		super();
	}
	
	MyOval( int x, int y, Color c ) {
		super( x, y, c );
	}

	@Override
	public void draw( Graphics g ) {
		int x1 = getX();
		int y1 = getY();
		Color c = getColor();
		Random gen = new Random();
		int x2 = x1 + gen.nextInt( 200 ) + 1;
		int y2 = y1 + gen.nextInt( 200 ) + 1;
		
		g.setColor( c );
		
		// Set Oval filled
		if( gen.nextInt() % 2 == 1 ) {
			g.fillOval( x1, y1, x2, y2 );
		}
		// Set Oval unfilled
		else {
			g.drawOval( x1, y1, x2, y2 );
		}
	}
}
