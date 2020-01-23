package d2.filters;

import d2.items.instance.D2ItemInstance;
import d2.items.type.D2Weapon;

public class IsRareWeapon implements D2ItemFilter {

	@Override
	public boolean isAcceptable(D2ItemInstance item) {
		return item.itemType instanceof D2Weapon &&
				item.quality.equals("Rare");
	}
	
}