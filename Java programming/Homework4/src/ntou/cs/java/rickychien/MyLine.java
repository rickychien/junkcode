package ntou.cs.java.rickychien;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/*
 * MyLine Class 定義隨機長度與方向的線
 */
public class MyLine extends MyShape {
	MyLine() {
		super();
	}
	
	MyLine( int x, int y, Color c ) {
		super( x, y, c );
	}
	
	@Override
	public void draw( Graphics g ) {
		int x1 = getX();
		int y1 = getY();
		Color c = getColor();
		Random gen = new Random();
		int x2 = x1 + gen.nextInt( 200 ) + 1; //變動x軸長度設定在200以內
		int y2 = y1 + gen.nextInt( 200 ) + 1; //變動y軸長度設定在200以內
		
		g.setColor( c );
		g.drawLine( x1, y1, x2, y2 );
	}
}
