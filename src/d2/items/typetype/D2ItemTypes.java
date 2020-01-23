package d2.items.typetype;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import d2.CSVReader;


public class D2ItemTypes {
	public static Hashtable<String, D2ItemType> itemTypes;
	
	public static D2ItemType getType(String typeCode) {
		if (typeCode == null) {
			return null;
		}
		return itemTypes.get(typeCode);
	}
	
	public static void init() {
		itemTypes = new Hashtable<String, D2ItemType>();
		List<String[]> rows = CSVReader.getRows("ItemTypes.txt");
		for (int i = 1 ; i < rows.size(); i++) {
			addType(new D2ItemType(rows.get(i)));
		}
		generateEquivalentTypes();
		
		//List<String> allCodes = new ArrayList();
		//allCodes.addAll(itemTypes.keySet());
		//Collections.sort(allCodes);
		//System.out.println(allCodes.toString());
	
	}
		
	private static Set<String> getEquivalentTypes(String typeCode) {
		D2ItemType itemType = itemTypes.get(typeCode);
		HashSet<String> equivs = new HashSet<String>();
		
		equivs.add(itemType.code);
		if (itemType.equiv1 != null) {
			equivs.add(itemType.equiv1);
			equivs.addAll(getEquivalentTypes(itemType.equiv1));
		}
		if (itemType.equiv2 != null) {
			equivs.add(itemType.equiv2);
			equivs.addAll(getEquivalentTypes(itemType.equiv2));
		}
		return equivs;
	}
	
	private static void generateEquivalentTypes() {
		for (String code : itemTypes.keySet()) {
			D2ItemType itemType = itemTypes.get(code);
			itemType.superCodes = getEquivalentTypes(code);
		}
		for (String code : itemTypes.keySet()) {
			D2ItemType itemType = itemTypes.get(code);
			if (itemType.superCodes.contains("clas")) {
				itemType.isClassSpecific = true;
			}
		}
	}
	
	private static void addType(D2ItemType type) {
		if (type != null && type.name != null && type.code != null &&
				!type.name.isEmpty() &&
				!type.code.isEmpty()) {
			itemTypes.put(type.code,type);
		}
	}
}