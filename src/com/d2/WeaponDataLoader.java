package com.d2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeaponDataLoader {
	Map<Integer,WeaponType> weaponTypesById;
	
	
	public static Map<Integer,WeaponType> loadFromFile(
			Map<Integer,ItemTypeInMemoryRepresentation> itemTypesInMemoryRepresentation, 
			File weaponMpqFile) {
		Map<Integer,WeaponType> weaponTypesById = new HashMap<>();
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(weaponMpqFile));
			in.readLine();
			
			String line;
			String[] parts;
			int id = 0;
			while ( (line = in.readLine()) != null) {
				parts = line.split("\t",-1);
				WeaponType type = new WeaponType(itemTypesInMemoryRepresentation.get(id), parts, id++);
				weaponTypesById.put(type.getId(), type);
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
		
		return weaponTypesById;
	}
}