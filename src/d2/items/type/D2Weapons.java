package d2.items.type;
import java.util.Hashtable;


public class D2Weapons {
	public static Hashtable<String, D2Weapon> weapons;
	
	public static void addWeapon(D2Weapon weapon) {
		if (weapons == null) {
			weapons = new Hashtable<String, D2Weapon>();
		}
		weapons.put(weapon.code, weapon);
	}
	
	public static D2Weapon getWeapon(String code) {
		return weapons.get(code);
	}
}