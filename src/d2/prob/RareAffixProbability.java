package d2.prob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import d2.affixes.AffixType;
import d2.affixes.AffixTypes;
import d2.affixes.ItemModifier;
import d2.items.typetype.D2ItemTypes;

class DamageModifiers {
    public int dmgPct;
    public int maxDmg;
    public int minDmg;
    public int sockets;
    public boolean selfRepairing;
}


class IntegerRandomVariable {
	public int minValue;
	public int maxValue;
	
	private double[] probs;
	
	public double getProb(int val) {
		return probs[val-minValue];
	}
	
	public static IntegerRandomVariable alwaysZero() {
		return equallyDistributed(0,0);
	}
	
	public static IntegerRandomVariable equallyDistributed(int min, int max) {
		IntegerRandomVariable x = new IntegerRandomVariable();
		x.minValue = min;
		x.maxValue = max;
		x.probs = new double[max-min+1];
		for (int i = 0; i < x.probs.length; i++) {
			x.probs[i] = 1.0/ (max-min+1);
		}
		return x;
	}
	
	public static IntegerRandomVariable fromWeightedDistribution(Map<IntegerRandomVariable, Double> probs) {
		int trueMin = Integer.MAX_VALUE;
		int trueMax = Integer.MIN_VALUE;
		for (IntegerRandomVariable poss : probs.keySet()) {
			if (poss.minValue < trueMin) trueMin = poss.minValue;
			if (poss.maxValue > trueMax) trueMax = poss.maxValue;
		}
		IntegerRandomVariable x = new IntegerRandomVariable();
		x.minValue = trueMin;
		x.maxValue = trueMax;
		x.probs = new double[trueMax-trueMin+1];
		
		for (IntegerRandomVariable poss : probs.keySet()) {
			for (int val = poss.minValue; val <= poss.maxValue; val++) {
				x.probs[val - trueMin] += poss.probs[val - poss.minValue] * probs.get(poss);
			}
		}
		
		return x;
	}
	
	public IntegerRandomVariable plus(IntegerRandomVariable b) {
		IntegerRandomVariable c = new IntegerRandomVariable();
		c.minValue = this.minValue + b.minValue;
		c.maxValue = this.maxValue + b.maxValue;
		c.probs = new double[c.maxValue - c.minValue + 1];
		
		for (int val1 = minValue; val1 <= maxValue; val1 ++) {
			for (int val2 = b.minValue; val2 <= b.maxValue; val2++) {
				c.probs[val1+val2- c.minValue] += this.probs[val1-this.minValue]*b.probs[val2-b.minValue];
				
			}
		}
		
		return c;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder("");
		double sum = 0.0;
		for (int val = minValue; val <= maxValue; val++) {
			sum += probs[val-minValue];
			builder.append(val + " : " + probs[val-minValue] + " : " + sum + "\n");
		}
		return builder.toString();
	}
}

class AffixGroup {
	public int id;
	public int frequency;
	public List<AffixType> affixes;
	public boolean isPrefix;
	
	public IntegerRandomVariable getModifierDistribution(String modifierCode) {		
		Map<IntegerRandomVariable,Double> probs = new HashMap<IntegerRandomVariable, Double>();
		
		for (AffixType affix : affixes) {
			boolean found = false;
			for (ItemModifier modifier : affix.modifiers) {
				if (modifier.name.equals(modifierCode)) {
				    probs.put(
				      IntegerRandomVariable.equallyDistributed(modifier.min, modifier.max),
				      affix.frequency*1.0/this.frequency);
				    found = true;
				}
			}
			if (!found) {
				probs.put(IntegerRandomVariable.alwaysZero(), affix.frequency*1.0/this.frequency);
			}
		}
		
		return IntegerRandomVariable.fromWeightedDistribution(probs);
	}
}

public class RareAffixProbability {
	
	public static String getAffixListString(List<AffixType> types) {
		AffixType[] typeArray = new AffixType[types.size()];
		int i = 0;
		for (AffixType type : types) {
			typeArray[i] = type;
			i++;
		}
		return getAffixListString(typeArray);
	}
	
