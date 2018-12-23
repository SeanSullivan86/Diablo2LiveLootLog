package com.d2.properties;

public class FireDamage extends CompositePropertyValue {
	private int minDamage;
	private int maxDamage;
	
	
	public FireDamage(int minDamage, int maxDamage) {
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
	}
	
	public String getDescription() {
		if (minDamage == maxDamage) {
			return "+" + minDamage + " Fire Damage";
		} else {
			return "+ " + minDamage + "-" + maxDamage + " Fire Damage";
		}
	}
}