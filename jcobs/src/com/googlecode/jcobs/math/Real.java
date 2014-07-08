package com.googlecode.jcobs.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Samuel Y. Deschamps [samueldeschamps at gmail dot com] [Brasil]
 * @since July/2014
 *
 */
public class Real extends Number implements Comparable<Real> {

	private static final long serialVersionUID = -2447989298519065798L;

	private long numerator;
	private long denominator;

	public Real(long numerator, long denominator) {
		this(numerator, denominator, false);
	}

	private Real(long numerator, long denominator, boolean normalized) {
		if (denominator == 0L) {
			throw new IllegalArgumentException("Denominator cannot be zero.");
		}
		this.numerator = numerator;
		this.denominator = denominator;
		if (!normalized) {
			normalize();
		}
	}

	private void normalize() {
		if (denominator < 0L) {
			// Denominator cannot be negative
			numerator = -numerator;
			denominator = -denominator;
		}
		if (numerator == 0L && denominator != 0L) {
			denominator = 1L;
		}
		if (denominator == 1L) {
			// It's already normalized
			return;
		}
		long gcd = gcd(abs(numerator), abs(denominator));
		if (gcd == 1L) {
			// It's already normalized
			return;
		}
		numerator /= gcd;
		denominator /= gcd;
	}

	private long abs(long number) {
		if (number < 0) {
			return -number;
		} else {
			return number;
		}
	}

	public static long gcd(long p, long q) {
		if (q == 0) {
			return p;
		}
		return gcd(q, p % q);
	}

	public Real(long value) {
		this(value, 1L, true);
	}

	// TODO Colocar um Javadoc dizendo que este construtor não é seguro.
	public Real(double value) {
		this(BigDecimal.valueOf(value));
	}

	public Real(BigDecimal value) {
		this(value.multiply(new BigDecimal(pow10(value.scale()))).longValue(), pow10(value.scale()));
	}

	private static long pow10(int exponent) {
		long result = 1;
		for (int i = 0; i < exponent; ++i) {
			result *= 10;
		}
		return result;
	}

	public long getNumerator() {
		return numerator;
	}

	public long getDenominator() {
		return denominator;
	}

	// TODO Testar limites! Pode ser necessário usar o MMC.
	public Real add(Real augend) {
		if (this.denominator == augend.denominator) {
			return new Real(this.numerator + augend.numerator, this.denominator);
		}
		long den = this.denominator * augend.denominator;
		long num = this.numerator * augend.denominator + this.denominator * augend.numerator;
		return new Real(num, den);
	}

	public Real add(BigDecimal augend) {
		return add(new Real(augend));
	}

	public Real add(long augend) {
		return add(new Real(augend));
	}

	// TODO Testar limites! Pode ser necessário usar o MMC mesmo.
	public Real subtract(Real subtrahend) {
		if (this.denominator == subtrahend.denominator) {
			return new Real(this.numerator - subtrahend.numerator, this.denominator);
		}
		long den = this.denominator * subtrahend.denominator;
		long num = this.numerator * subtrahend.denominator - this.denominator * subtrahend.numerator;
		return new Real(num, den);
	}

	public Real subtract(BigDecimal subtrahend) {
		return subtract(new Real(subtrahend));
	}

	public Real subtract(long subtrahend) {
		return subtract(new Real(subtrahend));
	}

	public Real multiply(Real multiplicand) {
		long num = this.numerator * multiplicand.numerator;
		long den = this.denominator * multiplicand.denominator;
		return new Real(num, den);
	}

	public Real multiply(BigDecimal multiplicand) {
		return multiply(new Real(multiplicand));
	}

	public Real multiply(long multiplicand) {
		return multiply(new Real(multiplicand));
	}

	public Real divide(Real divisor) {
		if (divisor.denominator == 0L) {
			throw new ArithmeticException("Division by zero.");
		}
		long num = this.numerator * divisor.denominator;
		long den = this.denominator * divisor.numerator;
		return new Real(num, den);
	}

	public Real divide(BigDecimal divisor) {
		return divide(new Real(divisor));
	}

	public Real divide(long divisor) {
		return divide(new Real(divisor));
	}

	public Real pow(int exponent) {
		Real result = new Real(1L);
		if (exponent > 0) {
			for (int i = 0; i < exponent; ++i) {
				result = result.multiply(this);
			}
		} else {
			exponent = -exponent;
			for (int i = 0; i < exponent; ++i) {
				result = result.divide(this);
			}
		}
		return result;
	}

	public boolean isZero() {
		return numerator == 0L;
	}

	public boolean isPositive() {
		return numerator > 0L;
	}

	public boolean isNegative() {
		return numerator < 0L;
	}

	public boolean isInteger() {
		return denominator == 1L;
	}

	public boolean greaterThan(Real other) {
		return this.compareTo(other) > 0;
	}

	public boolean lessThan(Real other) {
		return this.compareTo(other) < 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Real)) {
			return false;
		}
		Real other = (Real) obj;
		return this.numerator == other.numerator && this.denominator == other.denominator;
	}

	@Override
	public int hashCode() {
		int result = 0;
		result += (int) (numerator ^ (numerator >>> 32));
		result += 31 * (int) (denominator ^ (denominator >>> 32));
		return result;
	}

	@Override
	public int compareTo(Real other) {
		if (this.denominator == other.denominator) {
			return Long.compare(this.numerator, other.numerator);
		} else {
			long thisValue = this.numerator * other.denominator;
			long otherValue = other.numerator * this.denominator;
			return Long.compare(thisValue, otherValue);
		}
	}

	@Override
	public int intValue() {
		return (int) (numerator / denominator);
	}

	@Override
	public String toString() {
		if (isInteger()) {
			return Long.toString(numerator);
		} else {
			return Long.toString(numerator) + "/" + Long.toString(denominator);
		}
	}

	public String toDecimalString(int scale, RoundingMode roundingMode) {
		return toBigDecimal(scale, roundingMode).toPlainString();
	}

	@Override
	public long longValue() {
		return numerator / denominator;
	}

	@Override
	public float floatValue() {
		return (float) doubleValue();
	}

	@Override
	public double doubleValue() {
		return (double) numerator / (double) denominator;
	}

	// TODO Avisar que este método pode gerar exceção!
	public BigDecimal toBigDecimal() {
		return new BigDecimal(numerator).divide(new BigDecimal(denominator));
	}

	public BigDecimal toBigDecimal(int scale, RoundingMode roundingMode) {
		return new BigDecimal(numerator).divide(new BigDecimal(denominator), scale, roundingMode);
	}

	public Real abs() {
		if (isNegative()) {
			return negate();
		} else {
			return this;
		}
	}

	public Real negate() {
		return new Real(-numerator, denominator, true);
	}

	public Real round(int scale, RoundingMode roundingMode) {
		BigDecimal bd = toBigDecimal(scale, roundingMode);
		return new Real(bd);
	}

}
