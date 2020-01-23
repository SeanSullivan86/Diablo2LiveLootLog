package d2.items.instance;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import d2.ItemGenerationContext;
import d2.ItemRatios;
import d2.affixes.AffixType;
import d2.affixes.AffixTypes;
import d2.affixes.ItemModifier;
import d2.affixes.ItemModifierInstance;
import d2.items.type.D2Armor;
import d2.items.type.D2Item;
import d2.items.type.D2Weapon;
import d2.items.type.D2Weapons;
import d2.items.typetype.D2ItemType;
import d2.items.typetype.D2ItemTypes;
import d2.items.unique.UniqueItem;
import d2.items.unique.UniqueItems;


public class D2ItemInstance {
	public ItemGenerationContext context;
	public D2Item itemType;
	public String quality;
	
	public List<ItemModifierInstance> modifiers;
	public List<AffixType> affixes;
	public int baseDefense;
	public int ilvl;
	public int alvl;
	public int sockets;
	public boolean ethereal;
	public boolean isFailedUnique;
	public boolean isFailedSet;
	public String uniqueName;
	
	public String getName() {
		String name = itemType.name;
		if (quality.equals("Magic")) {
			for (AffixType affix : affixes) {
				if (affix.isPrefix) {
					name = affix.name + " " + name;
				} else {
					name = name + " " + affix.name;
				}
			}
		} else if (quality.equals("Rare")) {
			name = "Rare " + name;
		} else if (quality.equals("Unique")) {
			name = this.uniqueName;
		}
		return name;
	}
	
	public boolean hasAttribute (String att) {
		for (ItemModifierInstance mod : modifiers) {
			if (mod.name.equals(att)) {
				return true;
			}
		}
		return false;
	}
	
	public int getAttribute(String att) {
		int attVal = 0;
		for (ItemModifierInstance mod : modifiers) {
			if (mod.name.equals(att)) {
				attVal += mod.val;
			}
		}
		return attVal;
	}
	
	public int getDefense() {
		if (!(itemType instanceof D2Armor)) {
			return 0;
		}
		
		int defPct = 0;
		int defAdd = 0;
		
		for (ItemModifierInstance mod : modifiers) {
			if (mod.name.equals("ac%")) {
				defPct += mod.val;
			}
			if (mod.name.equals("ac")) {
				defAdd += mod.val;
			}
		}
		return (int) (baseDefense * (1 + defPct/100.0) + defAdd + 0.5);
	}
	
	public double getAverageDamage() {
		int[] dam = getDamage();
		return (dam[0]+dam[1])/2.0;
	}
	
	public double getAverageEliteJeweledDamage() {
		int[] dam = getEliteDamageWithJewels();
		return (dam[0]+dam[1])/2.0;
	}
	
	public int[] getEliteDamageWithJewels() {
		int[] result = new int[2];
		
		D2Weapon eliteType = (D2Weapon) itemType;
		if (itemType.canBeUpgradedToElite()) {
		    eliteType = D2Weapons.getWeapon(((D2Weapon)itemType).eliteCode);
		}
		
		double ethBonus = this.ethereal ? 1.5 : 1.0;

		result[0] = (int) (eliteType.minDamage * ethBonus + .5);
		result[1] = (int) (eliteType.maxDamage * ethBonus + .5);
		
		int dmgP = 0;
		int dmgMin = 0;
		int dmgMax = 0;
		for (ItemModifierInstance mod : modifiers) {
			if (mod.name.equals("dmg%")) {
				dmgP += mod.val;
			}
			if (mod.name.equals("dmg-min")) {
				dmgMin += mod.val;
			}
			if (mod.name.equals("dmg-max")) {
				dmgMax += mod.val;
			}
		}
		
		int sockets = this.getAttribute("sock"); // sockets from Magic Prefix
		if (sockets == 0) {
			if (this.itemType.maxSockets > 0) {
			    sockets = 1; // can add a socket via quest
			}
		}
		if (this.ethereal && !this.itemType.indestructable && !this.hasAttribute("rep-dur")) {
			sockets--; // one socket used for Zod
		}
		
		dmgP += sockets*40;
		
		result[0] = (int) (result[0]*(1 + dmgP/100.0) + dmgMin + 0.5);
		result[1] = (int) (result[1]*(1 + dmgP/100.0) + dmgMax + 0.5);
		return result;
	}
	
