package d2.items.type;

import d2.CSVReader;
import d2.ItemGenerationContext;
import d2.items.typetype.D2ItemTypes;

public class D2Armor extends D2Item {
	public int minArmor;
	public int maxArmor;
	
	public D2Armor(String[] row) {
		this.name = CSVReader.readString(row[0]);
		this.code = CSVReader.readString(row[17]);
		String typeStr = CSVReader.readString(row[48]);
		type = D2ItemTypes.getType(typeStr);
		if (type != null) {
			this.rarity = type.rarity;
		} else {
			this.rarity = 0;
		}
		this.minArmor = CSVReader.readInt(row[5], 0);
		this.maxArmor = CSVReader.readInt(row[6], 0);
		this.qlvl = CSVReader.readInt(row[13],0);
		this.indestructable = CSVReader.readInt(row[12], 0) == 1;
		this.normalCode = CSVReader.readString(row[23]);
		this.exceptionalCode = CSVReader.readString(row[24]);
		this.eliteCode = CSVReader.readString(row[25]);
		
		this.magic_lvl = CSVReader.readInt(row[19],0);
		this.isUber = (exceptionalCode != null && code.equals(exceptionalCode)) ||
				(eliteCode != null && code.equals(eliteCode));
		this.spawnable = CSVReader.readInt(row[4],0) == 1;
		this.maxSockets = CSVReader.readInt(row[31],0);
	}
	
	public int getRandomBaseDefense(ItemGenerationContext context) {
		int def = minArmor + ((int) (context.rand()*(maxArmor-minArmor+1)));
		context.log("Rolled Defense = " + def + " (from range "+minArmor +" to " + maxArmor +")");
		return def;
	}
}