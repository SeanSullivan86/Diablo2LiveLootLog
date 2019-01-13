package com.d2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class ItemType2Dao {
	
	private static ItemType2Dao instance;
	
	public synchronized static ItemType2Dao get() {
		if (instance == null) {
			instance = new ItemType2Dao();
		}
		return instance;
	}
	
	public ItemType2 getItemType(int id) {
		return itemTypesById.get(id);
	}
	
	private ItemType2Dao() {
		loadItemTypes(new File("d2ItemTypes.bin"));
	}
	
	private Map<Integer,ItemType2> itemTypesById = new HashMap<>();
	
	private void loadItemTypes(File file) {
		itemTypesById = new HashMap<>();
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream( file));
			
			byte[] bytes = new byte[0x1A8];
			
			int i = 0;
			for(;;) {
				if (in.read(bytes) < 0x1A8) break;
				
				itemTypesById.put(i, new ItemType2(i, bytes));
				i++;
			}
			
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		ItemType2Dao dao = ItemType2Dao.get();
		
		
	}
}