	public int[] getDamage() {
		int[] result = new int[2];
		
		if (!(itemType instanceof D2Weapon)) {
			return result;
		}
		
		double ethBonus = this.ethereal ? 1.5 : 1.0;

		result[0] = (int) (((D2Weapon)itemType).minDamage * ethBonus + .5);
		result[1] = (int) (((D2Weapon)itemType).maxDamage * ethBonus + .5);
		int dmgP = 0;
		int dmgMin = 0;
		int dmgMax = 0;
		for (ItemModifierInstance mod : modifiers) {
			if (mod.name.equals("dmg%")) {
				dmgP += mod.val;
			}
			if (mod.name.equals("dmg-min")) {
				dmgMin += mod.val;
			}
			if (mod.name.equals("dmg-max")) {
				dmgMax += mod.val;
			}
		}
		result[0] = (int) (result[0]*(1 + dmgP/100.0) + dmgMin + 0.5);
		result[1] = (int) (result[1]*(1 + dmgP/100.0) + dmgMax + 0.5);
		return result;
	}
	
	public String getNetworkString() {
		StringBuilder builder = new StringBuilder("");
		builder.append(this.getName() +"|");
		builder.append(this.ilvl+"|");
		builder.append(this.baseDefense+"|");
		builder.append((this.ethereal?1:0)+"|");
		builder.append(this.itemType.name +"|");
		builder.append(this.quality+"|");
		if (this.itemType instanceof D2Weapon) {
			builder.append(((D2Weapon) this.itemType).minDamage + "|");
			builder.append(((D2Weapon) this.itemType).maxDamage + "|");
		} else {
			builder.append("0|0|");
		}
		int i = 0;
		for (ItemModifierInstance mod : this.modifiers) {
			if (i > 0) { builder.append(","); }
			i++;
			builder.append(mod.name+","+mod.param+","+mod.val);
		}
		builder.append("|");
		builder.append(this.context.getLog());
		return builder.toString();
	}
	
	public String getDescription() {
		StringBuilder builder = new StringBuilder("");
		builder.append(getGenericItemDescription());
		if (itemType instanceof D2Weapon) {
			builder.append(getWeaponDescription());
		} else if (itemType instanceof D2Armor) {
			builder.append(getArmorDescription());
		}
		builder.append(getModifierDescription());
		if (this.ethereal) { builder.append(" Ethereal \n"); }
		return builder.toString();
	}
	
	public String getWeaponDescription() {
		D2Weapon weap = (D2Weapon) itemType;
		StringBuilder builder = new StringBuilder("");
		if (weap.minDamage > 0) {
		    int[] realDmg = getDamage();
			builder.append("Base Damage : " + weap.minDamage + "-" + weap.maxDamage + "\n");
		    builder.append("Damage : " + realDmg[0] + "-" + realDmg[1] + "\n");
		    int[] eliteDmg = this.getEliteDamageWithJewels();
		    builder.append("Elite Damage With Jewels : " + eliteDmg[0] + "-" + eliteDmg[1] +"\n");
		}
		return builder.toString();
	}
	
	public String getArmorDescription() {
		StringBuilder builder = new StringBuilder("");
		builder.append("Base Defense : " + this.baseDefense + "\n");
		builder.append("Defense : " + getDefense() + "\n");
		return builder.toString();
	}
	
	public String getGenericItemDescription() {
		StringBuilder builder = new StringBuilder("");
		builder.append("Quality : " + quality + "\n");
		builder.append("ilvl : " + ilvl + "\n");
		builder.append("alvl : " + alvl + "\n");
		builder.append("qlvl : " + this.itemType.qlvl + "\n");
		if (itemType.canBeUpgradedToElite()) {
			if (itemType instanceof D2Weapon) {
			    builder.append("Elite Version : " + D2Weapons.getWeapon(itemType.eliteCode).name +"\n");
			}
		}
		return builder.toString();
	}
	
