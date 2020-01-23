package d2;

public class ItemRatios {
	public int[] baseChance; // unique, set, rare, magic, hq, normal
	public int[] divisor ; // unique, set, rare, magic, hq, normal
	public int[] minChance; // unique, set, rare, magic
	
	public ItemRatios(int[] baseChance, int[] divisor, int[] minChance) {
		this.baseChance = baseChance;
		this.divisor = divisor;
		this.minChance = minChance;
	}
	
	public static final ItemRatios normal = new ItemRatios(
			new int[] { 400, 160, 100, 34, 12, 2 },
			new int[] { 1 , 2 , 2 , 3 , 8 , 2},
			new int[] { 6400, 5600, 3200, 192 });
	public static final ItemRatios uber = new ItemRatios(
			new int[] { 400, 160, 100, 34, 12, 1 },
			new int[] { 1 , 2 , 2 , 3 , 8 , 1},
			new int[] { 6400, 5600, 3200, 192 });
	public static final ItemRatios classSpecific = new ItemRatios(
			new int[] { 240, 120, 80, 17, 9, 2},
			new int[] { 3, 3, 3, 6, 8 , 2},
			new int[] { 6400, 5600, 3200, 192 });
	public static final ItemRatios uberClassSpecific = new ItemRatios(
			new int[] { 240, 120, 80, 17, 9, 1},
			new int[] { 3, 3, 3, 6, 8 , 1},
			new int[] { 6400, 5600, 3200, 192 });
	
	public static ItemRatios getRatio(boolean isUber, boolean isClassSpecific) {
		if (!isUber && !isClassSpecific) {
			return normal;
		}
		if (isUber && !isClassSpecific) {
			return uber;
		}
		if (isClassSpecific && !isUber) {
			return classSpecific;
		}
		return uberClassSpecific;
	}
}