package com.d2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ArmorDataLoader {

	public static Map<Integer,ArmorType> loadFromFile(
			Map<Integer,ItemTypeInMemoryRepresentation> itemTypesInMemoryRepresentation, 
			File ArmorMpqFile) {
		Map<Integer,ArmorType> armorTypesById = new HashMap<>();
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(ArmorMpqFile));
			in.readLine();
			
			String line;
			String[] parts;
			int id = 306;
			while ( (line = in.readLine()) != null) {
				parts = line.split("\t",-1);
				
				ArmorType type = new ArmorType(itemTypesInMemoryRepresentation.get(id), parts, id++);
				armorTypesById.put(type.getId(), type);
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
		return armorTypesById;
	}
}