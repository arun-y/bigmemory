package com.webmanthan.matrix;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

public class FileBackedFloatMatrixTest {

	private static FileBackedFloatMatrix fbfm;
	
	
	@BeforeClass
	public static void setup() throws IOException {
		fbfm = new FileBackedFloatMatrix("TESTM", 5000000, 100000);
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
