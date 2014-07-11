package com.googlecode.jcobs.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Immutable, signed real number. A {@code Real} consists of a <code>long</code>
 * numerator and a <code>long</code> denominator, so it supports any value that
 * can be represented as a fraction with numerator and denominator values
 * between <code>Long.MIN_VALUE</code> and <code>Long.MAX_VALUE</code>.
 * 
 * <p>
 * Unlike {@link BigDecimal}, {@code Real} supports infinite repeating decimals
 * (Ex: 3.333333...) without any lose of information. For example,
 * <code>10 divide 3</code> is performed without exception and the result is
 * assured to reach exactly <code>10</code> again if multiplied to
 * <code>3</code>.
 * 
 * <p>
 * {@code Real} stores the number in its exact form, unlike floating-point data
 * types like {@link Double} that keeps an approximation for some values.<br>
 * Reference: <a
 * href=http://www.exploringbinary.com/why-0-point-1-does-not-exist-in-floating
 * -point/>http://www.exploringbinary.com/why-0-point-1-does-not-exist-in-
 * floating -point/</a>
 * 
 * <p>
 * Any value that is not exactly supported by {@code Real} will throw an
 * {@link ArithmeticException} instead of keeping an approximated value. For
 * this reason this class is more reliable for currency computations or exact
 * values math. But it's not recommended for values bigger than
 * <code>Integer.MAX_VALUE</code>.
 *
 * <p>
 * The {@code Real} class provides operations for arithmetic, comparison,
 * rounding, hashing, and conversion to/from BigDecimal and primitive numbers
 * (double, float, long, int, short, byte).
 * 
 * <p>
 * The value is stored always in its most reduced fraction, so there's no way to
 * represent the same value in a different numerator/denominator pair. Thus,
 * it's safe to use <code>equals()</code>, <code>hashCode()</code> and use
 * {@code Real} objects in sets and maps as key.
 * 
 * <p>
 * As {@code Real} class is immutable, every arithmetic operation will create a
 * new object as result instead of modifying the <code>this</code> object state.
 * All methods and constructors for this class throw
 * {@link NullPointerException} when passed a {@code null} object reference for
 * any input parameter.
 * 
 * @author Samuel Y. Deschamps [samueldeschamps at gmail dot com]
 * @since July/2014
 *
 */
public class Real extends Number implements Comparable<Real> {

	private static final long serialVersionUID = -2447989298519065798L;

	private final long numerator;
	private final long denominator;

	public Real(long numerator, long denominator) {
		this(numerator, denominator, false);
	}

	private Real(long numerator, long denominator, boolean normalized) {
		if (denominator == 0L) {
			throw new ArithmeticException("Division by zero.");
		}
		if (!normalized) {
			if (denominator < 0L) {
				// Denominator cannot be negative
				numerator = -numerator;
				denominator = -denominator;
			}
			if (numerator == 0L) {
				denominator = 1L;
			} else if (denominator != 1L) {
				long gcd = gcd(abs(numerator), abs(denominator));
				if (gcd > 1L) {
					numerator /= gcd;
					denominator /= gcd;
				}
			}
		}
		this.numerator = numerator;
		this.denominator = denominator;
	}

