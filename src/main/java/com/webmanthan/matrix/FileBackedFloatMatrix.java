package com.webmanthan.matrix;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

public class FileBackedFloatMatrix {
	
	private final RandomAccessFile randomAccessFile;
	private final int rows;
	private final int columns;
	
	private long blockSize = 1 << 20; //512MiB
	private int rowsPerBlock; 
	private final MappedByteBuffer[] blockMapIndex;
	
	public FileBackedFloatMatrix(final String name, final int rows, final int columns) throws IOException {
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
