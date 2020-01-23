package d2.affixes;
import java.util.Arrays;
import java.util.HashSet;

import d2.ItemGenerationContext;

public class ItemModifier {
	public String name;
	public String param;
	public int min;
	public int max;
	
	public ItemModifier(String name, String param, int min, int max) {
		this.name = name;
		this.param = param;
		this.min = min;
		this.max = max;
	}
	
	public ItemModifierInstance getInstance(ItemGenerationContext context) {
		int val = min + (int) (context.rand()*(max-min+1));
		if (context.isReplay) context.log("Rolled modifier " + name + " = " + val + " (from range "+min+ " to " + max+")");
		return new ItemModifierInstance(name, param, val);
	}
	
	public static HashSet<String> stackableMods = null;
	
	public static boolean isStackable(String name) {
		if (stackableMods == null) {
			String[] mods = new String[] { 
					"dmg%", "dmg-min", "dmg-max", 
					"att", 
					"str", "dex", "vit", "enr",
					"res-ltng", "res-fire", "res-cold", "res-pois", "res-all"
					};
			stackableMods = new HashSet<String>(Arrays.asList(mods));
		}
		return stackableMods.contains(name);
	}
}