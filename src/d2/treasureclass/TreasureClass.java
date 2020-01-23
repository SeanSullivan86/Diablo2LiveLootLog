package d2.treasureclass;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import d2.CSVReader;
import d2.items.type.D2Misc;
import d2.items.type.D2Miscs;

public class TreasureClass {
	
	public static final String NO_DROP = "NO_DROP";
	public String name;
	public int group;
	public int level;
	public int picks;
	public int unique;
	public int set;
	public int rare;
	public int magic;
	public int noDrop;
	public List<TreasureClassElement> elements;
	public int probSum;
	
	public void sort() {
		Collections.sort(elements, new Comparator<TreasureClassElement>() {
			@Override
			public int compare(TreasureClassElement a,
					TreasureClassElement b) {
				if (a.rarity != b.rarity) {
					return b.rarity - a.rarity;
				}
				return a.name.compareTo(b.name);
			} });
		probSum = 0;
		for (TreasureClassElement element : elements) {
			probSum += element.rarity;
		}
	}
	
	@SuppressWarnings("unchecked")
	public TreasureClass(String name, List<? extends TreasureClassElement> elements) {
		this.name = name;
		this.elements = (List<TreasureClassElement>) elements;
		sort();
	}
		
	public TreasureClass(String[] row) {
		name = row[0];
		group = CSVReader.readInt(row[1], 0);
		level = CSVReader.readInt(row[2], 0);
		picks = CSVReader.readInt(row[3], 0);
		unique = CSVReader.readInt(row[4], 0);
		set = CSVReader.readInt(row[5], 0);
		rare = CSVReader.readInt(row[6], 0);
		magic = CSVReader.readInt(row[7] , 0);
		noDrop = CSVReader.readInt(row[8], 0);
		elements = new ArrayList<TreasureClassElement>();
		for ( int i = 0 ; i < 10; i++) {
			String item = row[9+2*i];
			int prob = CSVReader.readInt(row[10+2*i],0);
			if (item != null && !item.isEmpty()) {
				D2Misc misc = D2Miscs.getMisc(item);
				if (misc != null) {
					misc.rarity = prob;
					elements.add(misc);
				} else {
				    elements.add(new TreasureClassElement(item,prob));
				}
			}
		}
		if (noDrop > 0) {
			elements.add(new TreasureClassElement(NO_DROP,noDrop));
		}
		sort();
	}
}