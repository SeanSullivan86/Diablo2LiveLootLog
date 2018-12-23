package com.d2.properties;
class PoisonDamage extends CompositePropertyValue {
	
	private int minDamage;
	private int maxDamage;
	private int duration;
	
	public PoisonDamage(int minDamage, int maxDamage, int duration) {
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
		this.duration = duration;
	}

	public String getDescription() {
		if (minDamage == maxDamage) {
			return "+" + (int)(minDamage*1.0/256*duration) + " Poison Damage over " + (duration/25.0) + " seconds";
		} else {
			return "+" + (int)(minDamage*1.0/256*duration) + "-" + (int)(maxDamage*1.0/256*duration) + " Poison Damage over " + (duration/25.0) + " seconds";
		}
		
	}
}