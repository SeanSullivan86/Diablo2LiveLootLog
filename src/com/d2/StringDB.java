package com.d2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

/**
 * Represents one of the 3 separate String tables (regular strings, Expansion strings,
 * and Patch strings).
 *
 */
public class StringDB {
	
	private Map<Integer,Integer> nameHashCodeToIdx = new HashMap<>();
    private int[] addresses;
    private byte[] stringBytes;
    
    private int idCount;
    private int addressTableSize;
	
	public StringDB(File addressTable, File indexTable, File stringData) {
		loadStringIndexes(indexTable);
		loadStringAddresses(addressTable);
		loadStringBytes(stringData);
	}
	
	public String getString(int stringId) {
		if (!nameHashCodeToIdx.containsKey(stringId)) {
			return null;
		}
		int idx = nameHashCodeToIdx.get(stringId);
		//System.out.println(String.format("Idx : 0x%08X", idx));
		
		int addressOffset = addresses[idx];
		//System.out.println(String.format("Address Offset : 0x%08X", addressOffset));
		//System.out.println(String.format("Original Address : 0x%08X", addressOffset + 0x0688004C));
		return convertBytesToString(stringBytes, addressOffset);
	}
	
	private void loadStringIndexes(File indexFile) {
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(indexFile));
			
			for (int i = 0; i < 2; i++) in.read();
			idCount = readShort(in);
			addressTableSize = readShort(in);
			for (int i = 0; i < 15; i++) in.read();
						
			byte[] bytes = new byte[0x1A8];
			
			for(int i = 0; i < idCount; i++) {
				int idx = readShort(in);
				
				nameHashCodeToIdx.put(i, idx);
				//if (i == 0x7B0) System.out.println(idx);
			}
			
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void loadStringAddresses(File addressTable) {
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(addressTable));
			
			addresses = new int[addressTableSize];
			
			int offset = readInt(in);
			addresses[0] = 0;
			for(int i = 1; i < addressTableSize; i++) {
				addresses[i] = readInt(in) - offset;
			}
			
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void loadStringBytes(File stringsFile) {
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(stringsFile));
			stringBytes = IOUtils.toByteArray(in);
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static int readInt(InputStream in) {
		try {
			return in.read() + 256*in.read() + 256*256*in.read() + 256*256*256*in.read();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private static short readShort(InputStream in) {
		try {
			return (short) (in.read() + 256*in.read());
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	public static String convertBytesToString(byte[] input, int offset) {
		
		int i = 0;
		while (input[offset + 2*i] != 0 || input[offset + 2*i + 1] != 0) {
			i++;
		}
		
		return new String(Arrays.copyOfRange(input, offset, offset + 2*i), StandardCharsets.UTF_16LE);
	}
}