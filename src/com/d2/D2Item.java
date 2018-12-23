package com.d2;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.d2.properties.CompositeProperty;
import com.d2.properties.CompositePropertyValue;
import com.d2.properties.ItemProperty;
import com.d2.properties.ItemPropertyDao;
import com.d2.properties.ItemPropertyValue;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.ListMultimap;

public class D2Item {
	
	private ItemType itemType;
	private PropertyValues propertyValues;
	private ItemQuality quality;
	
	private int minDamage1h;
	private int maxDamage1h;
	private int minDamage2h;
	private int maxDamage2h;
	private int minDamageThrow;
	private int maxDamageThrow;
	private int durability;
	private int maxDurability;
	private boolean hasDurability;
	private boolean has1hDamage;
	private boolean has2hDamage;
	private boolean hasThrowDamage;
	private List<Affix> affixes;
	
	public String getName() {
		if (quality == ItemQuality.RARE ||
				quality == ItemQuality.LOW_QUALITY || 
				quality == ItemQuality.SUPERIOR ||
				quality == ItemQuality.SET ||
				quality == ItemQuality.UNIQUE ||
				quality == ItemQuality.CRAFTED) {
			return quality.name() + " " + itemType.getName();
		}
		if (quality == ItemQuality.NORMAL) {
			return itemType.getName();
		}
		
		if (quality == ItemQuality.MAGIC) {
			String prefix = null;
			String suffix = null;
			for (Affix affix : affixes) {
				if (affix.isSuffix()) suffix = affix.getName(); else prefix = affix.getName();
			}
			return ((prefix == null) ? "" : (prefix + " ")) +
					itemType.getName() + 
				   ((suffix == null) ? "" : (" " + suffix));
		}
		
		throw new RuntimeException();
	}
	
	public D2Item(byte[] input) {
		ByteBuffer buffer = ByteBuffer.wrap(input).order(ByteOrder.LITTLE_ENDIAN);

		int unitDataIndex = 0;
		
		int txtFileNumber = buffer.getInt(4);
		itemType = ItemTypeDao.get().getItemTypeById(txtFileNumber);
		
		ListMultimap<ItemProperty,ItemPropertyValue> propertyValuesBuilder = ArrayListMultimap.create();
		ItemPropertyDao propertyDao = ItemPropertyDao.get();

		
		int itemDataIndex = 0x60;
		int statsDataIndex = 0x60 + 0x88;
		int numStats1Index = statsDataIndex + 0x50;
		int statArray1Index = numStats1Index + 2;
		int numStats1 = buffer.getShort(numStats1Index);
		int numStats2Index = statArray1Index + 8*numStats1;
		int statArray2Index = numStats2Index + 2;
		int numStats2 = buffer.getShort(numStats2Index);
		this.quality = ItemQuality.forId(buffer.getInt(itemDataIndex + 0x00));
		
		this.affixes = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			int affixId = buffer.getShort(itemDataIndex + 0x38 + (i * 0x02));
			if (affixId > 0) {
				affixes.add(AffixDao.get().getById(affixId-1));
			}
		}
		
		for (int i = 0; i < numStats2; i++) {
			int offset = statArray2Index + 8*i;
			int a = buffer.getShort(offset);
			int b = buffer.getShort(offset+2);
			int c = buffer.getInt(offset+4);
			if (propertyDao.getById(b) == null) {
				System.out.println("Cannot find property : " + b);
			} else {
			    ItemProperty prop = propertyDao.getById(b);
				propertyValuesBuilder.put(prop, new ItemPropertyValue(a,c, prop));
				
				/*
				switch(b) {
				case 21:
					minDamage1h = c; has1hDamage=true; break;
				case 22:
					maxDamage1h = c; has1hDamage=true; break;
				case 23:
					minDamage2h = c; has2hDamage=true; break;
				case 24:
					maxDamage2h = c; has2hDamage=true; break;
				case 72:
					durability = c; hasDurability=true; break;
				case 73:
					maxDurability = c; hasDurability=true; break;
				case 159:
					minDamageThrow = c; hasThrowDamage=true; break;
				case 160:
					maxDamageThrow = c; hasThrowDamage=true; break;
				}
				*/
			}
			
		}
		//System.out.println(propertyValues);
		
