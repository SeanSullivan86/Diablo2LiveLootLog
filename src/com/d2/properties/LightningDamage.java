package com.d2.properties;
class LightningDamage extends CompositePropertyValue {
	private int minDamage;
	private int maxDamage;
	
	
	public LightningDamage(int minDamage, int maxDamage) {
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
	}
	
	public String getDescription() {
		if (minDamage == maxDamage) {
			return "+" + minDamage + " Lightning Damage";
		} else {
			return "+ " + minDamage + "-" + maxDamage + " Lightning Damage";
		}
	}
}