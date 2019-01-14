package com.d2;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
		Map<Integer,ItemTypeInMemoryRepresentation> itemTypesInMemoryRepresentation = loadItemTypesFromMemorySnapshot(new File("d2ItemTypes.bin"));
		
		itemTypesById = new HashMap<>();
		
		itemTypesById.putAll(WeaponDataLoader.loadFromFile(itemTypesInMemoryRepresentation, new File("Weapons.txt")));
		itemTypesById.putAll(ArmorDataLoader.loadFromFile(itemTypesInMemoryRepresentation, new File("Armor.txt")));
		itemTypesById.putAll(MiscItemDataLoader.loadFromFile(itemTypesInMemoryRepresentation, new File("Misc.txt")));
	}
	

	public ItemType getItemTypeById(int id) {
		return itemTypesById.get(id);
	}
	
	
	private Map<Integer,ItemTypeInMemoryRepresentation> loadItemTypesFromMemorySnapshot(File file) {
		Map<Integer,ItemTypeInMemoryRepresentation> itemTypesInMemoryById = new HashMap<>();
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream( file));
			
			byte[] bytes = new byte[0x1A8];
			
			int i = 0;
			for(;;) {
				if (in.read(bytes) < 0x1A8) break;
				
				itemTypesInMemoryById.put(i, new ItemTypeInMemoryRepresentation(i, bytes));
				i++;
			}
			
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return itemTypesInMemoryById;
	}
	
}