package d2.treasureclass;
public class TreasureClassElement {
	
	// TreasureClassElement can either be a TreasureClass or an item
	
	public TreasureClass tc;
	public String name;
	public int rarity;
	public boolean isTreasureClass;
	
	public TreasureClassElement(TreasureClass tc, int rarity) {
		this.tc = tc;
		this.name = tc.name;
		this.rarity = rarity;
		this.isTreasureClass = true;
	}
	
	public TreasureClassElement() { }
	
	public TreasureClassElement(String name, int rarity) {
		this.name = name;
		this.rarity = rarity;
	}
	
}