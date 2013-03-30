package com.webmanthan.matrix;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class FileBackedFloatMatrixTest {

	private static FileBackedFloatMatrix fbfm;
	private static FileBackedFloatMatrix fbfm1;
	private static FileBackedFloatMatrix fbfm2;
	private static FileBackedFloatMatrix fbfm3;
	private static FileBackedFloatMatrix fbfm4;
	private static FileBackedFloatMatrix fbfm5;
	private static FileBackedFloatMatrix fbfm6;
	private static FileBackedFloatMatrix fbfm7;
	
	
	@BeforeClass
	public static void setup() throws IOException {
		fbfm = new FileBackedFloatMatrix("TESTM", 5000000, 100000);
		fbfm = new FileBackedFloatMatrix("T2", 5000000, 100000);
		fbfm = new FileBackedFloatMatrix("T3", 5000000, 100000);
		fbfm = new FileBackedFloatMatrix("T4", 5000000, 100000);
		fbfm = new FileBackedFloatMatrix("T5", 5000000, 100000);
		fbfm = new FileBackedFloatMatrix("T6", 5000000, 100000);
		fbfm = new FileBackedFloatMatrix("T7", 5000000, 100000);
	}
	
	@Test
	public void testSet() {
		fbfm.set(10, 2, 1);
	}

	@Test
	public void testGet() {
		assertEquals(1, fbfm.get(10, 2), 0);
	}


}
