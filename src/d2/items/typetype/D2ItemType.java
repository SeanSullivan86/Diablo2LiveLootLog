package d2.items.typetype;

import java.util.Set;

import d2.CSVReader;


public class D2ItemType {
	public String name;
	public String code;
	public String equiv1;
	public String equiv2;
	public int rarity;
	public boolean canBeMagic;
	public boolean canBeRare;
	public boolean isClassSpecific;
	
	public Set<String> superCodes;
	
	public D2ItemType(String[] row) {
		this.name = CSVReader.readString(row[0]);
		this.code = CSVReader.readString(row[1]);
		this.equiv1 = CSVReader.readString(row[2]);
		this.equiv2 = CSVReader.readString(row[3]);
		this.rarity = CSVReader.readInt(row[24],0);
		int magic = CSVReader.readInt(row[14],0);
		int rare = CSVReader.readInt(row[15],0);
		this.canBeRare = rare == 1;
		this.canBeMagic = canBeRare || (magic == 1);
	}
}