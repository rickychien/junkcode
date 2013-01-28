package ntou.cs.java.rickychien;

import java.awt.Color;
import java.awt.Graphics;

/*
 * MyShape Class 畫出隨機形狀的圖形（線、橢圓、矩形）
 * 為一個抽象類別，透過抽象的 draw 方法畫出各種不同類的圖形
 */
public abstract class MyShape {
	private int x;
	private int y;
	private Color c;
	
	MyShape() {
		setX( 0 );
		setY( 0 );
		setColor( Color.BLACK );
	}
	
	MyShape( int x, int y, Color c ) {
		setX( x );
		setY( y );
		setColor( c );
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}
	
	public void setColor( Color c ) {
		this.c = c;
	}

	public Color getColor() {
		return c;
	}
	
	// Abstract Method 透過MyShape物件呼叫不同的Shape物件的 draw( g );
	public abstract void draw( Graphics g );
}
