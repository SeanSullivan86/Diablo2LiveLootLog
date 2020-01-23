package d2.treasureclass;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import d2.CSVReader;
import d2.ItemGenerationContext;
import d2.items.type.D2Armor;
import d2.items.type.D2Armors;
import d2.items.type.D2Misc;
import d2.items.type.D2Miscs;
import d2.items.type.D2Weapon;
import d2.items.type.D2Weapons;

public class TreasureClasses {
	public static Hashtable<String, TreasureClass> classes;
	
	public static void init() {
		classes = new Hashtable<String, TreasureClass>();
		
		List<String[]>rows = CSVReader.getRows("Misc.txt");
		for (int i = 1; i < rows.size(); i++) {
			D2Misc misc = new D2Misc(rows.get(i));
			if (misc.name != null &&  misc.code != null && misc.type != null) {
			    D2Miscs.addMisc(misc);
			}
		}
		
		rows = CSVReader.getRows("TreasureClassEx.txt");
		for (int i = 1 ; i < rows.size(); i++) {
			addClass(new TreasureClass(rows.get(i)));
		}
		
		Hashtable<String,List<D2Weapon>> weapTcs = new Hashtable<String,List<D2Weapon>>();
		rows = CSVReader.getRows("Weapons.txt");
		for (int i = 1; i < rows.size(); i++) {
			D2Weapon weapon = new D2Weapon(rows.get(i));
			if (weapon.code == null || weapon.name == null || !weapon.spawnable) {
				continue;
			}
			D2Weapons.addWeapon(weapon);
			int lvl = 3*((weapon.qlvl-1)/3+1);
			String tcName = "weap" + lvl;
			if (!weapTcs.containsKey(tcName)) {
				weapTcs.put(tcName, new ArrayList<D2Weapon>());
			}
			weapTcs.get(tcName).add(weapon);
		}
		for (String tcName : weapTcs.keySet()) {
			addClass(new TreasureClass(tcName,weapTcs.get(tcName)));
		}

		Hashtable<String,List<D2Armor>> armoTcs = new Hashtable<String, List<D2Armor>>();
		rows = CSVReader.getRows("Armor.txt");
		for (int i = 1; i < rows.size(); i++) {
			D2Armor armor = new D2Armor(rows.get(i));
			if (armor.code == null || armor.name == null || !armor.spawnable) {
				continue;
			}
			D2Armors.addArmor(armor);
			int lvl = 3*((armor.qlvl-1)/3+1);
			String tcName = "armo" + lvl;
			if (!armoTcs.containsKey(tcName)) {
				armoTcs.put(tcName, new ArrayList<D2Armor>());
			}
			armoTcs.get(tcName).add(armor);
		}
		for (String tcName : armoTcs.keySet()) {
			addClass(new TreasureClass(tcName,armoTcs.get(tcName)));
		}
		
		// replace any TC names with TC Objects
		for (String tcName : classes.keySet()) {
			TreasureClass tc = classes.get(tcName);
			List<TreasureClassElement> toAdd = new ArrayList<TreasureClassElement>();
			List<TreasureClassElement> toRemove = new ArrayList<TreasureClassElement>();
			for (TreasureClassElement element : tc.elements) {
				if (classes.containsKey(element.name)) {
					toAdd.add(new TreasureClassElement(classes.get(element.name), element.rarity));
					toRemove.add(element);
				}
			}
			tc.elements.removeAll(toRemove);
			tc.elements.addAll(toAdd);
		}
	}
	
	private static void addClass(TreasureClass newClass) {
		if (newClass.name != null && !newClass.name.isEmpty()) {
		    classes.put(newClass.name,newClass);
		}
	}
	
	public static TreasureClassElement getItem(String tcName, ItemGenerationContext context) {
		TreasureClass tc = classes.get(tcName);
		TreasureClassElement selectedItem = null;
		
		while (tc != null) {
			if (context.isReplay) {
				context.log("Selecting item from treasure class : " + tc.name);
				StringBuilder builder = null;
				builder = new StringBuilder("");
			    builder.append("Choices : ");
				for (TreasureClassElement item : tc.elements) {
					builder.append(item.name + "[" + item.rarity +"], ");
				}
			    context.log(builder.toString());
			    context.log("Total Rarity = " + tc.probSum);
			}
			double rand = context.rand()*tc.probSum;
			int partialSum = 0;
			for (TreasureClassElement item : tc.elements) {
				partialSum += item.rarity;
				if (rand < partialSum) {
					context.log("Rolled: " + item.name);
					selectedItem = item;
					break;
				}
			}
						
			if (selectedItem.isTreasureClass) {
				tc = selectedItem.tc;
			} else {
				tc = null;
			}
		}
		return selectedItem;
	}
}