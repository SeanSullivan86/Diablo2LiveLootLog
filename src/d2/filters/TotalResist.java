package d2.filters;

import d2.items.instance.D2ItemInstance;

public class TotalResist implements D2ItemMetric {
	@Override
	public int getMetric(D2ItemInstance item) {
		return 4*item.getAttribute("res-all") + 
				item.getAttribute("res-ltng") + 
		        item.getAttribute("res-fire") +
		        item.getAttribute("res-cold") +
		        item.getAttribute("res-pois");
	}
	
}