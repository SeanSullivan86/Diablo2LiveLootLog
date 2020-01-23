package d2.items.type;

import java.util.Hashtable;

public class D2Armors {
	public static Hashtable<String, D2Armor> armors;

	public static void addArmor(D2Armor armor) {
		if (armors == null) {
			armors = new Hashtable<String, D2Armor>();
		}
		armors.put(armor.code, armor);
	}

	public static D2Armor getArmor(String code) {
		return armors.get(code);
	}
}
