package com.d2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MiscItemDataLoader {

	public static Map<Integer,MiscItemType> loadFromFile(
			Map<Integer,ItemTypeInMemoryRepresentation> itemTypesInMemoryRepresentation, 
			File ArmorMpqFile) {
		Map<Integer,MiscItemType> miscItemTypesById = new HashMap<>();
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(ArmorMpqFile));
			in.readLine();
			
			String line;
			String[] parts;
			
			while ( (line = in.readLine()) != null) {
				parts = line.split("\t",-1);
				
				MiscItemType type = new MiscItemType(itemTypesInMemoryRepresentation.get(Integer.parseInt(parts[0])), parts);
				miscItemTypesById.put(type.getId(), type);
				System.out.println(type);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return miscItemTypesById;
	}
}