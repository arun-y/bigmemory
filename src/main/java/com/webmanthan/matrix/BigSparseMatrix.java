/*******************************************************************************
 * Copyright (c) 2013 Arun Yadav
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ******************************************************************************/
package com.webmanthan.matrix;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author aruny
 *
 */
public class BigSparseMatrix {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BigSparseMatrix.class);
	
	public final int rows;
	public final int columns;
	
	private final BigArray aa;
	private final BigArray ja;
	private final int[] ia;
	
	/**
	 * 
	 * @param name
	 * @param rows
	 * @param columns
	 * @param vargs
	 * @throws IOException
	 */
	public BigSparseMatrix(final String name, final int rows, final int columns, final int nonZeroCount, long...vargs) throws IOException {
		this.rows = rows;
		this.columns = columns;
		
		aa = new BigArray("_AA_" + name, nonZeroCount);
		ja = new BigArray("_JA_" + name, nonZeroCount);
		ia = new int[rows + 1];
	}
	
	public final float get(final int row, final int column) {
		int blockMapIndexCount = row / rowsPerBlock;
		int blockOffset = row % rowsPerBlock;
		return blockMapIndex[blockMapIndexCount].getFloat((blockOffset * columns + column) * 4);
	}
	
	public final void set(final int row, final int column, final float value) {
		int blockMapIndexCount = row / rowsPerBlock;
		int blockOffset = row % rowsPerBlock;
		blockMapIndex[blockMapIndexCount].putFloat((blockOffset * columns + column) * 4, value);
	}

}
