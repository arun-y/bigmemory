package com.webmanthan.matrix;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BigArrayTest {

	private static BigArray ba;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ba = new BigArray("T", 1000000000);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testSet() {
		ba.set(10000000, 2);
	}

	@Test
	public void testGet() {
		assertEquals(2, ba.get(10000000), 0);
	}
}
