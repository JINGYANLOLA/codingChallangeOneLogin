package com.code.oa;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Scanner;

@SpringBootApplication
public class Operator {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter(System.getProperty("line.separator"));
		System.out.print("? ");
		String input = scanner.next();
		String result = Operator.operate(input);
		System.out.println(String.format("= %s", result));
	}

	public static String operate(String input) {
		// Operands and operators shall be separated by one or more spaces
		String[] components = input.split("\\s+");
		/**
		 * ? 1/2 * 3_3/4 components[0]: 1/2 components[1]: * components[2]: 3_3/4
		 */
		long[] first = mixedNumbersToParts(components[0]);
		long[] second = mixedNumbersToParts(components[2]);
		long firstNumerator = first[0];
		long firstDenominator = first[1];
		long secondNumerator = second[0];
		long secondDenominator = second[1];
		long resultNumerator = 0;
		long resultDenominator = 1;
		// Now we deal with operator
		switch (components[1]) {
			case "+":
				resultNumerator = secondNumerator * firstDenominator + firstNumerator * secondDenominator;
				resultDenominator = firstDenominator * secondDenominator;
				break;
			case "-":
				resultNumerator = secondNumerator * firstDenominator - firstNumerator * secondDenominator;
				resultDenominator = firstDenominator * secondDenominator;
				break;
			case "*":
				resultNumerator = firstNumerator * secondNumerator;
				resultDenominator = firstDenominator * secondDenominator;
				break;
			case "/":
				resultNumerator = firstNumerator * secondDenominator;
				resultDenominator = firstDenominator * secondNumerator;
				break;
		}

		// reduce fraction e.g. "4/2"
		long[] resultAfterReducing = asFraction(resultNumerator, resultDenominator);
		long numeratorAfterReducing = resultAfterReducing[0];
		long denominatorAfterReducing = resultAfterReducing[1];
		if (denominatorAfterReducing == 1) {
			return String.valueOf(numeratorAfterReducing);
		} else if (numeratorAfterReducing < denominatorAfterReducing) {
			return numeratorAfterReducing + "/" + denominatorAfterReducing;
		} else {
			// change improper fraction to mixed numbers;
			long finalWhole = numeratorAfterReducing / denominatorAfterReducing;
			long finalNumerator = numeratorAfterReducing % denominatorAfterReducing;
			return finalWhole + "_" + finalNumerator + "/" + denominatorAfterReducing;
		}

	}

	public static long[] mixedNumbersToParts(String mixedNumbers) {
		// Mixed numbers will be represented by whole_numerator/denominator. e.g.
		// "3_1/4"
		String[] mixedParts = mixedNumbers.split("_");
		long whole = 0;
		long numerator = 0;
		long denominator = 1;
		if (mixedParts.length == 1) {
			// There is no mixed numbers. e.g. "1/2" or "1"
			String properFractionOrWholeNumber = mixedParts[0];
			String[] properFractionOrWholeNumberParts = properFractionOrWholeNumber.split("/");
			if (properFractionOrWholeNumberParts.length == 1) {
				// It is a whole
				whole = Long.parseLong(properFractionOrWholeNumberParts[0]);
			} else {
				// It is a proper fraction
				numerator = Long.parseLong(properFractionOrWholeNumberParts[0]);
				denominator = Long.parseLong(properFractionOrWholeNumberParts[1]);
			}
		} else {
			whole = Long.parseLong(mixedParts[0]);
			String properFraction = mixedParts[1];
			String[] properFractionParts = properFraction.split("/");
			numerator = Long.parseLong(properFractionParts[0]);
			denominator = Long.parseLong(properFractionParts[1]);
		}
		long[] tmp = new long[3];
		tmp[0] = whole;
		tmp[1] = numerator;
		tmp[2] = denominator;
		// change mixed numbers to proper fraction or stay whole
		long[] result = new long[2];
		if (tmp[2] == 1) {
			// whole
			result[0] = tmp[0];
			result[1] = 1;
		} else {
			// fraction
			result[0] = tmp[0] * tmp[2] + tmp[1];
			result[1] = tmp[2];
		}
		return result;

	}

	public static long gcd(long a, long b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	public static long[] asFraction(long a, long b) {
		long gcd = gcd(a, b);
		long[] result = new long[2];
		result[0] = (a / gcd);
		result[1] = (b / gcd);
		return result;
	}

}
