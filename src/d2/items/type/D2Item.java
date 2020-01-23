package d2.items.type;

import d2.items.typetype.D2ItemType;
import d2.treasureclass.TreasureClassElement;

public class D2Item extends TreasureClassElement {
	public String code;
	public D2ItemType type;
	public boolean indestructable;
	
	public String normalCode;
	public String exceptionalCode;
	public String eliteCode;
	public boolean spawnable;
	public boolean isUber;
	public int maxSockets;
	
	public int magic_lvl;
	
	public D2Item() { }

	public int qlvl;
	
	public boolean canBeUpgradedToElite() {
		return (eliteCode != null && !eliteCode.equals(code));
	}
}