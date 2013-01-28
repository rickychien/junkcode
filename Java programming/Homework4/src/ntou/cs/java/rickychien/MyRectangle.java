package ntou.cs.java.rickychien;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/*
 * MyRectangle Class 定義隨機形狀大小的矩形
 */
public class MyRectangle extends MyShape {
	MyRectangle() {
		super();
	}
	
	MyRectangle( int x, int y, Color c ) {
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
		
		// Set Rectangle filled
		if( gen.nextInt() % 2 == 1 ) {
			g.fillRect( x1, y1, x2, y2 );
		}
		// Set Rectangle unfilled
		else {
			g.drawRect( x1, y1, x2, y2 );
		}
	}
}
