package com.d2;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.d2.properties.ItemProperty;

public class ItemPropertiesDataLoader {
	
	public static Map<Integer, ItemProperty> loadItemPropertiesFromFile(File file) {
		Map<Integer,ItemProperty> propertiesById = new HashMap<>();
		
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(file));
			in.readLine();
			
			String line;
			String[] parts;
			while ( (line = in.readLine()) != null) {
				parts = line.split("\t",-1);
				if (parts[2].equals("")) continue;
				ItemProperty type = new ItemProperty(parts);
				propertiesById.put(type.getId(), type);
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
		return propertiesById;
	}
}