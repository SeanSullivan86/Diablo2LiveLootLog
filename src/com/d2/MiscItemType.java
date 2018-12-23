package com.d2;
class MiscItemType extends ItemType{

	private String type;

	public MiscItemType(String[] row) {
		super(Integer.parseInt(row[0]), row[1], row[14], row[24]);
		
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