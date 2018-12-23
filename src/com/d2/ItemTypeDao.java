package com.d2;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ItemTypeDao {
		
	private Map<Integer,ItemType> itemTypesById;

	static ItemTypeDao instance;
	
	public static ItemTypeDao get() {
		if (instance == null) {
			instance = new ItemTypeDao();
		}
		return instance;
	}
	
	private ItemTypeDao() {
		
		itemTypesById = new HashMap<>();
		
		itemTypesById.putAll(WeaponDataLoader.loadFromFile(new File("Weapons.txt")));
		itemTypesById.putAll(ArmorDataLoader.loadFromFile(new File("Armor.txt")));
		itemTypesById.putAll(MiscItemDataLoader.loadFromFile(new File("Misc.txt")));
	}
	

	public ItemType getItemTypeById(int id) {
		return itemTypesById.get(id);
	}
	
	
}