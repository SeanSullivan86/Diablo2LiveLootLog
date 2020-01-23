package d2.affixes;
import java.util.ArrayList;
import java.util.List;

import d2.CSVReader;


public class AffixType {
	public String name;
	public boolean magicOnly;
	public int spawnable;
	public int minALvl;
	public int maxALvl;
	public int frequency;
	public int group;
	public List<ItemModifier> modifiers;
	public List<String> eligibleTypes;
	public List<String> ineligibleTypes;
	public boolean isPrefix;
	public int id;
	
	public static int nextId = 0;
	
	public AffixType(String[] row, boolean isPrefix) {
		nextId++;
		this.id = nextId;
		this.isPrefix = isPrefix;
		this.name = CSVReader.readString(row[0]);
		this.spawnable = CSVReader.readInt(row[2], 0);
		this.magicOnly = CSVReader.readInt(row[3], 0) == 0;
		this.minALvl = CSVReader.readInt(row[4], 0);
		this.maxALvl = CSVReader.readInt(row[5], 999);
		this.frequency = CSVReader.readInt(row[10],0);
		this.group = CSVReader.readInt(row[11],0);
		this.modifiers = new ArrayList<ItemModifier>();
		if (CSVReader.readString(row[12]) != null) {
			this.modifiers.add(new ItemModifier(CSVReader.readString(row[12]), 
					CSVReader.readString(row[13]),
					CSVReader.readInt(row[14],0),
					CSVReader.readInt(row[15],0)));
		}
		if (CSVReader.readString(row[16]) != null) {
			this.modifiers.add(new ItemModifier(CSVReader.readString(row[16]), 
					CSVReader.readString(row[17]),
					CSVReader.readInt(row[18],0),
					CSVReader.readInt(row[19],0)));
		}
		if (CSVReader.readString(row[20]) != null) {
			this.modifiers.add(new ItemModifier(CSVReader.readString(row[20]), 
					CSVReader.readString(row[21]),
					CSVReader.readInt(row[22],0),
					CSVReader.readInt(row[23],0)));
		}
		this.eligibleTypes = new ArrayList<String>();
		for (int i = 26; i <= 32; i++) {
			if (CSVReader.readString(row[i]) != null) {
				this.eligibleTypes.add(CSVReader.readString(row[i]));
			}
		}
		this.ineligibleTypes = new ArrayList<String>();
		for (int i = 33; i <= 37; i++) {
			if (CSVReader.readString(row[i]) != null) {
				this.ineligibleTypes.add(CSVReader.readString(row[i]));
			}
		}		
		
	}
}