	public String getModifierDescription() {
		StringBuilder builder = new StringBuilder("");
		for (ItemModifierInstance modifier : modifiers) {
			builder.append(modifier.getDescription() + "\n");
		}
		return builder.toString();
	}
	
	public void setALvl() {
		int qlvl = itemType.qlvl;
		int tempIlvl = ilvl;
		int magic_lvl = itemType.magic_lvl;
		
		if (tempIlvl > 99) {
			tempIlvl = 99;
		}
		if (qlvl > tempIlvl) {
			tempIlvl = qlvl;
		}
		if (magic_lvl > 0) {
			alvl = tempIlvl + magic_lvl;
		} else {
			if (tempIlvl < (99 - qlvl / 2)) {
				alvl = tempIlvl - qlvl / 2;
			} else {
				alvl = 2 * tempIlvl - 99;
			}
		}
		if (alvl > 99) {
			alvl = 99;
		}
		context.log("AffixLevel (alvl) = " + alvl + " (From ilvl=" + ilvl + " , qlvl=" +qlvl + " )");
	}
	
	public AffixType selectAffixFromList(AffixType[] available) {
		return available[(int) (available.length*context.rand())];
	}
	
	
	/*
	public AffixType selectAffixFromList(List<AffixType> available) {
		int totalFrequency = 0;
		for (AffixType type : available) { totalFrequency += type.frequency; }
		int partialSum = 0;
		int roll = (int) (context.rand()*totalFrequency);
		for (AffixType type : available) {
			partialSum += type.frequency;
			if (roll < partialSum) {
				return type;
			}
		}
		throw new RuntimeException("Unreachable code in selectAffixFromList");
	}*/
	
	public void assignModifiersFromAffixes() {
		for (AffixType type : affixes) {
			for (ItemModifier modifier : type.modifiers) {
				ItemModifierInstance newMod = modifier.getInstance(context);
				boolean alreadyHasMod = false;
				
				if (newMod.name.equals("sock")) {
					this.sockets = newMod.val;
					if (this.sockets > this.itemType.maxSockets) {
						this.sockets = this.itemType.maxSockets;
					}
				}
				
				if (ItemModifier.isStackable(newMod.name)) {
				    for (ItemModifierInstance mod : this.modifiers) {
					    if (newMod.name.equals(mod.name)) {
					    	mod.val += newMod.val;
					    	alreadyHasMod = true;
					    }
				    }
				}
				
				if (!alreadyHasMod) {
			        this.modifiers.add(newMod);
				}
			}
		}
	}
	
	public static String getAffixListString(AffixType[] types) {
		StringBuilder builder = new StringBuilder("");
		builder.append("Affix Choices : ");
		int totalProb = 0;
		for (int i = 0; i < types.length; i++) {
			if (i == 0 || types[i] != types[i-1]) {
				builder.append(types[i].name + " ["+types[i].frequency+"] ,");
				totalProb += types[i].frequency;
			}
		}
		builder.append(" (Frequency Sum = " + totalProb + ")");
		return builder.toString();
	}
	
	public void assignMagicAffixes() {
		setALvl();

		double rand = context.rand();
		
		if (rand < .75) {
			AffixType[] prefixes = AffixTypes.getAvailableAffixes(alvl, itemType.type.code, true, true, false);
			context.log("Rolled : This item will have a prefix");
			if (context.isReplay) context.log(getAffixListString(prefixes));
			AffixType type = prefixes[(int) (prefixes.length*context.rand())];
			context.log("Rolled : Prefix = " + type.name);
			this.affixes.add(type);
		}
		if (rand > .25) {
			AffixType[] suffixes = AffixTypes.getAvailableAffixes(alvl, itemType.type.code, true, false, true);
			context.log("Rolled : This item will have a suffix");
			if (context.isReplay) context.log(getAffixListString(suffixes));
			AffixType type = suffixes[(int) (suffixes.length*context.rand())];
			context.log("Rolled : Suffix = " + type.name);
			this.affixes.add(type);
		}
		
		assignModifiersFromAffixes();
	}
	
