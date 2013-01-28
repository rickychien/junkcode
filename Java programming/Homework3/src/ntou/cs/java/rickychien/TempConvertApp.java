package ntou.cs.java.rickychien;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.*;

import javax.swing.*;

public class TempConvertApp extends JFrame {
	enum InputTemp { FAHRENHEIT, CELSIUS, MYHOT };
	enum OutputTemp { FAHRENHEIT, CELSIUS, MYHOT };
	
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JRadioButton fahrenheitRadioButton1;
	private JRadioButton celsiusRadioButton1;
	private JRadioButton myhotRadioButton1;
	private JRadioButton fahrenheitRadioButton2;
	private JRadioButton celsiusRadioButton2;
	private JRadioButton myhotRadioButton2;
	private ButtonGroup radioGroup1;
	private ButtonGroup radioGroup2;
	private JTextField tempField;
	private JTextField resultField;
	InputTemp inputTemp = InputTemp.FAHRENHEIT;
	OutputTemp outputTemp = OutputTemp.FAHRENHEIT;
	private int temperature;
	
	public TempConvertApp () {
		super( "Temperature Conversion App" );
		setLayout( new GridBagLayout() );
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Natural height, maximum width
		gbc.fill = GridBagConstraints.BOTH;
		
		label1 = new JLabel( "Convert from:" );
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		this.add( label1, gbc );
		
		// Create radio button
		fahrenheitRadioButton1 = new JRadioButton( "Fahrenheit", true );
		celsiusRadioButton1 = new JRadioButton( "Celsius", false );
		myhotRadioButton1 = new JRadioButton( "MyHot", false );
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		this.add( fahrenheitRadioButton1, gbc );
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		this.add( celsiusRadioButton1, gbc );
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		this.add( myhotRadioButton1, gbc );
		
		// Add to radioGroup1
		radioGroup1 = new ButtonGroup();
		radioGroup1.add( fahrenheitRadioButton1 );
		radioGroup1.add( celsiusRadioButton1 );
		radioGroup1.add( myhotRadioButton1 );
		
		// Register events for radioGroup1
		RadioButtonHandler radioButtonHandler1 = new RadioButtonHandler();
		fahrenheitRadioButton1.addItemListener( radioButtonHandler1 );
		celsiusRadioButton1.addItemListener( radioButtonHandler1 );
		myhotRadioButton1.addItemListener( radioButtonHandler1 );
		
		label2 = new JLabel( "Enter Numeric Temperature:" );
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		this.add( label2, gbc );
		
		tempField = new JTextField( "0", 25 );
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		this.add( tempField, gbc );
		
		label3 = new JLabel( "Convert to:" );
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		this.add( label3, gbc );
		
		// Create radio button
		fahrenheitRadioButton2 = new JRadioButton( "Fahrenheit", true );
		celsiusRadioButton2 = new JRadioButton( "Celsius", false );
		myhotRadioButton2 = new JRadioButton( "MyHot", false );
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		this.add( fahrenheitRadioButton2, gbc );
		gbc.gridx = 1;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		this.add( celsiusRadioButton2, gbc );
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		this.add( myhotRadioButton2, gbc );
		
		// Add to radioGroup2
		radioGroup2 = new ButtonGroup();
		radioGroup2.add( fahrenheitRadioButton2 );
		radioGroup2.add( celsiusRadioButton2 );
		radioGroup2.add( myhotRadioButton2 );
		
		// Register events for radioGroup2
		RadioButtonHandler radioButtonHandler2 = new RadioButtonHandler();
		fahrenheitRadioButton2.addItemListener( radioButtonHandler2 );
		celsiusRadioButton2.addItemListener( radioButtonHandler2 );
		myhotRadioButton2.addItemListener( radioButtonHandler2 );
		
		label4 = new JLabel( "Comparable Temperature is:" );
		gbc.gridx = 0;
		gbc.gridy = 6;
		gbc.gridwidth = 3;
		this.add( label4, gbc );
		
		resultField = new JTextField( "", 25 );
		resultField.setEditable(false);
		gbc.gridx = 0;
		gbc.gridy = 7;
		gbc.gridwidth = 3;
		this.add( resultField, gbc );
		
	}
	
	private class RadioButtonHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent event) {
			if( event.getSource() == fahrenheitRadioButton1 ) {
				inputTemp = InputTemp.FAHRENHEIT;
			} else if( event.getSource() == fahrenheitRadioButton2 ) {
				outputTemp = OutputTemp.FAHRENHEIT;
			} else if( event.getSource() == celsiusRadioButton1 ) {
				inputTemp = InputTemp.CELSIUS;
			} else if( event.getSource() == celsiusRadioButton2 ) {
				outputTemp = OutputTemp.CELSIUS;
			} else if( event.getSource() == myhotRadioButton1 ) {
				inputTemp = InputTemp.MYHOT;
			} else { // event.getSource() == myhotRadioButton2
				outputTemp = OutputTemp.MYHOT;
			}
			temperature = Integer.parseInt( tempField.getText() );
			resultField.setText( String.valueOf(caculateTemperature()) );
		}
	}
	
	private int caculateTemperature() {
		double result = 0.0;
		// Fahrenheit to Fahrenheit
		if( inputTemp == InputTemp.FAHRENHEIT && outputTemp == OutputTemp.FAHRENHEIT ) {
			result = temperature;
		}
		// Fahrenheit to Celsius
		else if( inputTemp == InputTemp.FAHRENHEIT && outputTemp == OutputTemp.CELSIUS ) {
			//result = (temperature - 32) * 5 / 9;
			result = 5f / 9f * (temperature - 32);
		}
		// Fahrenheit to MyHot
		else if( inputTemp == InputTemp.FAHRENHEIT && outputTemp == OutputTemp.MYHOT ) {
			result = 7 / 15f * temperature - 16 / 3f ;
		}
		// Celsius to Fahrenheit
		else if( inputTemp == InputTemp.CELSIUS && outputTemp == OutputTemp.FAHRENHEIT ) {
			result = temperature * 1.8f + 32;
		}
		// Celsius to Celsius
		else if( inputTemp == InputTemp.CELSIUS && outputTemp == OutputTemp.CELSIUS) {
			result = temperature;
		}
		// Celsius to MyHot
		else if( inputTemp == InputTemp.CELSIUS&& outputTemp == OutputTemp.MYHOT ) {
			result = (temperature * 2.8f + 32) * 0.3f;
		}		
		// MyHot to Fahrenheit
		else if( inputTemp == InputTemp.MYHOT && outputTemp == OutputTemp.FAHRENHEIT ) {
			result = temperature * 15 / 7 + 80 / 7;
		}
		// MyHot to Celsius
		else if( inputTemp == InputTemp.MYHOT && outputTemp == OutputTemp.CELSIUS ) {
			result = temperature * 25 / 21 - 80 / 7;
		}
		// MyHot to MyHot
		else if( inputTemp == InputTemp.MYHOT && outputTemp == OutputTemp.MYHOT ) {
			result = temperature;
		}
		
		return (int) result;
	}
}