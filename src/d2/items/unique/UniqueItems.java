package d2.items.unique;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import d2.CSVReader;
import d2.items.instance.D2ItemInstance;
import d2.items.typetype.D2ItemType;

public class UniqueItems {
	
	public static Hashtable<String, List<UniqueItem>> uniques;
	
	public static List<UniqueItem> getUniquesWithLevelCap(String itemCode, int maxLevel) {
	    List<UniqueItem> found = new ArrayList<UniqueItem>();
	    if (uniques.containsKey(itemCode)) {
	    	for (UniqueItem item : uniques.get(itemCode)) {
	    		if (item.qlvl <= maxLevel) {
	    			found.add(item);
	    		}
	    	}
	    }
	    return found;
	}
	
	public static void init() {
		uniques = new Hashtable<String, List<UniqueItem>>();
		List<String[]> rows = CSVReader.getRows("UniqueItems.txt");
		for (int i = 1 ; i < rows.size(); i++) {
			UniqueItem item = new UniqueItem(rows.get(i));
			if (item.isEnabled) {
				if (!uniques.containsKey(item.itemType)) {
					uniques.put(item.itemType, new ArrayList<UniqueItem>());
				}
				uniques.get(item.itemType).add(item);
			}
		}
	}
	
	


}
