package ntou.cs.java.rickychien;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JPanel;

/*
 * ShapeGenerator Class 主程式
 * 依照使用者輸入要顯示的圖形個數，隨機的繪製在畫面上
 */
public class ShapeGenerator extends JPanel {
	private int numOfLine;
	private int numOfOval;
	private int numOfRect;
	//定義MyShape陣列物件來存放多的圖形
	MyShape[] myShapes;
	
	//程式進入點不會呼叫到無參數的建構者（不會用到）
	ShapeGenerator() {
	}
	
	//程式需接收使用者輸入的個數，並把參數傳進此建構者
	ShapeGenerator( int numOfShape ) {
		//用來記憶線、橢圓、矩形的出現次數
		numOfLine = 0;
		numOfOval = 0;
		numOfRect = 0;
		
		myShapes = new MyShape[numOfShape];
		Random gen = new Random();
		
		for( int i = 0; i < numOfShape; i++ ) {
			//建構圖形時隨機給予X軸座標位置
			int x = gen.nextInt( 300 );
			//建構圖形時隨機給予y軸座標位置
			int y = gen.nextInt( 300 );
			//建構圖形時隨機給予Color
			int rgb = gen.nextInt();
			
			//有三種圖形（線、橢圓、矩形），隨機選一種
			switch ( gen.nextInt( 3 ) ) {
			case 0:
				++numOfLine;
				myShapes[i] = new MyLine( x, y, new Color( rgb ) );
				break;
			case 1:
				++numOfOval;
				myShapes[i] = new MyOval( x, y, new Color( rgb ) );
				break;
			case 2:
				++numOfRect;
				myShapes[i] = new MyRectangle( x, y, new Color( rgb ) );
				break;
			}
		}
	}
	
	public int getLines() {
		return numOfLine;
	}
	
	public int getOvals() {
		return numOfOval;
	}
	
	public int getRectangles() {
		return numOfRect;
	}
	
	// Override 繪圖方法並呼叫自定義的圖形物件繪製圖片
	public void paintComponent( Graphics g ) {
		super.paintComponents( g );
		
		for( MyShape shape : myShapes ) {
			shape.draw( g );
		}
	}
}
