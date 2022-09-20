package com.boq.application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class GuiBOQV2 extends WindowAdapter implements ActionListener {

	static JTextField quanitytextField, minRateTextField;
	static JTextField requiredValueTextField, actualSumTextField, itemsLengthTeXtField;
	static JTextArea resultArea;
	static JButton calculateBtn, resetBtn;
	static JCheckBox checkBox;
	static JFrame f;

	static List<JTextField> quanitytextFields = new ArrayList<JTextField>();
	static List<JTextField> minRatetextFields = new ArrayList<JTextField>();
	static List<JCheckBox> minRateCheckboxes = new ArrayList<JCheckBox>();
	static JScrollPane scroll;
	static JTable resultTable;
	static DefaultTableModel dtm;
	static String column[];

	GuiBOQV2() {
		String values = "";
		f = new JFrame("BOQ Calculate");

		values = JOptionPane.showInputDialog(f, "Number of Items(upto 15):-");
		if (values ==null)
			System.exit(0);
		else{
			BOQCalculateV2.MAX_ITEMS = Integer.parseInt(values);

			JLabel items = new JLabel("Number of Items:");
			items.setBounds(20, 10, 120, 20);
			f.add(items);
			itemsLengthTeXtField = new JTextField();
			itemsLengthTeXtField.setBounds(130, 10, 75, 20);
			itemsLengthTeXtField.setText(values);
			itemsLengthTeXtField.setEditable(false);
			f.add(itemsLengthTeXtField);

			drawUI();

			f.setSize(500, 500);
			f.setLayout(null);
			f.setVisible(true);
			f.setLocationRelativeTo(null);

			f.addWindowListener(this);
			f.setResizable(false);
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
	}

	public void drawUI() {
		JLabel quantity = new JLabel("Quantity");
		quantity.setBounds(20, 50, 50, 20);
		f.add(quantity);

		int x = 20, y = 75, width = 50, height = 20;

		for (int i = 0; i < BOQCalculateV2.MAX_ITEMS; i++) {
			quanitytextField = new JTextField();
			quanitytextField.setBounds(x, y, width, height);
			f.add(quanitytextField);
			quanitytextFields.add(quanitytextField);
			y += 25;
		}

		quanitytextFields.get(0).setText("58.24");
		quanitytextFields.get(1).setText("1.568");
		quanitytextFields.get(2).setText("33.104");
		quanitytextFields.get(3).setText("7.74");
		quanitytextFields.get(4).setText("7.50");
		quanitytextFields.get(5).setText("11");

		JLabel minimumRate = new JLabel("Min. Rate");
		minimumRate.setBounds(90, 50, 60, 20);
		f.add(minimumRate);
		x = 90;
		y = 75;
		width = 60;
		height = 20;
		for (int i = 0; i < BOQCalculateV2.MAX_ITEMS; i++) {
			minRateTextField = new JTextField();
			minRateTextField.setBounds(x, y, width, height);

			checkBox = new JCheckBox();
			checkBox.setBounds(150, y, 20, 20);

			minRatetextFields.add(minRateTextField);
			minRateCheckboxes.add(checkBox);

			f.add(minRateTextField);
			f.add(checkBox);
			y += 25;
		}

		minRatetextFields.get(0).setText("70");
		minRatetextFields.get(1).setText("2700");
		minRatetextFields.get(2).setText("2900");
		minRatetextFields.get(3).setText("300");
		minRatetextFields.get(4).setText("2450");
		minRatetextFields.get(5).setText("50");

		JPanel resultPanel = new JPanel();
		resultPanel.setBorder(new TitledBorder("Result Area:-"));
		resultPanel.setBounds(190, 55, 280, 170);
		resultPanel.setLayout(new BorderLayout());
//		
//		resultArea = new JTextArea();
//		resultArea.setLineWrap(true);
//
//		scroll = new JScrollPane (resultArea);
//		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		
//		resultPanel.add(scroll);
//		f.add(resultPanel);

		String data[][] = {};

		List<String> col = new ArrayList<>();
		for (int i = 0; i < BOQCalculateV2.MAX_ITEMS; i++)
			col.add("Rate-" + (i + 1));

		column = col.toArray(new String[0]);
		dtm = new DefaultTableModel(data, column);
		resultTable = new JTable(dtm);

		resultTable.setBounds(190, 55, 280, 170);
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//	    TableColumnModel columnModel = resultTable.getColumnModel();
//	    for(int i=0;i<BOQCalculateV2.MAX_ITEMS;i++) 
//	    {
//	    	columnModel.getColumn(i).setPreferredWidth(50);
//	    }

		scroll = new JScrollPane(resultTable);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		resultPanel.add(scroll);
		f.add(resultPanel);

		JLabel requiredValue = new JLabel("Required Sum");
		requiredValue.setBounds(200, 230, 90, 20);
		requiredValueTextField = new JTextField();
		requiredValueTextField.setBounds(200, 250, 65, 20);
		f.add(requiredValue);

		JLabel actualSum = new JLabel("Actual Sum");
		actualSum.setBounds(300, 230, 80, 20);
		actualSumTextField = new JTextField();
		actualSumTextField.setBounds(300, 250, 65, 20);
		f.add(actualSum);

		actualSumTextField.setText(Double.toString(180268));
		requiredValueTextField.setText(
				Double.toString(BOQCalculateV2.round2point(Double.parseDouble(actualSumTextField.getText()) * 0.7)));

		actualSumTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateRequiredValue();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateRequiredValue();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateRequiredValue();
			}
		});

		calculateBtn = new JButton("Calculate");
		calculateBtn.setBounds(200, 285, 100, 30);
		calculateBtn.addActionListener(new BOQCalculateV2());
		f.add(calculateBtn);
		resetBtn = new JButton("Reset");
		resetBtn.setBounds(320, 285, 100, 30);
		resetBtn.addActionListener(new BOQCalculateV2());
		f.add(resetBtn);

		f.add(requiredValueTextField);
		f.add(actualSumTextField);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}

	void updateRequiredValue() {
		requiredValueTextField.setText(
				Double.toString(BOQCalculateV2.round2point(Double.parseDouble(actualSumTextField.getText()) * 0.7)));
	}

	public void windowClosing(WindowEvent e) 
	{
			int option = JOptionPane.showConfirmDialog(f, "Do you really want to quit 	?", "Please confirm..",
					JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
			if (option == JOptionPane.NO_OPTION) {
				f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			}
	}
}