	public static String getAffixListString(AffixType[] types) {
		StringBuilder builder = new StringBuilder("");
		builder.append("Affix Choices : ");
		int totalProb = 0;
		for (int i = 0; i < types.length; i++) {
			if (i == 0 || types[i] != types[i-1]) {
				builder.append(types[i].name + " ["+types[i].frequency+"] ,");
				totalProb += types[i].frequency;
			}
		}
		builder.append(" (Frequency Sum = " + totalProb + ")");
		return builder.toString();
	}
	
	public static IntegerRandomVariable getModifierDistribution(
			Map<List<AffixGroup>, Double> affixGroupProbs,
			String modifierCode) {
		
		Map<IntegerRandomVariable, Double> probs = new HashMap<IntegerRandomVariable,Double>();
		
		for (List<AffixGroup> groups : affixGroupProbs.keySet()) {
			IntegerRandomVariable sum = IntegerRandomVariable.alwaysZero();
			double groupProb = affixGroupProbs.get(groups);
			
			for (AffixGroup group : groups) {
				sum = sum.plus(group.getModifierDistribution(modifierCode));
			}
			
			probs.put(sum, groupProb);
		}
		return IntegerRandomVariable.fromWeightedDistribution(probs);
	}
	
	public static void main(String[] args) {
		D2ItemTypes.init();
		AffixTypes.init();
		
		Map<Integer, AffixGroup> affixGroups = getAffixGroups(99, "swor");
		
		Map<List<AffixGroup>, Double> groupProbs = new HashMap<List<AffixGroup>,Double>();
		List<AffixGroup> none = new ArrayList<AffixGroup>();
		List<AffixGroup> cruel = new ArrayList<AffixGroup>();
		cruel.add(affixGroups.get(105));
		List<AffixGroup> masters = new ArrayList<AffixGroup>();
		masters.add(affixGroups.get(111));
		List<AffixGroup> cruelMasters = new ArrayList<AffixGroup>();
		cruelMasters.addAll(cruel);
		cruelMasters.addAll(masters);
		
		groupProbs.put(cruel, 0.2814);
		groupProbs.put(masters, 0.2591);
		groupProbs.put(cruelMasters, 0.3347);
		groupProbs.put(none, 1-0.2814-0.2591-0.3347);
		
		IntegerRandomVariable dmg = getModifierDistribution(groupProbs, "dmg%");
		
		System.out.println( dmg.toString());
	}
	
	public static Map<Integer, AffixGroup> getAffixGroups(int alvl, String code) {	
	    AffixType[] prefixes = AffixTypes.getUnweightedAvailableAffixes(alvl, code, false, true, false);
	    Map<Integer, AffixGroup> affixGroups = new HashMap<Integer, AffixGroup>();
	    int prefixSum = 0;
	    int suffixSum = 0;
	    int totalSum = 0;
	    
	    for (AffixType prefix : prefixes) {
	    	if( !affixGroups.containsKey(prefix.group)) {
	    		affixGroups.put(prefix.group, new AffixGroup());
	    		affixGroups.get(prefix.group).id = prefix.group;
	    		affixGroups.get(prefix.group).isPrefix = true;
	    		affixGroups.get(prefix.group).affixes = new ArrayList<AffixType>();
	    	}
	    	affixGroups.get(prefix.group).frequency += prefix.frequency;
	    	affixGroups.get(prefix.group).affixes.add(prefix);
	    	prefixSum += prefix.frequency;
	    }
	    
	    AffixType[] suffixes = AffixTypes.getAvailableAffixes(alvl, code, false, false, true);
	    
	    for (AffixType suffix : suffixes) {
	    	if( !affixGroups.containsKey(suffix.group)) {
	    		affixGroups.put(suffix.group, new AffixGroup());
	    		affixGroups.get(suffix.group).id = suffix.group;
	    		affixGroups.get(suffix.group).isPrefix = false;
	    		affixGroups.get(suffix.group).affixes = new ArrayList<AffixType>();
	    	}
	    	affixGroups.get(suffix.group).frequency += suffix.frequency;
	    	affixGroups.get(suffix.group).affixes.add(suffix);
	    	suffixSum += suffix.frequency;
	    }
	    return affixGroups;
	}
	
