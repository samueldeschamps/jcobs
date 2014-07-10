package com.googlecode.jcobs.math;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class RealTest {

	@Test
	public void testAdd_1() {
		Real actual = new Real(5L).add(new Real(10L));
		Real expected = new Real(15L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_2() {
		Real actual = new Real(10L, 2L).add(new Real(20L, 2L));
		Real expected = new Real(15L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_3() {
		Real actual = new Real(10L, 2L).add(new Real(40L, 4L));
		Real expected = new Real(15L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_4() {
		Real actual = new Real(10L, 3L).add(new Real(40L, 3L));
		Real expected = new Real(50L, 3L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_5() {
		Real actual = new Real(-10L, 2L).add(new Real(10L, 2L));
		Real expected = new Real(0L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_6() {
		Real actual = new Real(-10L, 2L).add(new Real(-20L, 4L));
		Real expected = new Real(-10L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_7() {
		Real actual = new Real(0L, 2L).add(new Real(0L, 4L));
		Real expected = new Real(0L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_8() {
		Real actual = new Real(0L).add(new Real(0L, 4L));
		Real expected = new Real(0L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_9() {
		Real actual = new Real(0L).add(new Real(0L));
		Real expected = new Real(0L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_10() {
		Real actual = new Real(1L, 100L).add(1L);
		Real expected = new Real(101L, 100L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_11() {
		Real num1 = new Real(10L, -2L);
		Real num2 = new Real(-5_000L, 4L);
		Real actual = num1.add(num2);
		Real expected = new Real(-1_255L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_LargeNumbers1() {
		Real num1 = new Real(Integer.MAX_VALUE);
		Real num2 = new Real(Integer.MAX_VALUE, 1_000_000_000L);
		Real actual = num1.add(num2);
		Real expected = new Real(2_147_483_649_147_483_647L, 1_000_000_000L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testAdd_LargeNumbers2() {
		Real num1 = new Real(123_456_789L);
		Real num2 = new Real(2_345_678_012L, 250_000L);
		Real actual = num1.add(num2);
		Real expected = new Real(7_716_635_732_003L, 62_500L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSubtract_1() {
		Real num1 = new Real(984L, 3L);
		Real num2 = new Real(10L);
		Real actual = num1.subtract(num2);
		Real expected = new Real(318L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSubtract_2() {
		Real num1 = new Real(19_981L, 7L);
		Real num2 = new Real(10L, 5L);
		Real actual = num1.subtract(num2);
		Real expected = new Real(19967L, 7L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSubtract_3() {
		Real num1 = new Real(10L, -2L);
		Real num2 = new Real(-5_000L, 4L);
		Real actual = num1.subtract(num2);
		Real expected = new Real(1_245L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSubtract_4() {
		Real num1 = new Real(-10L);
		Real num2 = new Real(5_000L);
		Real actual = num1.subtract(num2);
		Real expected = new Real(-5010L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSubtract_5() {
		Real num1 = new Real(1_234_567L, 2L);
		Real num2 = new Real(2_469_134L, 4L);
		Real actual = num1.subtract(num2);
		Real expected = new Real(0L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testSubtract_6() {
		Real actual = new Real(10L).subtract(112L);
		Real expected = new Real(-102L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMultiply_1() {
		Real actual = new Real(0L).multiply(new Real(0L));
		Real expected = new Real(0L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMultiply_2() {
		Real actual = new Real(10L, 2L).multiply(new Real(3L, 58L));
		Real expected = new Real(15L, 58L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMultiply_3() {
		Real actual = new Real(101L, 2L).multiply(new Real(548L, 9L));
		Real expected = new Real(27674L, 9L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMultiply_4() {
		Real actual = new Real(-11L, 2L).multiply(new Real(548L, 9L));
		Real expected = new Real(-3014L, 9L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMultiply_5() {
		Real actual = new Real(-11L, 2L).multiply(new Real(548L, 9L));
		Real expected = new Real(-3014L, 9L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMultiply_6() {
		Real actual = new Real(-11L, 2L).multiply(new Real(-548L, 9L));
		Real expected = new Real(3014L, 9L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMultiply_7() {
		Real actual = new Real(10L, 3L).multiply(new Real(3L));
		Real expected = new Real(10L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMultiply_8() {
		Real actual = new Real(10L, 3L).multiply(7L);
		Real expected = new Real(70L, 3L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testDivide_1() {
		Real actual = new Real(1L).divide(3L);
		Real expected = new Real(1L, 3L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testDivide_2() {
		Real actual = new Real(1L, 2L).divide(3L);
		Real expected = new Real(1L, 6L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testDivide_3() {
		Real actual = new Real(464_621L, 5_462L).divide(new Real(65_421L, 8_465L));
		Real expected = new Real(3_933_016_765L, 357_329_502L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testMultipleOperations_1() {
		Real actual = new Real(1000L).divide(new Real(3L)).multiply(new Real(3L));
		Real expected = new Real(1000L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCreateFromDouble_1() {
		Real actual = new Real(0.05);
		Real expected = new Real(1, 20);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCreateFromDouble_2() {
		Real actual = new Real(10.534578);
		Real expected = new Real(5267289L, 500000L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCreateFromDouble_3() {
		Real actual = new Real(5267289.0 / 500000.0);
		Real expected = new Real(5267289L, 500000L);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testCompareTo_1() {
		List<Real> list = new ArrayList<>();
		list.add(new Real(1, 2));
		list.add(new Real(1, 3));
		list.add(new Real(1, 4));
		list.add(new Real(2, 1));
		list.add(new Real(3, 2));
		list.add(new Real(-1000000, 2));
		list.add(new Real(1, 1));
		list.add(new Real(1000, 999));
		list.add(new Real(1000, 999));
		list.add(new Real(-1, 2));
		list.add(new Real(-1, 1));
		list.add(new Real(-100, 2));
		list.add(new Real(123456, 7));
		list.add(new Real(0, 7));
		Collections.sort(list);
		Assert.assertEquals("[-500000, -50, -1, -1/2, 0, 1/4, 1/3, 1/2, 1, 1000/999, 1000/999, 3/2, 2, 123456/7]", list.toString());
	}
	
	@Test
	public void testCompareTo_MaxInt_MinInt() {
		List<Real> list = new ArrayList<>();
		list.add(new Real(Integer.MAX_VALUE, 1));
		list.add(new Real(Integer.MIN_VALUE, 1));
		list.add(new Real(1, 2));
		list.add(new Real(1, 3));
		list.add(new Real(1, 4));
		list.add(new Real(2, 1));
		list.add(new Real(3, 2));
		list.add(new Real(-1000000, 2));
		list.add(new Real(1, 1));
		list.add(new Real(1000, 999));
		list.add(new Real(1000, 999));
		list.add(new Real(-1, 2));
		list.add(new Real(-1, 1));
		list.add(new Real(-100, 2));
		list.add(new Real(123456, 7));
		list.add(new Real(0, 7));
		Collections.sort(list);
		Assert.assertEquals("[-2147483648, -500000, -50, -1, -1/2, 0, 1/4, 1/3, 1/2, 1, 1000/999, 1000/999, 3/2, 2, 123456/7, 2147483647]", list.toString());
	}
	
	@Test
	public void testCompareTo_MaxLong_MinLong() {
		List<Real> list = new ArrayList<>();
		list.add(new Real(Integer.MAX_VALUE, 1));
		list.add(new Real(1, 2));
		list.add(new Real(1, 3));
		list.add(new Real(1, 4));
		list.add(new Real(Integer.MIN_VALUE, 1));
		list.add(new Real(2, 1));
		list.add(new Real(3, 2));
		list.add(new Real(Long.MIN_VALUE));
		list.add(new Real(-1000000, 2));
		list.add(new Real(1, 1));
		list.add(new Real(1000, 999));
		list.add(new Real(1000, 999));
		list.add(new Real(-1, 2));
		list.add(new Real(Long.MAX_VALUE));
		list.add(new Real(-1, 1));
		list.add(new Real(-100, 2));
		list.add(new Real(123456, 7));
		list.add(new Real(0, 7));
		Collections.sort(list);
		Assert.assertEquals("[-9223372036854775808, -2147483648, -500000, -50, -1, -1/2, 0, 1/4, 1/3, 1/2, 1, 1000/999, 1000/999, 3/2, 2, 123456/7, 2147483647, 9223372036854775807]", list.toString());
	}

	@Test
	public void testRound_1() {
		Real real = new Real(10L, 3L);
		Real actual = real.round(2, RoundingMode.HALF_EVEN);
		Real expected = new Real(3.33);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRound_2() {
		Real real = new Real(3.1415926);
		Real actual = real.round(3, RoundingMode.HALF_EVEN);
		Real expected = new Real(3.142);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRound_3() {
		Real real = new Real(3.1415926);
		Real actual = real.round(3, RoundingMode.HALF_DOWN);
		Real expected = new Real(3.142);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRound_4() {
		Real real = new Real(3.1415926);
		Real actual = real.round(3, RoundingMode.DOWN);
		Real expected = new Real(3.141);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRound_5() {
		Real real = new Real(3.1411);
		Real actual = real.round(3, RoundingMode.UP);
		Real expected = new Real(3.142);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRound_6() {
		Real real = new Real(3.1415926);
		Real actual = real.round(3, RoundingMode.HALF_UP);
		Real expected = new Real(3.142);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRound_7() {
		Real real = new Real(97652.15);
		Real actual = real.round(7, RoundingMode.HALF_EVEN);
		Real expected = new Real(97652.15);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRound_8() {
		Real real = new Real(97652.15);
		Real actual = real.round(-2, RoundingMode.HALF_EVEN);
		Real expected = new Real(97700);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testNegate_1() {
		Real actual = new Real(1000).negate();
		Real expected = new Real(-1000);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testNegate_2() {
		Real actual = new Real(0).negate();
		Real expected = new Real(0);
		Assert.assertEquals(expected, actual);
	}

}
