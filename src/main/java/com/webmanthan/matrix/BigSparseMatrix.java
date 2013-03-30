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
