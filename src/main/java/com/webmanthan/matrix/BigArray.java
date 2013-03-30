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
public class BigArray {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(BigArray.class);
	
	private final RandomAccessFile randomAccessFile;
	public long size;
	private int blockSize = 1 << 29; //512MiB - default minimum block size - can store up 2^27 floats
	private final MappedByteBuffer[] blockMapIndex;
	
	/**
	 * 
	 * @param name
	 * @param vargs
	 * @throws IOException
	 */
	public BigArray(final String name, final long size, long...vargs) throws IOException {
		this.size = size;
		
		//initialize underlying file based storage and memory mapping it
		this.randomAccessFile = new RandomAccessFile(new File("_ARRY_" + name), "rw");
		
		long totalSize = size << 2;//number of element times 4 bytes of float
		
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
	
	public float get(final int index) {
		int blockMapIndexCount = index / blockSize;
		int blockOffset = index % blockSize;
		//TODO: boundary check
		return blockMapIndex[blockMapIndexCount].getFloat(blockOffset << 2);
	}
	
	public void set(final int index, final float value) {
		int blockMapIndexCount = index / blockSize;
		int blockOffset = index % blockSize;
		//boundary check
		blockMapIndex[blockMapIndexCount].putFloat(blockOffset << 2, value);
	}
}
