package com.d2;

public enum ItemQuality {
	LOW_QUALITY(1),
	NORMAL(2),
	SUPERIOR(3),
	MAGIC(4),
	SET(5),
	RARE(6),
	UNIQUE(7),
	CRAFTED(8);
	
	private final int id;
	
	private ItemQuality(int id) {
		this.id = id;
	}
	
	public static ItemQuality forId(int id) {
		for (ItemQuality quality : values()) {
			if (quality.id == id) {
				return quality;
			}
		}
		throw new RuntimeException("No ItemQuality for id " + id);
	}
}