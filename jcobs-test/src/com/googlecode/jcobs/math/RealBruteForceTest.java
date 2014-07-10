package com.googlecode.jcobs.math;

import java.math.BigDecimal;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

public class RealBruteForceTest {

	@Test
	public void testAdd_001_to_100_BigDecimal() {
		compareOperationsBigDecimal(100_00, 2, Operation.ADD);
	}

	@Test
	public void testSubtract_001_to_100_BigDecimal() {
		compareOperationsBigDecimal(100_00, 2, Operation.SUBTRACT);
	}

	@Test
	public void testMultiply_001_to_100_BigDecimal() {
		compareOperationsBigDecimal(100_00, 2, Operation.MULTIPLY);
	}

	@Test
	public void testDivide_001_to_100_BigDecimal() {
		compareOperationsBigDecimal(100_00, 2, Operation.DIVIDE);
	}

	@Test
	public void testAdd_0001_to_10_BigDecimal() {
		compareOperationsBigDecimal(100_00, 3, Operation.ADD);
	}

	@Test
	public void testSubtract_0001_to_10_BigDecimal() {
		compareOperationsBigDecimal(100_00, 3, Operation.SUBTRACT);
	}

	@Test
	public void testMultiply_0001_to_10_BigDecimal() {
		compareOperationsBigDecimal(100_00, 3, Operation.MULTIPLY);
	}

	@Test
	public void testDivide_0001_to_10_BigDecimal() {
		compareOperationsBigDecimal(100_00, 3, Operation.DIVIDE);
	}

	@Test
	public void testAdd_001_to_100_Double() {
		compareOperationsDouble(100_00, 2, Operation.ADD);
	}

	@Test
	public void testSubtract_001_to_100_Double() {
		compareOperationsDouble(100_00, 2, Operation.SUBTRACT);
	}

	@Test
	public void testMultiply_001_to_100_Double() {
		compareOperationsDouble(100_00, 2, Operation.MULTIPLY);
	}

	@Test
	public void testDivide_001_to_100_Double() {
		compareOperationsDouble(100_00, 2, Operation.DIVIDE);
	}

	@Test
	public void testAdd_1000_Random_Numbers_BigDecimal() {
		Random random = new Random(System.currentTimeMillis());
		BigDecimal[] numbers = new BigDecimal[1000];
		for (int i = 0; i < numbers.length; ++i) {
		 	numbers[i] = nextBigDecimal(random, 14, 4);
		}
		for (int i = 0; i < numbers.length; ++i) {
			for (int j = 0; j < numbers.length; ++j) {
				compareBigDecimalOperation(numbers[i], numbers[j], Operation.ADD);
			}
		}
	}
	
	@Test
	public void testSubtract_1000_Random_Numbers_BigDecimal() {
		Random random = new Random(System.currentTimeMillis());
		BigDecimal[] numbers = new BigDecimal[1000];
		for (int i = 0; i < numbers.length; ++i) {
			numbers[i] = nextBigDecimal(random, 14, 4);
		}
		for (int i = 0; i < numbers.length; ++i) {
			for (int j = 0; j < numbers.length; ++j) {
				compareBigDecimalOperation(numbers[i], numbers[j], Operation.SUBTRACT);
			}
		}
	}
	
	@Test
	public void testMultiply_1000_Random_Numbers_BigDecimal() {
		Random random = new Random(System.currentTimeMillis());
		BigDecimal[] numbers = new BigDecimal[1000];
		for (int i = 0; i < numbers.length; ++i) {
			numbers[i] = nextBigDecimal(random, 9, 4);
		}
		for (int i = 0; i < numbers.length; ++i) {
			for (int j = 0; j < numbers.length; ++j) {
				compareBigDecimalOperation(numbers[i], numbers[j], Operation.MULTIPLY);
			}
		}
	}
	
	@Test
	public void testDivide_1000_Random_Numbers_BigDecimal() {
		Random random = new Random(System.currentTimeMillis());
		BigDecimal[] numbers = new BigDecimal[1000];
		for (int i = 0; i < numbers.length; ++i) {
			numbers[i] = nextBigDecimal(random, 14, 4);
		}
		for (int i = 0; i < numbers.length; ++i) {
			for (int j = 0; j < numbers.length; ++j) {
				compareBigDecimalOperation(numbers[i], numbers[j], Operation.DIVIDE);
			}
		}
	}

	private BigDecimal nextBigDecimal(Random random, int precision, int scale) {
		BigDecimal raw = new BigDecimal(random.nextLong() % pow10(precision));
		if (scale != 0) {
			return raw.movePointLeft(scale);
		}
		return raw;
	}

	// @Test
	// public void testAdd_00001_to_1000_Double() {
	// compareOperationsDouble(1000_0000, 4, Operation.ADD);
	// }
	//
	// @Test
	// public void testSubtract_00001_to_1000_Double() {
	// compareOperationsDouble(1000_0000, 4, Operation.SUBTRACT);
	// }
	//
	// @Test
	// public void testMultiply_00001_to_1000_Double() {
	// compareOperationsDouble(1000_0000, 4, Operation.MULTIPLY);
	// }
	//
	// @Test
	// public void testDivide_00001_to_1000_Double() {
	// compareOperationsDouble(1000_0000, 4, Operation.DIVIDE);
	// }

