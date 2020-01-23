package d2.affixes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import d2.CSVReader;
import d2.items.typetype.D2ItemTypes;


class AffixCriteria {
	private int intPart;
	private String stringPart;
	public AffixCriteria(int alvl, String itemType, boolean isMagic, boolean includePrefixes, boolean includeSuffixes) {
		this.intPart = alvl*1000+(isMagic ? 4 : 0) + (includePrefixes ? 2 : 0) + (includeSuffixes ? 1 : 0);
		this.stringPart = itemType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + intPart;
		result = prime * result
				+ ((stringPart == null) ? 0 : stringPart.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		AffixCriteria other = (AffixCriteria) obj;
		return intPart == other.intPart && stringPart.equals(other.stringPart);
	}
	
	
}


public class AffixTypes {
	private static List<AffixType> prefixes;
	private static List<AffixType> suffixes;
	
	public static Hashtable<AffixCriteria, AffixType[]> weightedAffixes;

	private static AffixType[] constructWeightedAffixList(List<AffixType> types) {
		int sum = 0;
		for (AffixType type : types) {
			sum += type.frequency;
		}
		AffixType[] result = new AffixType[sum];
		
		int i = 0;
		for (AffixType type : types) {
			for (int j = 0; j < type.frequency; j++) {
				result[i] = type;
				i++;
			}
		}
		return result;
	}
	
	public static AffixType[] getUnweightedAvailableAffixes(int alvl, String itemType, boolean isMagic, boolean includePrefixes, boolean includeSuffixes) {
		AffixCriteria criteria = new AffixCriteria(alvl, itemType, isMagic, includePrefixes, includeSuffixes);
		
		if (weightedAffixes.containsKey(criteria)) {
			return weightedAffixes.get(criteria);
		}
		
		List<AffixType> results = new ArrayList<AffixType>();
		if (includePrefixes) {
		    results.addAll(getAvailableAffixes(alvl,itemType, isMagic, prefixes));
		}
		if (includeSuffixes) {
			results.addAll(getAvailableAffixes(alvl, itemType, isMagic, suffixes));
		}
		
		AffixType[] ret = new AffixType[results.size()];
		for (int i = 0 ; i < results.size(); i++) {
			ret[i] = results.get(i);
		}
		return ret;
	}
	
	public static AffixType[] getAvailableAffixes(int alvl, String itemType, boolean isMagic, boolean includePrefixes, boolean includeSuffixes) {
		AffixCriteria criteria = new AffixCriteria(alvl, itemType, isMagic, includePrefixes, includeSuffixes);
		
		if (weightedAffixes.containsKey(criteria)) {
			return weightedAffixes.get(criteria);
		}
		
		List<AffixType> results = new ArrayList<AffixType>();
		if (includePrefixes) {
		    results.addAll(getAvailableAffixes(alvl,itemType, isMagic, prefixes));
		}
		if (includeSuffixes) {
			results.addAll(getAvailableAffixes(alvl, itemType, isMagic, suffixes));
		}
		
		AffixType[] weightedList = constructWeightedAffixList(results);
		weightedAffixes.put(criteria, weightedList);
		return weightedList;
	}
	
	private static List<AffixType> getAvailableAffixes(int alvl, String itemType, boolean isMagic, List<AffixType> possibilities) {
		List<AffixType> results = new ArrayList<AffixType>();
		for (AffixType type : possibilities) {
			if (alvl < type.minALvl || alvl > type.maxALvl) {
				continue;				
			}
			if (!isMagic && type.magicOnly == true) {
				continue;
			}
			boolean eligible = false;
			for (String equivItemType : D2ItemTypes.getType(itemType).superCodes) {
				if (type.eligibleTypes.contains(equivItemType)) {
					eligible = true;
					break;
				}
			}
			for (String equivItemType : D2ItemTypes.getType(itemType).superCodes) {
				if (type.ineligibleTypes.contains(equivItemType)) {
					eligible = false;
					break;
				}
			}
			if (eligible) {
				results.add(type);
			}
		}
		return results;
	}
			
	private static void addType(AffixType type) {
		if (type.spawnable == 0 || type.frequency == 0) {
			return;
		}
		if (type.isPrefix) {
			prefixes.add(type);
		} else {
			suffixes.add(type);
		}
	}
	
	public static void init() {
		prefixes = new ArrayList<AffixType>();
		suffixes = new ArrayList<AffixType>();
		
		weightedAffixes = new Hashtable<AffixCriteria,AffixType[]>();
		
		List<String[]> rows = CSVReader.getRows("MagicPrefix.txt");
		for (int i = 1; i < rows.size(); i++) {
			addType(new AffixType(rows.get(i),true));
		}
		rows = CSVReader.getRows("MagicSuffix.txt");
		for (int i = 1; i < rows.size(); i++) {
			addType(new AffixType(rows.get(i),false));
		}
	}
	
}