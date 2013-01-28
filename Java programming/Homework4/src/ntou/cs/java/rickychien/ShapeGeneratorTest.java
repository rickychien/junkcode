package ntou.cs.java.rickychien;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * ShapeGeneratorTest Class 主程式測試檔
 * 繪製了顯示ShapeGenerator的基本介面
 */
public class ShapeGeneratorTest {
	public static void main(String[] args) {
		//使用者輸入的Shape個數
		int numOfShape = 0; 
		String inputNum = JOptionPane.showInputDialog( "Number of shapes" );
		
		//檢查使用者是否有輸入
		if( inputNum != null && !( inputNum.equals( "" ) ) ) {
			numOfShape = Integer.parseInt( inputNum );
		} else {
			return;
		}
		
		//繪製簡單的程式介面
		JFrame frame = new JFrame( "Shape Generator" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setLayout( new BorderLayout() );
		frame.setBackground( Color.GRAY );
		
		ShapeGenerator shapeGenerator = new ShapeGenerator( numOfShape );
		CounterPanel counterPanel = new CounterPanel( 
				shapeGenerator.getLines(), 
				shapeGenerator.getOvals(), 
				shapeGenerator.getRectangles()
				);
		
		frame.add( shapeGenerator, BorderLayout.CENTER );
		frame.add( counterPanel, BorderLayout.SOUTH );
		frame.setSize( 700, 600 );
		frame.setVisible( true );
	}
}
