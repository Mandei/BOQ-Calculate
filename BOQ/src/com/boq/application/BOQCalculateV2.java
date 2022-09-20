package com.boq.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.TableColumnModel;

public class BOQCalculateV2 implements ActionListener {

	static int MAX_ITEMS;

	static double quantity[];
	static double rate_min[];
	static int fixedRate[]; // array to store the value to be fixed/change- (0-> fixed, 1->change)

	static double fixedSum;
	static double varSum;
	static double actualValue;
	static double desiredValue;
	static String resultSet[][];

	static List<Integer> valueArray = new ArrayList<>(); // List storing index to be calculated for changes
	static List<double[]> resultArray = new ArrayList<>();
	
	public static void main(String[] args) 
	{
		new GuiBOQV2();
	}
	
	void init() {
		fixedSum=0;
		varSum=0;
		
		quantity= new double[MAX_ITEMS];
		rate_min= new double[MAX_ITEMS];
		fixedRate= new int[MAX_ITEMS];
		
		if(!valueArray.isEmpty())
			valueArray.clear();
	}

	public static void mainFunction() {
		
		double diffFromDesiredValue;
		double nextPlusInRateMinValues;
		double chosenQuantitySum = 0;
		
		// Storing value(1) in array for the chosen rows
		for (int val : valueArray)
			fixedRate[val] = 1; 

		// It is the actual sum of multiply of all the quantity with rate
		System.out.println("Actual Value:-" + actualValue);
		
		// It is the value that is required after adjustment- max= 70% of actualValue
		System.out.println("Desired Value-:" + round2point(desiredValue));

		// Taking the sum for quantity for the chosen rows
		for (int pos : valueArray)
			chosenQuantitySum += quantity[pos];
		
		System.out.println("Quantity Sum for chosen rows only-: " + round2point(chosenQuantitySum));

		/*
		 * Calculating the fixedSum and varSum - -> fixedSum= values which are not going
		 * to change for the calculation, -> varSum= values which are going to change
		 * for the calculation,
		 */
		for (int i = 0; i < quantity.length; i++) {
			if (fixedRate[i] == 0) {
				fixedSum += (quantity[i] * rate_min[i]);
			} else {
				varSum += (quantity[i] * rate_min[i]);
			}
		}

		System.out.println("Fixed Sum: " + round2point(fixedSum));
		System.out.println("Var Sum: " + round2point(varSum));
		System.out.println(" \nVarSum + fixedSum (Before Adjustments)-:" + round2point(varSum + fixedSum));

		diffFromDesiredValue = desiredValue - (varSum + fixedSum);
		System.out.println("Difference from Desired Value(First time)-:" + round2point(diffFromDesiredValue));

		// minimum value to be added in each chosen rate value to cover the difference-
		// One time only
		nextPlusInRateMinValues = Math.floor(diffFromDesiredValue / chosenQuantitySum);

		// Update 50 Values to cover the gap
		for (int i = 0; i < quantity.length; i++)
			if (fixedRate[i] == 1) {
				rate_min[i] += nextPlusInRateMinValues;
				// System.out.println(rate_min[i]);
			}
		System.out.println("Next Value to add after first iteration-: " + nextPlusInRateMinValues);
		varSum = 0;

		// Again calculating the updated varSum
		for (int i = 0; i < quantity.length; i++)
			if (fixedRate[i] == 1)
				varSum += (quantity[i] * rate_min[i]);

		// calculating difference for the last time After Gap Adjustment
		diffFromDesiredValue = desiredValue - (varSum + fixedSum); // need to get check for negative
		System.out
				.println("\nDifference from Desired Value(After Gap Adjustment)-:" + round2point(diffFromDesiredValue));

		System.out.println("VarSum+fixedSum(After Gap Adjustment)-:" + round2point(varSum + fixedSum));

		// iterate through selected column in equation.
		for (int i = 0; i < valueArray.size() - 1; i++)
			for (int j = i + 1; j < valueArray.size(); j++)
				equationSolver(valueArray.get(i), valueArray.get(j), diffFromDesiredValue);

	}

	static void displayValue(double result[]) {
		for (int i = 0; i < quantity.length; i++)
			System.out.println("Result[" + i + "] = " + (result[i]));

		resultArray.add(result);
		
		resultSet=new String[resultArray.size()][];
		for(int i=0;i<resultArray.size();i++) {
			resultSet[i]=covertToStringArray(resultArray.get(i));
		}
		GuiBOQV2.dtm.setDataVector(resultSet, GuiBOQV2.column);
		TableColumnModel columnModel = GuiBOQV2.resultTable.getColumnModel();
	    for(int i=0;i<BOQCalculateV2.MAX_ITEMS;i++) 
	    {
	    	columnModel.getColumn(i).setPreferredWidth(60);
	    }
//		for (int i = 0; i < MAX_ITEMS; i++) {
//			if (i != 0)
//				GuiBOQV2.resultArea.setText(GuiBOQV2.resultArea.getText() + " , " + Double.toString(result[i]));
//			else
//				GuiBOQV2.resultArea.setText(GuiBOQV2.resultArea.getText() + "\n" + Double.toString(result[i]));
//		}
		
//		GuiBOQV2.resultArea.setText(GuiBOQV2.resultArea.getText() + "\n");

	}
	static String[] covertToStringArray(double[] val) 
	{
		String[] result=new String[val.length];
		for(int i=0;i<val.length;i++)
			result[i]=Double.toString(val[i]);
		return result;
	}