	private static final BigInteger BIG_MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
	private static final BigInteger BIG_MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);

	private Real(BigInteger numerator, BigInteger denominator, boolean normalized) {
		int comparisonDen = denominator.compareTo(BigInteger.ZERO);
		if (comparisonDen == 0) {
			throw new ArithmeticException("Division by zero.");
		}
		if (!normalized) {
			if (comparisonDen < 0) {
				// Denominator cannot be negative
				numerator = numerator.negate();
				denominator = denominator.negate();
			}
			if (numerator.compareTo(BigInteger.ZERO) == 0) {
				denominator = BigInteger.ONE;
			} else if (denominator.compareTo(BigInteger.ONE) != 0) {
				BigInteger gcd = numerator.gcd(denominator);
				if (gcd.compareTo(BigInteger.ONE) > 0) {
					numerator = numerator.divide(gcd);
					denominator = denominator.divide(gcd);
				}
			}
		}
		if (numerator.compareTo(BIG_MAX_LONG) > 0 || numerator.compareTo(BIG_MIN_LONG) < 0) {
			throw new ArithmeticException("Numeric overflow");
		}
		if (denominator.compareTo(BIG_MAX_LONG) > 0 || denominator.compareTo(BIG_MIN_LONG) < 0) {
			throw new ArithmeticException("Numeric overflow");
		}
		this.numerator = numerator.longValue();
		this.denominator = denominator.longValue();
	}

	private long abs(long number) {
		if (number < 0) {
			return -number;
		} else {
			return number;
		}
	}

	public static long gcd(long p, long q) {
		while (q != 0) {
			long aux = q;
			q = p % q;
			p = aux;
		}
		return p;
	}

	public Real(long value) {
		this(value, 1L, true);
	}

	// TODO Colocar um Javadoc dizendo que este construtor não é seguro.
	public Real(double value) {
		this(BigDecimal.valueOf(value));
	}

	public Real(BigDecimal value) {
		this(//
				(value.scale() < 0 ? value : value.movePointRight(value.scale())).longValue(), //
				value.scale() < 0 ? 1 : pow10(value.scale()), //
				false);
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

	public Real add(Real augend) {
		if (this.denominator == augend.denominator) {
			if (overIntLimits(this.numerator, augend.numerator)) {
				BigInteger num = BigInteger.valueOf(this.numerator).add(BigInteger.valueOf(augend.numerator));
				return new Real(num, BigInteger.valueOf(this.denominator), false);
			} else {
				return new Real(this.numerator + augend.numerator, this.denominator, false);
			}
		} else {
			if (overIntLimits(this.numerator, this.denominator, augend.numerator, augend.denominator)) {
				BigInteger den = BigInteger.valueOf(this.denominator).multiply(BigInteger.valueOf(augend.denominator));
				BigInteger v1 = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf(augend.denominator));
				BigInteger v2 = BigInteger.valueOf(this.denominator).multiply(BigInteger.valueOf(augend.numerator));
				BigInteger num = v1.add(v2);
				return new Real(num, den, false);
			} else {
				long den = this.denominator * augend.denominator;
				long num = this.numerator * augend.denominator + this.denominator * augend.numerator;
				return new Real(num, den, false);
			}
		}
	}

	public Real add(BigDecimal augend) {
		return add(new Real(augend));
	}

	public Real add(long augend) {
		return add(new Real(augend));
	}

	public Real subtract(Real subtrahend) {
		return add(subtrahend.negate());
	}

	public Real subtract(BigDecimal subtrahend) {
		return subtract(new Real(subtrahend));
	}

	public Real subtract(long subtrahend) {
		return subtract(new Real(subtrahend));
	}

	public Real multiply(Real multiplicand) {
		if (overIntLimits(this.numerator, multiplicand.numerator, this.denominator, multiplicand.denominator)) {
			BigInteger num = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf(multiplicand.numerator));
			BigInteger den = BigInteger.valueOf(this.denominator).multiply(BigInteger.valueOf(multiplicand.denominator));
			return new Real(num, den, false);
		} else {
			long num = this.numerator * multiplicand.numerator;
			long den = this.denominator * multiplicand.denominator;
			return new Real(num, den, false);
		}
	}

	private static boolean overIntLimits(long a, long b) {
		if (a > Integer.MAX_VALUE || a < Integer.MIN_VALUE) {
			return true;
		}
		if (b > Integer.MAX_VALUE || b < Integer.MIN_VALUE) {
			return true;
		}
		return false;
	}

	private static boolean overIntLimits(long a, long b, long c, long d) {
		return overIntLimits(a, b) || overIntLimits(c, d);
	}

	public Real multiply(BigDecimal multiplicand) {
		return multiply(new Real(multiplicand));
	}

	public Real multiply(long multiplicand) {
		return multiply(new Real(multiplicand));
	}

	public Real divide(Real divisor) {
		return multiply(divisor.invert());
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
			if (overIntLimits(this.numerator, this.denominator, other.numerator, other.denominator)) {
				BigInteger thisValue = BigInteger.valueOf(this.numerator).multiply(BigInteger.valueOf(other.denominator));
				BigInteger otherValue = BigInteger.valueOf(other.numerator).multiply(BigInteger.valueOf(this.denominator));
				return thisValue.compareTo(otherValue);
			} else {
				long thisValue = this.numerator * other.denominator;
				long otherValue = other.numerator * this.denominator;
				return Long.compare(thisValue, otherValue);
			}
		}
	}

	@Override
	public int intValue() {
		return (int) (numerator / denominator);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(toShortDecimalString(10, RoundingMode.HALF_EVEN));
		sb.append(" (");
		sb.append(Long.toString(numerator));
		sb.append("/");
		sb.append(Long.toString(denominator));
		sb.append(")");
		return sb.toString();
	}

	public String toShortDecimalString(int maxScale, RoundingMode roundingMode) {
		BigDecimal decimal = toBigDecimal(maxScale, roundingMode);
		if (decimal.compareTo(BigDecimal.ZERO) == 0) {
			return "0";
		} else {
			decimal = decimal.stripTrailingZeros();
			if (decimal.scale() < 0) {
				decimal = decimal.setScale(0);
			}
			return decimal.toPlainString();
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

	public Real invert() {
		return new Real(denominator, numerator, true);
	}

	public Real max(Real other) {
		if (this.greaterThan(other)) {
			return this;
		} else {
			return other;
		}
	}

	public Real min(Real other) {
		if (this.lessThan(other)) {
			return this;
		} else {
			return other;
		}
	}

	public Real round(int scale, RoundingMode roundingMode) {
		BigDecimal bd = toBigDecimal(scale, roundingMode);
		return new Real(bd);
	}

	// TODO Implement remainder(Real): Real
	// TODO Implement divideAndremainder(Real): Real[]

}
