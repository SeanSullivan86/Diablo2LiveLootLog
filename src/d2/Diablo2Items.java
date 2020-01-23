package d2;
import java.util.Arrays;
import java.util.Hashtable;

import d2.affixes.AffixTypes;
import d2.filters.D2ItemSearch;
import d2.filters.EliteWeaponDamage;
import d2.filters.IsQualityAndType;
import d2.filters.IsRareWeapon;
import d2.filters.TotalResist;
import d2.items.instance.D2ItemInstance;
import d2.items.type.D2Weapon;
import d2.items.typetype.D2ItemTypes;
import d2.items.unique.UniqueItems;
import d2.treasureclass.TreasureClasses;

public class Diablo2Items {
	
	public static final String VERSION = "0.1";
	
	public static void main(String[] args) {
		D2ItemTypes.init();
		AffixTypes.init();
		TreasureClasses.init();
		UniqueItems.init();
		
		ItemGenerationContext context = null;
		
		while (true) {
			context = new ItemGenerationContext("Act 5 (H) H2H C");
			context.doSinglePick();
			if (context.item != null && context.item.quality.equals("Unique")) {
				break;
			}
		}
		 
		System.out.println(context.getLog());
		if (context.item != null) {
		    System.out.println(context.item.getName());
		    System.out.println(context.item.getDescription());
		}
		
		//doSearch();
		/*
		for (int i = 0; i < 1000; i++) {
			ItemGenerationContext context = new ItemGenerationContext("Act 5 (H) H2H C");
			context.doSinglePick();
			
			D2ItemInstance item = context.item;
			if (item != null && !item.quality.equals("Normal")) {
				System.out.println(item.quality);
			}
		}*/
	}
		
	public static void doSearch() {	
		/* D2ItemSearch search = new D2ItemSearch(new IsQualityAndType("Rare","boot"),
				new TotalResist(), 10); */

		long iterations = 10000000;
		long maxIterations = 1000000000;
		while(true) {
			long batchStartTime = System.currentTimeMillis();
			System.out.println("Starting batch of " + iterations + " item generation attempts.");
			D2ItemSearch search = new D2ItemSearch(new IsRareWeapon(),
					new EliteWeaponDamage(), 10);
			for (int i = 0; i < iterations; i++) {
				ItemGenerationContext context = new ItemGenerationContext("Act 5 (H) H2H C");
				context.doSinglePick();
				
				D2ItemInstance item = context.item;
				search.tryItem(item);
			}
			long batchEndTime = System.currentTimeMillis();
			long compTime = batchEndTime - batchStartTime;
			System.out.println("Batch Complete: Computation Time = " + (compTime/1000.0) + " seconds.");
			
			search.sendBatch(compTime);
			
			iterations *= 2;
			if (iterations > maxIterations) {
				iterations = maxIterations;
			}
		}
		
	}
		
	public static void doLongTest(int millions) {
		long startTime = System.currentTimeMillis();
		
		D2ItemTypes.init();
		AffixTypes.init();
		TreasureClasses.init();
		
		System.out.println("Init time : " + (System.currentTimeMillis() - startTime));
		
		Hashtable<Integer,Integer> normalDamages = new Hashtable<Integer,Integer>();
		Hashtable<Integer,Integer> ethRepDamages = new Hashtable<Integer,Integer>();
		Hashtable<Integer,Integer> ethNonRepDamages = new Hashtable<Integer,Integer>();
		double currentMaxNormal = 0.0;
		double currentMaxEthRep = 0.0;
		double currentMaxEthNonRep = 0.0;
						
		for (int i = 0 ; i < millions; i++) {
			if (i % 1000 == 0) {
				System.out.println(i);
			}
	      for (int j = 0; j < 1000000; j++) {

			ItemGenerationContext context = new ItemGenerationContext("Act 5 (H) H2H C");
			context.doSinglePick();
						
			D2ItemInstance item = context.item;
			
			
			if (item != null) {
				
				//if (item.itemType instanceof D2Armor && item.getDefense() > 1400 && item.getAttribute("hp") >= 80) {
				//    System.out.println(item.getName());
				//    System.out.println(item.getDescription());			
			    //}
				
				if (item.quality.equals("Rare")) {
					
					if (item.itemType instanceof D2Weapon ) {
						
						if (item.ethereal && item.hasAttribute("rep-dur")) {
							int damageSum = (int) (item.getAverageEliteJeweledDamage()*2+.5);						
							
							if (!ethRepDamages.containsKey(damageSum)) {
								ethRepDamages.put(damageSum, 0);
							}
							ethRepDamages.put(damageSum, ethRepDamages.get(damageSum)+1);
							
							if (item.getAverageEliteJeweledDamage() > currentMaxEthRep) {
								currentMaxEthRep = item.getAverageEliteJeweledDamage();
								System.out.println("New Best Eth Rep Item");
							    System.out.println(item.getName());
							    System.out.println(item.getDescription());
							    System.out.println(context.getLog());
							}
						} else if (item.ethereal) {
							int damageSum = (int) (item.getAverageEliteJeweledDamage()*2+.5);						
							
							if (!ethNonRepDamages.containsKey(damageSum)) {
								ethNonRepDamages.put(damageSum, 0);
							}
							ethNonRepDamages.put(damageSum, ethNonRepDamages.get(damageSum)+1);
							
							if (item.getAverageEliteJeweledDamage() > currentMaxEthNonRep) {
								currentMaxEthNonRep = item.getAverageEliteJeweledDamage();
								System.out.println("New Best Eth NonRep Item");
							    System.out.println(item.getName());
							    System.out.println(item.getDescription());
							    System.out.println(context.getLog());
							}
						} else {
							int damageSum = (int) (item.getAverageEliteJeweledDamage()*2+.5);						
							
							if (!normalDamages.containsKey(damageSum)) {
								normalDamages.put(damageSum, 0);
							}
							normalDamages.put(damageSum, normalDamages.get(damageSum)+1);
							
							if (item.getAverageEliteJeweledDamage() > currentMaxNormal) {
								currentMaxNormal = item.getAverageEliteJeweledDamage();
								System.out.println("New Best Normal Item");
							    System.out.println(item.getName());
							    System.out.println(item.getDescription());
							    System.out.println(context.getLog());
							}
						}
					}
				}
			}
				
	      }
		}
		
		System.out.println("Printing normal distribution");
		Integer[] damageKeys = normalDamages.keySet().toArray(new Integer[]{});
		Arrays.sort(damageKeys);
		for (int i : damageKeys) {
			System.out.println(i + ":" + normalDamages.get(i));
		}
		
		System.out.println("Printing ethereal repairing distribution");
		damageKeys = ethRepDamages.keySet().toArray(new Integer[]{});
		Arrays.sort(damageKeys);
		for (int i : damageKeys) {
			System.out.println(i + ":" + ethRepDamages.get(i));
		}
		
		System.out.println("Printing ethereal non-repairing distribution");
		damageKeys = ethNonRepDamages.keySet().toArray(new Integer[]{});
		Arrays.sort(damageKeys);
		for (int i : damageKeys) {
			System.out.println(i + ":" + ethNonRepDamages.get(i));
		}
		
		System.out.println("End time : " + (System.currentTimeMillis() - startTime));
	}
}
