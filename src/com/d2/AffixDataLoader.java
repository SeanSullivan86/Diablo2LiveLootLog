package com.d2;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AffixDataLoader {
	
	public static void main(String[] args) {

	}
	
	public static Map<Integer,Affix> loadItemPropertiesFromFile(File file) {
		Map<Integer,Affix> affixes = new HashMap<>();
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream( file));
			
			byte[] affixBytes = new byte[144];
			
			int i = 0;
			for(;;) {
				if (in.read(affixBytes) < 144) break;
				
				affixes.put(i, new Affix(i, affixBytes));
				i++;
			}
			
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return affixes;

	}
	
}