	private static void compareOperationsBigDecimal(int limit, int decimals, Operation op) {
		BigDecimal[] numbers = new BigDecimal[limit + 1];
		for (int i = 0; i <= limit; ++i) {
			numbers[i] = new BigDecimal(i).movePointLeft(decimals);
		}
		long skipped = 0L;
		for (int i = 0; i <= limit; ++i) {
			for (int j = 0; j <= limit; ++j) {
				if (!compareBigDecimalOperation(numbers[i], numbers[j], op)) {
					++skipped;
				}
			}
		}
		System.out.println("Skipped operations: " + skipped + ".");
	}

	private static boolean compareBigDecimalOperation(BigDecimal n1, BigDecimal n2, Operation op) {
		if (op == Operation.DIVIDE && BigDecimal.ZERO.compareTo(n2) == 0) {
			return false;
		}
		BigDecimal expected;
		try {
			expected = operateBigDecimal(n1, n2, op);
		} catch (ArithmeticException ex) {
			return false;
		}
		BigDecimal actual = operateReal(n1, n2, op);
		if (!expected.equals(actual)) {
			Assert.fail(String.format("Expected='%s', actual='%s', Num1='%s', Num2='%s'", //
					expected.toString(), actual.toString(), n1.toString(), n2.toString()));
		}
		return true;
	}

	private static void compareOperationsDouble(int limit, int decimals, Operation op) {
		double[] numbers = new double[limit + 1];
		for (int i = 0; i <= limit; ++i) {
			numbers[i] = (double) i / pow10(decimals);
		}
		long skipped = 0L;
		for (int i = 0; i <= limit; ++i) {
			for (int j = 0; j <= limit; ++j) {
				if (op == Operation.DIVIDE && numbers[j] == 0.0) {
					++skipped;
					continue;
				}
				double expected = operateDouble(numbers[i], numbers[j], op);
				double actual = operateReal(numbers[i], numbers[j], op);
				if ((Math.abs(expected - actual) > 0.00000000001)) {
					Assert.fail(String.format("Expected='%f', actual='%f', Num1='%f', Num2='%f'", //
							expected, actual, numbers[i], numbers[j]));
				}
			}
		}
		System.out.println("Skipped operations: " + skipped + ".");
	}

	private static long pow10(int decimals) {
		long res = 1;
		if (decimals >= 0) {
			for (int i = 0; i < decimals; ++i) {
				res *= 10;
			}
		} else {
			decimals = -decimals;
			for (int i = 0; i < decimals; ++i) {
				res /= 10;
			}
		}
		return res;
	}

	private static BigDecimal operateReal(BigDecimal num1, BigDecimal num2, Operation op) {
		Real real = new Real(num1);
		Real result = null;
		switch (op) {
		case ADD:
			result = real.add(num2);
			break;
		case SUBTRACT:
			result = real.subtract(num2);
			break;
		case MULTIPLY:
			result = real.multiply(num2);
			break;
		case DIVIDE:
			result = real.divide(num2);
			break;
		}
		return result.toBigDecimal();
	}

	private static double operateReal(double num1, double num2, Operation op) {
		Real real = new Real(num1);
		Real result = null;
		switch (op) {
		case ADD:
			result = real.add(new Real(num2));
			break;
		case SUBTRACT:
			result = real.subtract(new Real(num2));
			break;
		case MULTIPLY:
			result = real.multiply(new Real(num2));
			break;
		case DIVIDE:
			result = real.divide(new Real(num2));
			break;
		}
		return result.doubleValue();
	}

	private static BigDecimal operateBigDecimal(BigDecimal num1, BigDecimal num2, Operation op) {
		BigDecimal result = null;
		switch (op) {
		case ADD:
			result = num1.add(num2);
			break;
		case SUBTRACT:
			result = num1.subtract(num2);
			break;
		case MULTIPLY:
			result = num1.multiply(num2);
			break;
		case DIVIDE:
			result = num1.divide(num2);
			break;
		}
		return removeZerosToTheRight(result);
	}

	private static double operateDouble(double num1, double num2, Operation op) {
		switch (op) {
		case ADD:
			return num1 + num2;
		case SUBTRACT:
			return num1 - num2;
		case MULTIPLY:
			return num1 * num2;
		case DIVIDE:
			return num1 / num2;
		}
		return 0.0;
	}

	private static BigDecimal removeZerosToTheRight(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}
		if (value.scale() > 0) {
			value = value.stripTrailingZeros();
			if (value.scale() < 0) {
				value = value.setScale(0);
			}
		}
		return value;
	}

	private static enum Operation {
		ADD, SUBTRACT, MULTIPLY, DIVIDE
	}
	
}