	public static void main2(String[] args) {
		D2ItemTypes.init();
		AffixTypes.init();
		int alvl = 99;
		String code = "swor";

		// 105 cruel, 111 masters, 122 sockets, 14 15 min max damage , 37 repairing
		int[] importantGroups = new int[] { 105, 111, 122, 37};
		
		double[] probs = new double[1 << importantGroups.length];
		double[] approxProbs = new double[1 << importantGroups.length];
		
	    AffixType[] prefixes = AffixTypes.getUnweightedAvailableAffixes(alvl, code, false, true, false);
	    Map<Integer, AffixGroup> affixGroups = new HashMap<Integer, AffixGroup>();
	    int prefixSum = 0;
	    int suffixSum = 0;
	    int totalSum = 0;
	    
	    for (AffixType prefix : prefixes) {
	    	if( !affixGroups.containsKey(prefix.group)) {
	    		affixGroups.put(prefix.group, new AffixGroup());
	    		affixGroups.get(prefix.group).id = prefix.group;
	    		affixGroups.get(prefix.group).isPrefix = true;
	    		affixGroups.get(prefix.group).affixes = new ArrayList<AffixType>();
	    	}
	    	affixGroups.get(prefix.group).frequency += prefix.frequency;
	    	affixGroups.get(prefix.group).affixes.add(prefix);
	    	prefixSum += prefix.frequency;
	    }
	    
	    AffixType[] suffixes = AffixTypes.getAvailableAffixes(alvl, code, false, false, true);
	    
	    for (AffixType suffix : suffixes) {
	    	if( !affixGroups.containsKey(suffix.group)) {
	    		affixGroups.put(suffix.group, new AffixGroup());
	    		affixGroups.get(suffix.group).id = suffix.group;
	    		affixGroups.get(suffix.group).isPrefix = false;
	    		affixGroups.get(suffix.group).affixes = new ArrayList<AffixType>();
	    	}
	    	affixGroups.get(suffix.group).frequency += suffix.frequency;
	    	affixGroups.get(suffix.group).affixes.add(suffix);
	    	suffixSum += suffix.frequency;
	    }
	    totalSum = prefixSum + suffixSum;
	    int n = affixGroups.keySet().size();
	    
	    int targetAffixes = 6;
	    AffixType[] weightedAffixes = AffixTypes.getAvailableAffixes(alvl, code, false, true, true);
	    int iters = 10000000;
	    for (int iter = 0; iter < iters; iter++) {
	    	int chosenPrefixes = 0, chosenSuffixes = 0;
	    	Set<Integer> chosenGroups = new HashSet<Integer>();
	    	while (chosenPrefixes + chosenSuffixes < targetAffixes) {
	    		AffixType newAffix = weightedAffixes[(int) (Math.random()*weightedAffixes.length)];
	    		if (newAffix.isPrefix && chosenPrefixes >= 3) continue;
	    		if (!newAffix.isPrefix && chosenSuffixes >= 3) continue;
	    		if (chosenGroups.contains(newAffix.group)) continue;
	    		if (newAffix.isPrefix) chosenPrefixes++; else chosenSuffixes++;
	    		chosenGroups.add(newAffix.group);
	    	}
	    	
	    	int key = 0;
	    	for (int a = 0; a < importantGroups.length; a++) {
	    		if (chosenGroups.contains(importantGroups[a])) {
	    			key += (1 << a);
	    		}
	    	}
	    	approxProbs[key] += 1;
	    }
	    for (int i = 0; i < approxProbs.length; i++) {
	    	StringBuilder groupStr = new StringBuilder("");
	    	for (int a = 0; a < importantGroups.length; a++) {
	    		if ((i >> a) % 2 == 1) {
	    			groupStr.append(importantGroups[a]+",");
	    		}
	    	}
	    	System.out.println(i + " : " + groupStr.toString() + " : " + approxProbs[i]/iters);
	    }
	    
	    
	    
	    
	    int[] groupIds = new int[n];
	    int i = 0;
	    
	    for (int group : affixGroups.keySet()) {
	    	groupIds[i] = group;
	    	System.out.println(group + " : " + getAffixListString(affixGroups.get(group).affixes));
	    	i++;
	    }
	    
	    long idx = 0;
	    long n2 = n*n;
	    long n3 = n*n*n;
	    long n4 = n*n*n*n;
	    long n5 = n*n*n*n*n;
	    long max = n*n*n*n*n*n;
	    AffixGroup[] g = new AffixGroup[6];
	    int g5,g4,g3,g2,g1,g0;
	    boolean failed = false;
	    int prefixSumLeft, suffixSumLeft;
	    int denom = 1, numerator = 1;
	    int key = 0;
	    double p = 1.0;
	    for (idx = 0; idx < max; idx++) {
	    	if (idx % 100000000 == 0) {System.out.println("..."); }
	    	g5 = (int) (idx % n);
	    	g4 = (int) ((idx / n) % n);
	    	g3 = (int) ((idx / n2) % n);
	    	g2 = (int) ((idx / n3) % n);
	    	g1 = (int) ((idx / n4) % n);
	    	g0 = (int) (idx / n5);
	    	
	    	if (g5 == g4 || g5 == g3 || g5 == g2 || g5 == g1 || g5 == g0 ||
	    	    g4 == g3 || g4 == g2 || g4 == g1 || g4 == g0 ||
	    	    g3 == g2 || g3 == g1 || g3 == g0 ||
	    	    g2 == g1 || g2 == g0 ||
	    	    g1 == g0) {
	    		continue;
	    	}
	    	
	    	key = 0;
	    	for (int a = 0; a < importantGroups.length; a++) {
	    		if (importantGroups[a] == groupIds[g0] ||
	    				importantGroups[a] == groupIds[g1] ||
	    				importantGroups[a] == groupIds[g2] ||
	    				importantGroups[a] == groupIds[g3] ||
	    				importantGroups[a] == groupIds[g4] ||
	    				importantGroups[a] == groupIds[g5]) {
	    			key += (1 << a);
	    		}
	    	}
	    	
	    	if (key == 0) continue;
	    		    	
	    	g[0] = affixGroups.get(groupIds[g0]);
	    	g[1] = affixGroups.get(groupIds[g1]);
	    	g[2] = affixGroups.get(groupIds[g2]);
	    	g[3] = affixGroups.get(groupIds[g3]);
	    	g[4] = affixGroups.get(groupIds[g4]);
	    	g[5] = affixGroups.get(groupIds[g5]);
	    	int prefixesSoFar = 0;
	    	int suffixesSoFar = 0;
	    	prefixSumLeft = prefixSum;
	    	suffixSumLeft = suffixSum;
	    	failed = false;
	    	denom = 1;
	    	numerator = 1;
	    	p = 1.0;
	    	for (int j = 0; j < 6; j++) {
	    		// try to assign jth group to be g[j]
	    		if (g[j].isPrefix) prefixesSoFar++; else suffixesSoFar++;
	    		if (prefixesSoFar > 3 || suffixesSoFar > 3) { failed = true; break; }
	    		if (g[j].isPrefix) {
	    			if (suffixesSoFar == 3) { // forced to pick prefix
	    				denom = prefixSumLeft;
	    			} else {
	    				denom = (prefixSumLeft + suffixSumLeft);
	    			}
	    			prefixSumLeft -= g[j].frequency;
	    		} else {
	    			if (prefixesSoFar == 3) { // forced to pick suffix
	    				denom = suffixSumLeft;
	    			} else {
	    				denom = (prefixSumLeft + suffixSumLeft);
	    			}
	    			suffixSumLeft -= g[j].frequency;
	    		}
	    		numerator = g[j].frequency;
	    		p *= (1.0*numerator/denom);
	    	}
	    	
	    	if (failed == false) {
	    		probs[key] += p;
	    	}
	    }
	    
	    for (i = 0; i < probs.length; i++) {
	    	StringBuilder groupStr = new StringBuilder("");
	    	for (int a = 0; a < importantGroups.length; a++) {
	    		if ((i >> a) % 2 == 1) {
	    			groupStr.append(importantGroups[a]+",");
	    		}
	    	}
	    	System.out.println(i + " : " + groupStr.toString() + " : " + probs[i]);
	    }
	}
}
