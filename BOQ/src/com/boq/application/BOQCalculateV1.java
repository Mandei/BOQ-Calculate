package com.boq.application;

import java.util.*;

public class BOQCalculateV1 {

	static double a, b, c, d, e, f;
	static double quantity[] = new double[6];

	static double j_min, k_min, l_min, m_min, n_min, o_min;

	static double j_initial, k_initial, l_initial, m_initial, n_initial, o_initial;
	static double rate[] = new double[6];
	static double rate_min[] = new double[6];

	static int fixedRate[] = new int[6]; // array to store the value to be fixed/change- (0-> fixed, 1->change)

	static double fixedSum;
	static double varSum;
	static double actualValue;
	static double desiredValue;

	public static void main(String[] args) {
		a = 58.24;
		b = 1.568;
		c = 33.104;
		d = 7.74;
		e = 7.50;
		f = 11;

		quantity[0] = a;
		quantity[1] = b;
		quantity[2] = c;
		quantity[3] = d;
		quantity[4] = e;
		quantity[5] = f;

		j_initial = 70; // 138.15
		k_initial = 2700; // 3173.80
		l_initial = 2900; // 4009.20
		m_initial = 3000; // 727.20
		n_initial = 2450; // 3498.7
		o_initial = 50; // 241.30

		rate[0] = j_initial;
		rate[1] = k_initial;
		rate[2] = l_initial;
		rate[3] = m_initial;
		rate[4] = n_initial;
		rate[5] = o_initial;

		j_min = 70;
		k_min = 2700;
		l_min = 2900;
		m_min = 300;
		n_min = 2450;
		o_min = 50;

		rate_min[0] = j_min;
		rate_min[1] = k_min;
		rate_min[2] = l_min;
		rate_min[3] = m_min;
		rate_min[4] = n_min;
		rate_min[5] = o_min;

		try (Scanner sc = new Scanner(System.in)) {

			for (int i = 0; i < quantity.length; i++) {
				// actualValue+=(quantity[i]*rate[i]); because we did not get the final rate
				// everytime to calculate the Actual SUM
				fixedRate[i] = 0;
			}

			System.out.println("Enter how many values you want to choose for change to get your result:-");
			int values = sc.nextInt();

			// array storing index to be calculated for changes
			int valueArray[] = new int[values];

			System.out.println("Enter row numbers to be used:(0-<n-1>)");
			for (int i = 0; i < values; i++)
				valueArray[i] = sc.nextInt();

			// Storing value(1) in array for the chosen rows
			for (int val : valueArray)
				fixedRate[val] = 1; // need to handle zero input by user

			// It is the actual sum of multiply of all the quantity with rate
			actualValue = 180268;
			System.out.println("Actual Value:-" + actualValue);

			// It is the value that is required after adjustment- max= 70% of actualValue
			desiredValue = round2point(actualValue * 0.7);
			System.out.println("Desired Value-:" + round2point(desiredValue));

			double chosenQuantitySum = 0;

			// Taking the sum for quantity for the chosen rows
			for (int pos : valueArray)
				chosenQuantitySum += quantity[pos];

			/*
			 * Calculating the fixedSum and varSum - -> fixedSum= values which are not going
			 * to change for the calculation, -> varSum= values which are going to change
			 * for the calculation,
			 */
			for (int i = 0; i < quantity.length; i++) {
				if (fixedRate[i] == 0) {
					fixedSum += (quantity[i] * rate[i]);
				} else {
					varSum += (quantity[i] * rate_min[i]);
				}
			}

			System.out.println("Fixed Sum: " + round2point(fixedSum));
			System.out.println("Var Sum: " + round2point(varSum));
			System.out.println(" \nVarSum + fixedSum (Before Adjustments)-:" + round2point(varSum + fixedSum));

			double diffFromDesiredValue;
			double nextPlusInRateMinValues;

			diffFromDesiredValue = desiredValue - (varSum + fixedSum);
			System.out.println("Difference from Desired Value(First time)-:" + round2point(diffFromDesiredValue));

			// minimum value to be added in each chosen rate value to cover the difference-
			// One time only
			nextPlusInRateMinValues = Math.floor(diffFromDesiredValue / chosenQuantitySum);

			// Update rate_min Values to cover the gap
			for (int i = 0; i < quantity.length; i++)
				if (fixedRate[i] == 1) {
					rate_min[i] += nextPlusInRateMinValues;
					// System.out.println(rate_min[i]);
				}

			varSum = 0;

			// Again calculating the updated varSum
			for (int i = 0; i < quantity.length; i++)
				if (fixedRate[i] == 1)
					varSum += (quantity[i] * rate_min[i]);

			// calculating difference for the last time After Gap Adjustment
			diffFromDesiredValue = desiredValue - (varSum + fixedSum);
			System.out.println("\nDifference from Desired Value(After Gap Adjustment)-:" + round2point(diffFromDesiredValue));

			System.out.println("VarSum+fixedSum(After Gap Adjustment)-:" + round2point(varSum + fixedSum)
					+ " \nQuantity Sum for chosen rows only-:" + round2point(chosenQuantitySum)
					+ " \nNext Value to add after first iteration-: " + nextPlusInRateMinValues);

			// iterate through selected column in equation.
			for (int i = 0; i < valueArray.length - 1; i++)
				for (int j = i + 1; j < valueArray.length; j++)
					equationSolver(valueArray[i], valueArray[j], diffFromDesiredValue);
		}

//		new GuiBOQ();

	}

	static void displayValue(double result[]) {
		for (int i = 0; i < quantity.length; i++)
			System.out.println("Result[" + i + "] = " + (result[i]));
	}

	static double round2point(double value) {
		return Math.round((value) * 100.0) / 100.0;
	}

	static double round1point(double value) {
		return Math.round((value) * 10.0) / 10.0;
	}

	static void equationSolver(int i, int j, double k) 
	{
		boolean flag = false;
		double x = 0, y = 0;
		
		//Equation to solve is ax+by=c, need to find every possible value of x and y for given value of c
		System.out.println("\n\nValue of < a,b > :(" + " " + quantity[i] + " , " + quantity[j] + " )");
		

		double x_max = (k - (quantity[j] * 0.01)) / quantity[i];
		double y_max = (k - (quantity[i] * 0.01)) / quantity[j];
		
		System.out.println("Value of <x_max , y_max> :( " + Math.ceil(x_max) + " , " + Math.ceil(y_max)+ " )");

		for (x = 0.01; x < Math.ceil(x_max); x += 0.01) {
			for (y = 0.01; y < Math.ceil(y_max); y += 0.01) {

				x = round2point(x);
				y = round2point(y);

				if (round2point(quantity[i] * x + quantity[j] * y) == round2point(k)) {
					System.out.println("x:" + x + ", y:" + y + " ax+by=" + "" + round2point(quantity[i] * x + quantity[j] * y));

					double temp_i_min = round2point(rate_min[i] + x);
					double temp_j_min = round2point(rate_min[j] + y);

					System.out.println("x-:" + temp_i_min + ", y-:" + temp_j_min);

					varSum = 0.0;

					//updating the rate_min value for selected index i and j and calulating updated varSum
					for (int pos = 0; pos < quantity.length; pos++) 
					{
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
					
					
					System.out.println(
							"Desired Value-:" + round2point(desiredValue) + 
							", Result value-:"+ round2point(resultValue) + "\n"
							);
					
					if (round2point(desiredValue) == round2point(resultValue)) 
					{
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
		if (!flag)
			System.out.println("NO value found for x and y where a: " + quantity[i] + ", b: " + quantity[j]);


	}
}