	public void assignRareAffixes() {
		setALvl();
		int numAffixes = ((int) (context.rand()*4))+3;
		int numPrefixes = 0;
		int numSuffixes = 0;
		Set<Integer> groupsAlreadySelected = new HashSet<Integer>();
		AffixType[] available = AffixTypes.getAvailableAffixes(alvl, itemType.type.code, false, true, true);
		
		context.log("Rolled : " + numAffixes + " Rare Affixes (from 3 to 6)");
		context.log("Available Rare Affixes : ");
		if (context.isReplay) context.log(getAffixListString(available));
		
		while (numPrefixes + numSuffixes < numAffixes) {
			AffixType choice = available[(int) (available.length*context.rand())];
			context.log("Rolled Affix = " + choice.name);
			if (numPrefixes == 3 && choice.isPrefix) {
				context.log("  Rejecting because there are already 3 prefixes");
				continue;
			}
			if (numSuffixes == 3 && !choice.isPrefix) {
				context.log("  Rejecting because there are already 3 suffixes");
				continue;				
			}
			if (groupsAlreadySelected.contains(choice.group)) {
				context.log("  Rejecting because there is already an affix from the same group");
				continue;
			}
			
			if (choice.isPrefix) numPrefixes++; else numSuffixes++;
			groupsAlreadySelected.add(choice.group);
			this.affixes.add(choice);
		}
		assignModifiersFromAffixes();
	}
	
