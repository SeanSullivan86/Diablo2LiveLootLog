package d2.items.type;

import d2.CSVReader;
import d2.items.typetype.D2ItemTypes;

/** Items that are niether weapon nor armor */
public class D2Misc extends D2Item {
	
	public D2Misc(D2Misc misc) {
		this.name = misc.name;
		this.qlvl = misc.qlvl;
		this.code = misc.code;
		this.type = misc.type;
		this.rarity = misc.rarity;
		this.indestructable = true;
	}
	
	public D2Misc(String[] row) {
		this.name = CSVReader.readString(row[0]);
		this.qlvl = CSVReader.readInt(row[5],0);
		this.code = CSVReader.readString(row[13]);
		String typeStr = CSVReader.readString(row[32]);
		if (typeStr != null) {
			this.type = D2ItemTypes.getType(typeStr);
		    this.rarity = type.rarity;
		}
		this.indestructable = true;
		this.spawnable = true;
	}
}