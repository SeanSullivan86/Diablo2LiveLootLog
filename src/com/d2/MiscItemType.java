package com.d2;
class MiscItemType extends ItemType{

	private String type;

	public MiscItemType(ItemTypeInMemoryRepresentation itemTypeInMemoryRepresentation, String[] row) {
		// row[1] has name from file
		super(Integer.parseInt(row[0]), CombinedStringsDao.get().getString(itemTypeInMemoryRepresentation.getNameStringId()), row[14], row[24]);
		
		this.type = row[33];

	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "MiscItemType [type=" + type + ", getId()=" + getId() + ", getName()=" + getName() + ", getCode()="
				+ getCode() + ", getInvFile()=" + getInvFile() + "]";
	}




	
}