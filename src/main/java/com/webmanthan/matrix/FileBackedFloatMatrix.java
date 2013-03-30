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

public class FileBackedFloatMatrix {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileBackedFloatMatrix.class);
	
	private final RandomAccessFile randomAccessFile;
	public final int rows;
	public final int columns;
	
	private long blockSize = 1 << 29; //512MiB - default minimum block size - can store up 2^27 floats
	private int rowsPerBlock; 
	private final MappedByteBuffer[] blockMapIndex;
	
	/**
	 * 
	 * @param name
	 * @param rows
	 * @param columns
	 * @param vargs
	 * @throws IOException
	 */
	public FileBackedFloatMatrix(final String name, final int rows, final int columns, long...vargs) throws IOException {
		this.rows = rows;
		this.columns = columns;
		
		//initialize underlying file based storage and memory mapping it
		this.randomAccessFile = new RandomAccessFile(new File(name), "rw");
		
		long totalSize = rows * columns * 4L;//number of element times 4 bytes of float
		//determine maximum number of rows that can accommodate completely into BLOCK_SIZE
		long rowSize = columns * 4L;
		rowsPerBlock = (int) (blockSize / rowSize);
		blockSize = rowSize * rowsPerBlock;
		if (blockSize == 0) {
			//BLOCK_SIZE is not sufficient to hold even one row
			//incrementing block size to accommodate at least one row
			blockSize = rowSize;
		}
		
		int totalBlockCount = (int)(totalSize / blockSize) + 1;//one additional for remainder block
		//TODO: if its exact divide, we are wasting one place
		blockMapIndex = new MappedByteBuffer[totalBlockCount];
		int blockMapIndexCounter = 0;
		for (long offset = 0; offset < totalSize; offset += blockSize)
		{
			long remainingBlockSize = Math.min(blockSize, totalSize - offset); 
			blockMapIndex[blockMapIndexCounter++] = randomAccessFile.getChannel().map(MapMode.READ_WRITE, offset, remainingBlockSize);
		}
		
		LOGGER.info("Initialized BigFloatMatrix");
	}
	
	public float get(final int row, final int column) {
		int blockMapIndexCount = row / rowsPerBlock;
		int blockOffset = row % rowsPerBlock;
		return blockMapIndex[blockMapIndexCount].getFloat((blockOffset * columns + column) * 4);
	}
	
	public void set(final int row, final int column, final float value) {
		int blockMapIndexCount = row / rowsPerBlock;
		int blockOffset = row % rowsPerBlock;
		blockMapIndex[blockMapIndexCount].putFloat((blockOffset * columns + column) * 4, value);
	}

}
