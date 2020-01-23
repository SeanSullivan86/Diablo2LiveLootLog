package d2.filters;

import d2.items.instance.D2ItemInstance;

public class EliteWeaponDamage implements D2ItemMetric{

	@Override
	public int getMetric(D2ItemInstance item) {
		int[] dam = item.getEliteDamageWithJewels();
		return dam[0] + dam[1];
	}

}
