package com.d2;
public class ArmorType extends ItemType {
	private String type;
	private String normalVersionCode;
	private String exceptionalVersionCode;
	private String eliteVersionCode;
	

	public ArmorType(ItemTypeInMemoryRepresentation itemTypeInMemoryRepresentation, String[] row, int id) {
		super(id, CombinedStringsDao.get().getString(itemTypeInMemoryRepresentation.getNameStringId()), row[17], row[34]);
		this.type = row[48];
		this.normalVersionCode = row[23];
		this.exceptionalVersionCode = row[24];
		this.eliteVersionCode = row[25];
	}

	public String getType() {
		return type;
	}

	public String getNormalVersionCode() {
		return normalVersionCode;
	}
	public String getExceptionalVersionCode() {
		return exceptionalVersionCode;
	}
	public String getEliteVersionCode() {
		return eliteVersionCode;
	}

	@Override
	public String toString() {
		return "ArmorType [type=" + type + ", normalVersionCode=" + normalVersionCode + ", exceptionalVersionCode="
				+ exceptionalVersionCode + ", eliteVersionCode=" + eliteVersionCode + ", getId()=" + getId()
				+ ", getName()=" + getName() + ", getCode()=" + getCode() + ", getInvFile()=" + getInvFile() + "]";
	}

	
	
	
}