	public D2ItemInstance(ItemGenerationContext context, D2Item itemType, int ilvl, int qfUnique,
			int qfSet, int qfRare, int qfMagic, int mf) {
		this.context = context;
		if (context.isReplay) context.log("Creating new item instance : ilvl="+ilvl + 
				" , qfUnique="+qfUnique +
				" , qfRare="+qfRare +
				" , qfMagic="+qfMagic +
				" , magicFind="+mf);
		
		ItemRatios ratios = ItemRatios.getRatio(itemType.isUber, itemType.type.isClassSpecific);
		
		this.affixes = new ArrayList<AffixType>();
		this.modifiers = new ArrayList<ItemModifierInstance>();
		this.ilvl = ilvl;
		this.itemType = itemType;
		
		
		int chance, effectiveMF, finalChance;
		double rand;
		double p;
		// unique check
		if (itemType.type.canBeRare) {
			chance = (ratios.baseChance[0] - ((ilvl-itemType.qlvl)/ratios.divisor[0])) * 128;
			effectiveMF = mf*250/(mf+250);
			chance =  chance*100/(100+ effectiveMF);
			if (chance < ratios.minChance[0]) {
				chance = ratios.minChance[0];
			}
			finalChance=chance - (chance*qfUnique/1024);
			rand = context.rand()*finalChance;
			p = 128.0/finalChance;
			if (rand < 128) {
				quality = "Unique";
				if (context.isReplay) context.log("Unique roll succeeded (p = " + p + ")");
			} else {
				if (context.isReplay) context.log("Unique roll failed (p = " + p + ")");
			}
		}
		
		if (quality == null && itemType.type.canBeRare) {
			// Set check
			chance = (ratios.baseChance[1] - ((ilvl-itemType.qlvl)/ratios.divisor[1])) * 128;
			effectiveMF = mf*500/(mf+500);
			chance =  chance*100/(100+ effectiveMF);
			if (chance < ratios.minChance[1]) {
				chance = ratios.minChance[1];
			}
			finalChance=chance - (chance*qfSet/1024);
			rand = context.rand()*finalChance;
			p = 128.0/finalChance;
			if (rand < 128) {
				quality = "Set";
				if (context.isReplay) context.log("Set roll succeeded (p = " + p + ")");
			} else {
				if (context.isReplay) context.log("Set roll failed (p = " + p + ")");
			}
		}
		
		if (quality == null && itemType.type.canBeRare) {
			// Rare check
			chance = (ratios.baseChance[2] - ((ilvl-itemType.qlvl)/ratios.divisor[2])) * 128;
			effectiveMF = mf*600/(mf+600);
			chance =  chance*100/(100+ effectiveMF);
			if (chance < ratios.minChance[2]) {
				chance = ratios.minChance[2];
			}
			finalChance=chance - (chance*qfRare/1024);
			rand = context.rand()*finalChance;
			p = 128.0/finalChance;
			if (rand < 128) {
				quality = "Rare";
				if (context.isReplay) context.log("Rare roll succeeded (p = " + p + ")");
			} else {
				if (context.isReplay) context.log("Rare roll failed (p = " + p + ")");
			}			
		}
		
		if (quality == null && itemType.type.canBeMagic) {
			// Magic Check
			chance = (ratios.baseChance[3] - ((ilvl-itemType.qlvl)/ratios.divisor[3])) * 128;
			effectiveMF = mf;
			chance =  chance*100/(100+ effectiveMF);
			if (chance < ratios.minChance[3]) {
				chance = ratios.minChance[3];
			}
			finalChance=chance - (chance*qfMagic/1024);
			rand = context.rand()*finalChance;
			p = 128.0/finalChance;
			if (rand < 128) {
				quality = "Magic";
				if (context.isReplay) context.log("Magic roll succeeded (p = " + p + ")");
			} else {
				if (context.isReplay) context.log("Magic roll failed (p = " + p + ")");
			}				
		}
		
		if (quality == null ) {
			quality = "Normal";
			// TODO : Can really be high quality, normal, or low quality
		}
		
		context.log("Rolled Quality = " + quality);
		
		if (quality.equals("Unique")) {
			List<UniqueItem> uniques = UniqueItems.getUniquesWithLevelCap(this.itemType.code, this.ilvl);
			
			if (uniques.isEmpty()) {
				context.log("No unique items found for this type. Reverting to Rare.");
				this.isFailedUnique = true;
				this.quality = "Rare";
			} else {
				UniqueItem chosenUnique = null;
				if (uniques.size() == 1) {
					chosenUnique = uniques.get(0);
				} else {
					int raritySum = 0;
					if (context.isReplay) {
						StringBuilder str = new StringBuilder("Choosing unique from list : ");
						for (UniqueItem unique : uniques) {
							str.append(unique.name + "(" + unique.rarity + "), ");
						}
						context.log(str.toString());
					}
					List<UniqueItem> multiList = new ArrayList<UniqueItem>();
					for (UniqueItem unique : uniques) {
						for (int i = 0; i < unique.rarity; i++) {
							multiList.add(unique);
						}
					}
					chosenUnique = multiList.get((int) ( multiList.size() * context.rand() ));
				}
				context.log("Chose Unique Item : " + chosenUnique.name);
				this.uniqueName = chosenUnique.name;
				
				for (ItemModifier modifier : chosenUnique.modifiers) {
					this.modifiers.add(modifier.getInstance(context));
				}
			}
		}
		
		if (quality.equals("Magic")) {
			assignMagicAffixes();
		} else if (quality.equals("Rare")) {
			assignRareAffixes();
		} else if (quality.equals("Unique")) {

		}
		
		if (itemType instanceof D2Armor) {
			this.baseDefense = ((D2Armor) itemType).getRandomBaseDefense(context);
		}
		
		// Check for ethereal
		if (!quality.equals("Set")) {
			if (itemType instanceof D2Armor && !itemType.indestructable) {
				if (context.rand() < 0.05) {
					context.log("Rolled Ethereal.");
					this.ethereal = true;
					this.baseDefense = (int) (1.5 * this.baseDefense + 0.5);
				}
			}
			if (itemType instanceof D2Weapon && !itemType.indestructable) {
				if (context.rand() < 0.05) {
					context.log("Rolled Ethereal.");
					this.ethereal = true;
				}
			}
		}
		
	}
}