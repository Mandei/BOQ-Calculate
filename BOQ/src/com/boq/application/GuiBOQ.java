package com.boq.application;

import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;

public class GuiBOQ implements ActionListener {
	JTextField text1, text2, text3, text4, text5, text6, text7, text8, text9, text10;
	JTextField text11, text12, text13, text14, text15, text16, text17, text18, text19, text20, text21, text22;
	JButton btn;

	GuiBOQ() {
		JFrame f = new JFrame("BOQ Calculate");
		JLabel quantity=new JLabel("Quantity");
		quantity.setBounds(20, 5, 50, 20);
		text1 = new JTextField();
		text1.setBounds(20, 25, 50, 20);
		text2 = new JTextField();
		text2.setBounds(20, 50, 50, 20);
		text3 = new JTextField();
		text3.setBounds(20, 75, 50, 20);
		text4 = new JTextField();
		text4.setBounds(20, 100, 50, 20);
		text5 = new JTextField();
		text5.setBounds(20, 125, 50, 20);

		JLabel minimumRate =new JLabel("Min. Rate");
		minimumRate.setBounds(90, 5, 60, 20);
		text6 = new JTextField();
		text6.setBounds(90, 25, 60, 20);
		text7 = new JTextField();
		text7.setBounds(90, 50, 60, 20);
		text8 = new JTextField();
		text8.setBounds(90, 75, 60, 20);
		text9 = new JTextField();
		text9.setBounds(90, 100, 60, 20);
		text10 = new JTextField();
		text10.setBounds(90, 125, 60, 20);

		text11 = new JTextField();
		text11.setBounds(175, 25, 130, 100);
	
		JLabel requiredValue=new JLabel("Req. Sum");
		requiredValue.setBounds(160, 135, 80, 20);
		text21 = new JTextField();
		text21.setBounds(160, 160, 65, 20);


		JLabel actualSum=new JLabel("Actual Sum");
		actualSum.setBounds(240, 135, 80, 20);
		text22 = new JTextField();
		text22.setBounds(240, 160, 60, 20);

		btn = new JButton("Calculate");
		btn.setBounds(200, 200, 100, 30);
		btn.addActionListener(this);
		f.add(text1);
		f.add(text2);
		f.add(text3);
		f.add(text4);
		f.add(text5);
		f.add(text6);
		f.add(text7);
		f.add(text8);
		f.add(text9);
		f.add(text10);

		f.add(text11);
		
		f.add(text21);
		f.add(text22);
		f.add(quantity);
		f.add(minimumRate);
		f.add(requiredValue);
		f.add(actualSum);

		f.add(btn);
		f.setSize(350, 300);
		f.setLayout(null);
		f.setVisible(true);
		text1.setText(Double.toString(BOQCalculateV1.a));
		text2.setText(Double.toString(BOQCalculateV1.b));
		text3.setText(Double.toString(BOQCalculateV1.c));
		text4.setText(Double.toString(BOQCalculateV1.d));
		text5.setText(Double.toString(BOQCalculateV1.e));
		

		text6.setText(Double.toString(BOQCalculateV1.j_initial));
		text7.setText(Double.toString(BOQCalculateV1.k_initial));
		text8.setText(Double.toString(BOQCalculateV1.l_initial));
		text9.setText(Double.toString(BOQCalculateV1.m_initial));
		text10.setText(Double.toString(BOQCalculateV1.n_initial));

		text11.setText("");
		text21.setText(Double.toString(BOQCalculateV1.actualValue));
		String result1 = Double.toString(BOQCalculateV1.j_min);
		


		text22.setText(Double.toString(BOQCalculateV1.desiredValue));
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e) {
//		String quantity1 = text1.getText();
	
		if (e.getSource() == btn) {

		}

	}
	
	public static void main(String[] args) {
		new GuiBOQ();
	}

}
