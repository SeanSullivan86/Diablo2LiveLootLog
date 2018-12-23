package com.d2.properties;

public class Durability extends CompositePropertyValue {
	private int durability;
	private int maxDurability;
	
	public Durability(int durability, int maxDurability) {
		this.durability = durability;
		this.maxDurability = maxDurability;
	}
	
	public String getDescription() {
		return "Durability : " + durability + " of " + maxDurability;
	}
}