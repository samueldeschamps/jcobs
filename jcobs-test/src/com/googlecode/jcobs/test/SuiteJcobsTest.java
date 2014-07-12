package com.googlecode.jcobs.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.googlecode.jcobs.csv.CsvReaderTest;
import com.googlecode.jcobs.math.RealBruteForceTest;
import com.googlecode.jcobs.math.RealTest;

@RunWith(Suite.class)
@SuiteClasses({//
RealTest.class, //
		CsvReaderTest.class, //
		RealBruteForceTest.class, //
})
public class SuiteJcobsTest {

}
