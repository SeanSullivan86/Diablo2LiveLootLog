package com.d2.properties;

public class ItemPropertyValue {
	private int mod;
	private int value;
	private ItemProperty prop;
	
	public ItemPropertyValue(int mod, int value, ItemProperty prop) {
		this.mod = mod;
		this.value = value;
		this.prop = prop;
	}


	public int getMod() {
		return mod;
	}


	public int getValue() {
		return value;
	}
	
	public String getDescription() {
		return prop.getDescription(this);
	}


	@Override
	public String toString() {
		return "ItemPropertyValue [mod=" + mod + ", value=" + value + "]";
	}
	
	
}