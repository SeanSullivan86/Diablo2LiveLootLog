package com.d2.properties;

import java.util.Optional;

public class Damage extends CompositePropertyValue {
	private int min1hDamage;
	private int max1hDamage;
	private int min2hDamage;
	private int max2hDamage;
	private int minThrowDamage;
	private int maxThrowDamage;
	private boolean has1hDamage;
	private boolean has2hDamage;
	private boolean hasThrowDamage;
	
	
	public Damage(
			Optional<ItemPropertyValue> min1hDamage,
			Optional<ItemPropertyValue> max1hDamage,
			Optional<ItemPropertyValue> min2hDamage,
			Optional<ItemPropertyValue> max2hDamage,
			Optional<ItemPropertyValue> minThrowDamage,
			Optional<ItemPropertyValue> maxThrowDamage) {
	    if (min1hDamage.isPresent()) {
	    	this.min1hDamage = min1hDamage.get().getValue();
	    	this.max1hDamage = max1hDamage.get().getValue();
	    	this.has1hDamage = true;
	    }
	    if (min2hDamage.isPresent()) {
	    	this.min2hDamage = min2hDamage.get().getValue();
	    	this.max2hDamage = max2hDamage.get().getValue();
	    	this.has2hDamage = true;
	    }
	    if (minThrowDamage.isPresent()) {
	    	this.minThrowDamage = minThrowDamage.get().getValue();
	    	this.maxThrowDamage = maxThrowDamage.get().getValue();
	    	this.hasThrowDamage = true;
	    }
	}
	
	public String getDescription() {
		StringBuilder x = new StringBuilder();
		if (this.has2hDamage) {
			x.append(min2hDamage + "-" + max2hDamage + " Damage");
		}
		if (this.has1hDamage) {
			if (this.has2hDamage) {
				x.append(" (" + min1hDamage + "-" + max1hDamage + " 1-handed)");
			} else {
				x.append(min1hDamage + "-" + max1hDamage + " Damage");
			}
		}
		if (this.hasThrowDamage) {
			if (this.has2hDamage || this.has1hDamage) {
				x.append(" (" + minThrowDamage + "-" + maxThrowDamage + " Throw Damage)");
			} else {
				x.append(minThrowDamage + "-" + maxThrowDamage + " Throw Damage");
			}
		}
		return x.toString();
	}

	public int getMin1hDamage() {
		return min1hDamage;
	}

	public int getMax1hDamage() {
		return max1hDamage;
	}

	public int getMin2hDamage() {
		return min2hDamage;
	}

	public int getMax2hDamage() {
		return max2hDamage;
	}

	public int getMinThrowDamage() {
		return minThrowDamage;
	}

	public int getMaxThrowDamage() {
		return maxThrowDamage;
	}

	public boolean isHas1hDamage() {
		return has1hDamage;
	}

	public boolean isHas2hDamage() {
		return has2hDamage;
	}

	public boolean isHasThrowDamage() {
		return hasThrowDamage;
	}
	
	
}