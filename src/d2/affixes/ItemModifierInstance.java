package d2.affixes;
import java.util.Hashtable;


public class ItemModifierInstance {
	public String name;
	public String param;
	public int val;
	
	public ItemModifierInstance(String name, String param, int val) {
		this.name = name;
		this.param = param;
		this.val = val;
	}
	
	private static Hashtable<String,String> modifierStrings;
	public static String getModifierString(String name) {
		if (modifierStrings == null) {
			modifierStrings = new Hashtable<String,String>();
			modifierStrings.put("dmg%", "+[]% Enhanced Damage");
			modifierStrings.put("dmg-min", "+[] to Minimum Damage");
			modifierStrings.put("dmg-max", "+[] to Maximum Damage");
			modifierStrings.put("dmg", "+[] Damage");
			
			modifierStrings.put("att", "+[] to Attack Rating");
			
			modifierStrings.put("str", "+[] to Strength");
			modifierStrings.put("dex", "+[] to Dexterity");
			modifierStrings.put("vit", "+[] to Vitality");
			modifierStrings.put("enr", "+[] to Energy");
			
			modifierStrings.put("manasteal", "+[]% to Mana Steal");
			modifierStrings.put("lifesteal", "+[]% to Life Steal");
			
			modifierStrings.put("swing1", "+[]% Increased Attack Speed");
			modifierStrings.put("swing2", "+[]% Increased Attack Speed");
			modifierStrings.put("swing3", "+[]% Increased Attack Speed");
			modifierStrings.put("move1", "+[]% Faster Walk/Run Speed");
			modifierStrings.put("move2", "+[]% Faster Walk/Run Speed");
			modifierStrings.put("move3", "+[]% Faster Walk/Run Speed");
			modifierStrings.put("balance1", "+[]% Faster Hit Recovery");
			modifierStrings.put("balance2", "+[]% Faster Hit Recovery");
			modifierStrings.put("balance3", "+[]% Faster Hit Recovery");
			modifierStrings.put("block1", "+[]% Faster Block Rate");
			modifierStrings.put("block2", "+[]% Faster Block Rate");
			modifierStrings.put("block3", "+[]% Faster Block Rate");
			modifierStrings.put("cast1", "+[]% Faster Cast Rate");
			modifierStrings.put("cast2", "+[]% Faster Cast Rate");
			modifierStrings.put("cast3", "+[]% Faster Cast Rate");
			
			modifierStrings.put("cold-min", "+[] to Minimum Cold Damage");
			modifierStrings.put("cold-max", "+[] to Maximum Cold Damage");
			modifierStrings.put("fire-min", "+[] to Minimum Fire Damage");
			modifierStrings.put("fire-max", "+[] to Maximum Fire Damage");
			modifierStrings.put("ltng-min", "+[] to Minimum Lightning Damage");
			modifierStrings.put("ltng-max", "+[] to Maximum Lightning Damage");
			
			modifierStrings.put("res-pois-len", "Poison Length Reduced by []%");
			
			modifierStrings.put("res-ltng", "+[]% Lightning Resistance");
			modifierStrings.put("res-fire", "+[]% Fire Resistance");
			modifierStrings.put("res-cold", "+[]% Cold Resistance");
			modifierStrings.put("res-pois", "+[]% Poison Resistance");
			modifierStrings.put("res-all", "+[]% to All Resistances");
			
			modifierStrings.put("ama", "+[] to Amazon Skill Levels");
			modifierStrings.put("pal", "+[] to Paladin Skill Levels");
			modifierStrings.put("nec", "+[] to Necromancer Skill Levels");
			modifierStrings.put("sor", "+[] to Sorceress Skill Levels");
			modifierStrings.put("bar", "+[] to Barbarian Skill Levels");
			modifierStrings.put("dru", "+[] to Druid Skill Levels");
			modifierStrings.put("ass", "+[] to Assassin Skill Levels");
			
			modifierStrings.put("knock", "Knockback Target");
			
			modifierStrings.put("hp", "+[] to Life");
			modifierStrings.put("mana", "+[] to Mana");
			
			modifierStrings.put("ac", "+[] Defense");
			modifierStrings.put("ac-miss", "+[] Defense vs Missiles");
			modifierStrings.put("ac-hth", "+[] Defense vs Melee");
			modifierStrings.put("ac%", "+[]% Enhanced Defense");
			modifierStrings.put("red-dmg", "Damage Reduced by []");
			modifierStrings.put("red-dmg%", "Damage Reduced by []%");
			modifierStrings.put("red-mag", "Magic Damage Reduced by []");
			
			modifierStrings.put("block", "+[]% chance to block");
		}
		if (modifierStrings.containsKey(name)) {
			return modifierStrings.get(name);
		}
		return null;
	}
	
	public String getDescription() {
		String modStr = getModifierString(name);
		if (modStr == null) {
		    return name + "{" + param + "}" + " : " + val;
		}
		return modStr.replace("[]", ""+val);
	}
}