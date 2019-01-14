package com.d2;
public class WeaponType extends ItemType {
	private String type;
	private String normalVersionCode;
	private String exceptionalVersionCode;
	private String eliteVersionCode;

	public WeaponType(ItemTypeInMemoryRepresentation itemTypeInMemoryRepresentation, String[] row, int id) {	
		super(id, CombinedStringsDao.get().getString(itemTypeInMemoryRepresentation.getNameStringId()), row[3], row[48]);
		this.type = row[1];
		this.normalVersionCode = row[34];
		this.exceptionalVersionCode = row[35];
		this.eliteVersionCode = row[36];
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
		return "WeaponType [type=" + type + ", normalVersionCode=" + normalVersionCode + ", exceptionalVersionCode="
				+ exceptionalVersionCode + ", eliteVersionCode=" + eliteVersionCode + ", getId()=" + getId()
				+ ", getName()=" + getName() + ", getCode()=" + getCode() + ", getInvFile()=" + getInvFile() + "]";
	}


	
	
	
}