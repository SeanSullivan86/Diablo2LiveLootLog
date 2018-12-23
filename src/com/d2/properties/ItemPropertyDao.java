package com.d2.properties;
import java.io.File;
import java.util.Map;

import com.d2.ItemPropertiesDataLoader;

public class ItemPropertyDao {
	
	static ItemPropertyDao instance;
	
	private Map<Integer,ItemProperty> propertiesById; 
	
	public static ItemPropertyDao get() {
		if (instance == null) {
			instance = new ItemPropertyDao();
		}
		return instance;
	}
	
	private ItemPropertyDao() {
		propertiesById = ItemPropertiesDataLoader.loadItemPropertiesFromFile(new File("ATMA_properties2.txt"));
	}
	
	public ItemProperty getById(int id) {
		return propertiesById.get(id);
	}
}