package com.d2;

public class ItemType {
	private int id;
	private String name;
	private String code;
	private String invFile;
	
	public ItemType(int id, String name, String code, String invFile) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.invFile = invFile;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return CombinedStringsDao.get().getString(ItemType2Dao.get().getItemType(id).getNameStringId());
				
	}

	public String getCode() {
		return code;
	}

	public String getInvFile() {
		return invFile;
	}
	
	
}