package d2.items.type;

import d2.CSVReader;
import d2.items.typetype.D2ItemTypes;


public class D2Weapon extends D2Item {
	public int minDamage;
	public int maxDamage;
	
	public D2Weapon(String[] row) {
		this.name = CSVReader.readString(row[0]);
		String typeStr = CSVReader.readString(row[1]);
		type = D2ItemTypes.getType(typeStr);
		if (type != null) {
			this.rarity = type.rarity;
		} else {
			this.rarity = 0;
		}
		code = CSVReader.readString(row[3]);
		minDamage = CSVReader.readInt(row[10], 0);
		maxDamage = CSVReader.readInt(row[11], 0);
		if (minDamage == 0 && maxDamage == 0) {
			minDamage = CSVReader.readInt(row[14],0);
			maxDamage = CSVReader.readInt(row[15],0);
		}
		this.indestructable = CSVReader.readInt(row[26], 0) == 1;
		qlvl = CSVReader.readInt(row[27], 0);
		this.normalCode = CSVReader.readString(row[34]);
		this.exceptionalCode = CSVReader.readString(row[35]);
		this.eliteCode = CSVReader.readString(row[36]);
		this.isUber = (exceptionalCode != null && code.equals(exceptionalCode)) ||
				(eliteCode != null && code.equals(eliteCode));
		this.magic_lvl = CSVReader.readInt(row[31],0);
		this.spawnable = CSVReader.readInt(row[9],0) == 1;
		this.maxSockets = CSVReader.readInt(row[52],0);
	}
	
	
}