package d2.items.unique;

import java.util.ArrayList;
import java.util.List;

import d2.CSVReader;
import d2.affixes.AffixType;
import d2.affixes.ItemModifier;
import d2.items.type.D2Item;
import d2.items.typetype.D2ItemType;
import d2.items.typetype.D2ItemTypes;

public class UniqueItem {
	
	public String itemType;
	public String quality;
	
	public String name;
	public int rarity;
	public int qlvl;
	
	public boolean isEnabled;
	
	public List<ItemModifier> modifiers;
	
	public UniqueItem(String[] row) {
		this.name = CSVReader.readString(row[0]);
		this.rarity = CSVReader.readInt(row[4],0);
		this.qlvl = CSVReader.readInt(row[6],0);
		this.itemType = CSVReader.readString(row[8]);
		this.isEnabled = CSVReader.readInt(row[2],0) == 1;
		
		this.modifiers = new ArrayList<ItemModifier>();
		
		for (int idx = 1; idx <= 12; idx++) {
			String property = CSVReader.readString(row[17+4*idx]);
			if (property != null) {
				String param = CSVReader.readString(row[18+4*idx]);
				int min = CSVReader.readInt(row[19+4*idx], 0);
				int max = CSVReader.readInt(row[20+4*idx], 0);
				this.modifiers.add(new ItemModifier(property, param, min, max));
			}
		}
	}

}
