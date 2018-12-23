package com.d2.properties;

public class ColdDamage extends CompositePropertyValue {
	
	private int minDamage;
	private int maxDamage;
	private int duration;
	
	public ColdDamage(int minDamage, int maxDamage, int duration) {
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
		this.duration = duration;
	}

	public String getDescription() {
		if (minDamage == maxDamage) {
			return "+" + minDamage + " Cold Damage (Slowed for " + (duration/25.0) + " seconds)";
		} else {
			return "+ " + minDamage + "-" + maxDamage + " Cold Damage (Slowed for " + (duration/25.0) + " seconds)";
		}
		
	}
}