		propertyValues = new PropertyValues(propertyValuesBuilder);
	}
	
	
	
	static Set<Integer> specialProperties = new HashSet<Integer>(Arrays.<Integer>asList((new Integer[] { 21, 22, 23, 24, 72, 73, 159, 160})));


	
	public String getDisplayString() {
		StringBuilder x = new StringBuilder();
		if (itemType instanceof MiscItemType) {
			if (propertyValues.hasProperty(14)) {
				x.append(propertyValues.getOnlyValue(14).getValue() + " ");
			}
			if (propertyValues.hasProperty(70)) {
				x.append(propertyValues.getOnlyValue(70).getValue() + " ");
			}

			x.append(itemType.getName());
		} else {
			List<String> words = new ArrayList<>();
			x.append("[" + quality.name() + "] ");
			if (quality == ItemQuality.MAGIC || quality == ItemQuality.RARE) {
				for (Affix affix : Iterables.filter(affixes, affix -> (!affix.isSuffix()))) {
					words.add(affix.getName());
				}
			}
			
			words.add(itemType.getName());
			
			if (quality == ItemQuality.MAGIC || quality == ItemQuality.RARE) {
				for (Affix affix : Iterables.filter(affixes, affix -> (affix.isSuffix()))) {
					words.add(affix.getName());
				}
			}
			
			x.append(String.join(" ", words));
			
			x.append(" [");
			
			/*
			if (has1hDamage) {
				x.append(minDamage1h + "-" + maxDamage1h + " Damage(1h), ");
			}
			if (has2hDamage) {
				x.append(minDamage2h + "-" + maxDamage2h + " Damage(2h), ");
			}
			if (hasThrowDamage) {
				x.append(minDamageThrow + "-" + maxDamageThrow + " Throw Damage, ");
			}
			*/
			
			for (CompositeProperty property : propertyValues.getCompositeProperties().keySet()) {
				CompositePropertyValue value = propertyValues.getCompositeProperties().get(property);
				x.append(value.getDescription());
				x.append(", ");
			}
			
			
			int i = 0;
			for (ItemProperty property : propertyValues.getProperties().keySet()) {
				if (! specialProperties.contains(property.getId())) {
					List<ItemPropertyValue> values = propertyValues.get(property);
					for (ItemPropertyValue value : values) {
						if (i > 0) x.append(", ");
						x.append(property.getDescription(value));
						i++;
					}
					
				}
			}
			if (hasDurability) {
				x.append(", " + durability + "/" + maxDurability + " Durability");
			}
			x.append("]");
			
		}
		return x.toString();
	}



	public ItemType getItemType() {
		return itemType;
	}



	public PropertyValues getPropertyValues() {
		return propertyValues;
	}



	public ItemQuality getQuality() {
		return quality;
	}



	public int getMinDamage1h() {
		return minDamage1h;
	}



	public int getMaxDamage1h() {
		return maxDamage1h;
	}



	public int getMinDamage2h() {
		return minDamage2h;
	}



	public int getMaxDamage2h() {
		return maxDamage2h;
	}



	public int getMinDamageThrow() {
		return minDamageThrow;
	}



	public int getMaxDamageThrow() {
		return maxDamageThrow;
	}



	public int getDurability() {
		return durability;
	}



	public int getMaxDurability() {
		return maxDurability;
	}



	public boolean isHasDurability() {
		return hasDurability;
	}



	public boolean isHas1hDamage() {
		return has1hDamage;
	}



	public boolean isHas2hDamage() {
		return has2hDamage;
	}



	public boolean isHasThrowDamage() {
		return hasThrowDamage;
	}



	public List<Affix> getAffixes() {
		return affixes;
	}



	public static Set<Integer> getSpecialProperties() {
		return specialProperties;
	}
	
	
}