	static double round2point(double value) {
		return Math.round((value) * 100.0) / 100.0;
	}

	static double round1point(double value) {
		return Math.round((value) * 10.0) / 10.0;
	}

	static void equationSolver(int i, int j, double k) {
		boolean flag = false;
		double x = 0, y = 0;

		// Equation to solve is ax+by=c, need to find every possible value of x and y
		// for given value of c
		System.out.println("\n\nValue of < a,b > :(" + " " +  " , " + quantity[j] + " )");

		if( quantity[i]>0 &&  quantity[j]>0) 
		{
			double x_max = (k - (quantity[j] * 0.01)) / quantity[i];
			double y_max = (k - (quantity[i] * 0.01)) / quantity[j];
	
			System.out.println("Value of <x_max , y_max> :( " + Math.ceil(x_max) + " , " + Math.ceil(y_max) + " )");
	
			for (x = 0.01; x < Math.ceil(x_max); x += 0.01) {
				for (y = 0.01; y < Math.ceil(y_max); y += 0.01) {
	
					x = round2point(x);
					y = round2point(y);
	
					if (round2point(quantity[i] * x + quantity[j] * y) == round2point(k)) {
						System.out.println(
								"x:" + x + ", y:" + y + " ax+by=" + "" + round2point(quantity[i] * x + quantity[j] * y));
	
						double temp_i_min = round2point(rate_min[i] + x);
						double temp_j_min = round2point(rate_min[j] + y);
	
						System.out.println("x-:" + temp_i_min + ", y-:" + temp_j_min);
						//Clear varSum
						varSum = 0.0;
	
						// updating the rate_min value for selected index i and j and calulating updated
						// varSum
						for (int pos = 0; pos < quantity.length; pos++) {
							if (pos == i)
								varSum += (temp_i_min) * quantity[pos];
							else if (pos == j)
								varSum += (temp_j_min) * quantity[pos];
							else if (fixedRate[pos] == 1)
								varSum += rate_min[pos] * quantity[pos];
						}
	
	//					varSum = round2point(varSum);
	//					varSum = round2point(((rate_min[i] + x) * quantity[i]) + (rate_min[2] * quantity[2]) + ((rate_min[j] + y) * quantity[j]));
	
						double resultValue = round2point(varSum) + fixedSum;
	
						System.out.println("Desired Value-:" + round2point(desiredValue) + ", Result value-:"
								+ round2point(resultValue) + "\n");
	
						if (round2point(desiredValue) == round2point(resultValue)) {
							double finalrate[] = rate_min.clone();
	
							System.out.println("Value Found - for x and y where a: " + quantity[i] + ", b: " + quantity[j]);
	
							finalrate[i] = temp_i_min; // updating i <th> index value in finalRate
							finalrate[j] = temp_j_min; // updating j <th> index value in finalRate
	
							displayValue(finalrate);
							flag = true;
						}
					}
	
					else if (round2point(quantity[i] * x + quantity[j] * y) > k) {
	//					System.out.println("Break-"+ x + "," + y + ". ax+by=" + "" + (quantity[i] * x + quantity[j] * y));
						break;
					}
	//				else if(round2point(quantity[i] * x + quantity[j] * y) > k-3) {
	//					System.out.println( x + "," + y + ". ax+by=" + "" + (quantity[i] * x + quantity[j] * y));
	//				}
				}
				if (flag)
					break;
			}
		}
			if (!flag)
			{
				JOptionPane.showMessageDialog(GuiBOQV2.f,"No value found for the selected checkbox","Result not found",JOptionPane.WARNING_MESSAGE);     
				System.out.println("NO value found for x and y where a: " + quantity[i] + ", b: " + quantity[j]);
				return;
			}
		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == GuiBOQV2.calculateBtn) 
		{
			init();
			int countCheckBox=0;
			for (int i = 0; i < MAX_ITEMS; i++) 
			{
				if (GuiBOQV2.minRateCheckboxes.get(i).isSelected())
				{
					valueArray.add(i);
					countCheckBox++;
				}
			}
			if(countCheckBox<2) {
				 JOptionPane.showMessageDialog(GuiBOQV2.f,"Please select atleast 2 checkBoxes !!","Warning",JOptionPane.WARNING_MESSAGE);     
				 return;
			}
			else 
			{
				for (int i = 0; i < MAX_ITEMS; i++) 
				{
					if(!GuiBOQV2.quanitytextFields.get(i).getText().isEmpty())
						quantity[i] = Double.parseDouble(GuiBOQV2.quanitytextFields.get(i).getText());
					if(!GuiBOQV2.minRatetextFields.get(i).getText().isEmpty())
						rate_min[i] = Double.parseDouble(GuiBOQV2.minRatetextFields.get(i).getText());
				}
		
				actualValue = Double.parseDouble(GuiBOQV2.actualSumTextField.getText());
				desiredValue = round2point(actualValue * 0.7);
				mainFunction();
			}
		}
		
		if (e.getSource() == GuiBOQV2.resetBtn) 
		{
			resultArray.clear();
			resultSet=new String[resultArray.size()][];
			GuiBOQV2.dtm.setDataVector(resultSet, GuiBOQV2.column);
			for (int i = 0; i < MAX_ITEMS; i++) 
			{
				if (GuiBOQV2.minRateCheckboxes.get(i).isSelected())
				{
					GuiBOQV2.minRateCheckboxes.get(i).setSelected(false);
					
				}
			}
		}

	}
}
