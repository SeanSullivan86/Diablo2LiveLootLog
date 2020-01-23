package d2;
import java.util.ArrayList;

import d2.items.instance.D2ItemInstance;
import d2.items.type.D2Item;
import d2.treasureclass.TreasureClassElement;
import d2.treasureclass.TreasureClasses;

public class ItemGenerationContext {
	private ArrayList<String> log;
	public ArrayList<Double> rands = new ArrayList<Double>();
	public boolean isReplay;
	private int replayStep;
	public D2ItemInstance item;
	
	public String treasureClass;
	
	public String getLog() {
		rerunWithLogging();
		StringBuilder builder = new StringBuilder("");
		for (String x : log) {
			builder.append(x);
			builder.append("\n");
		}
		return builder.toString();
	}
	
	private void rerunWithLogging() {
		this.isReplay = true;
		this.replayStep = 0;
		this.log = new ArrayList<String>();
		doSinglePick();
	}
	
	public void doSinglePick() {
		TreasureClassElement itemType = TreasureClasses.getItem(treasureClass, this);
		
		int ilvl = 90;
		int qfUnique = 0;
		int qfSet = 0;
		int qfRare = 0;
		int qfMagic = 0;
		int mf = 300;
		if (itemType instanceof D2Item) {
			D2ItemInstance item = new D2ItemInstance(this, (D2Item)itemType, 
					ilvl, 
					qfUnique,
					qfSet,  
					qfRare, 
					qfMagic, 
					mf);
			this.item = item;
		}
	}
	
	public ItemGenerationContext(String treasureClass) {
		this.rands = new ArrayList<Double>();
		this.treasureClass = treasureClass;
		this.isReplay = false;
	}
		
	public void log(String x) {
		if (isReplay) {
		    log.add(x);
		}
	}
	
	public double rand() {
		if (isReplay) {
		   double rand = rands.get(replayStep);
		   replayStep++;
		   return rand;
		}
		
		double rand = Math.random();
		rands.add(rand);
		return rand;
	}
}