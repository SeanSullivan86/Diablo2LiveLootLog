package d2.filters;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import d2.Diablo2Items;
import d2.NetworkUtils;
import d2.items.instance.D2ItemInstance;

public class D2ItemSearch {
	private final D2ItemFilter filter;
	private final D2ItemMetric metric;
	
	private Hashtable<Integer,Integer> freqs = new Hashtable<Integer,Integer>();
	
	private ArrayList<D2ItemInstance> items;
	private Comparator<D2ItemInstance> comparator;
	
	private int attempts;
	private int currentMin;
	private int sizeCap;
	
	public int searchId = 1;
	public int user = 1;
	
    public ArrayList<D2ItemInstance> selectItemsToSend() {
    	ArrayList<D2ItemInstance> toSend = new ArrayList<D2ItemInstance>();
    	ArrayList<Integer> existingItems = NetworkUtils.getExistingItems(searchId, user);
		int k = 0;
    	for (int i = items.size()-1; i >= 0; i--) {
    		int metric = this.metric.getMetric(items.get(i));
    		int atLeastAsBigAsMe = k;
    		for (int existing : existingItems) {
    			if (existing >= metric) { atLeastAsBigAsMe++; }
    		}
    		
    		if (atLeastAsBigAsMe >= 10) {
    			break;
    		}
    		
    		toSend.add(items.get(i));
    		k++;
    	}
    	return toSend;
	}
		
	public void sendBatch(long compTime) {
		
		ArrayList<D2ItemInstance> toSend = selectItemsToSend();
		System.out.println("Sending " + toSend.size() + " items to server.");
		StringBuilder batch = new StringBuilder("");
		batch.append(compTime + "#" + attempts + "#");
		int i = 0;
		for (int k : freqs.keySet()) {
			if (i > 0) {
				batch.append("|");
			}
			i++;
			batch.append(k);
			batch.append(",");
			batch.append(freqs.get(k));
		}
		
		StringBuilder itemString = new StringBuilder("");
		i = 0;
		for (D2ItemInstance item : toSend) {
			if (i > 0) { itemString.append("#"); }
			i++;
			itemString.append(item.getNetworkString());
			itemString.append("|");
			itemString.append(this.metric.getMetric(item));
		}
		
		String ids = searchId+"|"+Diablo2Items.VERSION+"|"+user;

		try {
			// Construct data
			String data = URLEncoder.encode("vals", "UTF-8") + "="
					+ URLEncoder.encode(batch.toString(), "UTF-8") + "&" +
					URLEncoder.encode("items", "UTF-8") + "=" +
					URLEncoder.encode(itemString.toString(), "UTF-8") + "&" +
					URLEncoder.encode("ids", "UTF-8") + "=" +
					URLEncoder.encode(ids, "UTF-8");
			// Send data
			URL url = new URL("http://www.seansullivan86.com/d2/postdata.php");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(data);
			wr.flush();
	
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				System.out.println("Server : " +line);
			}
			wr.close();
			rd.close();
		} catch (IOException e) {
			System.out.println("Exception while contacting server : " + e.getMessage());
			e.printStackTrace();
		}

	}
	
	public D2ItemSearch(D2ItemFilter filter, D2ItemMetric metric, int sizeCap) {
		this.filter = filter;
		this.metric = metric;
		this.items = new ArrayList<D2ItemInstance>();
		this.comparator = getComparator();
		this.currentMin = 0;
		this.attempts = 0;
		this.freqs = new Hashtable<Integer,Integer>();
		this.sizeCap = sizeCap;
	}
	
	private Comparator<D2ItemInstance> getComparator() {
		return new Comparator<D2ItemInstance>() {			
			@Override
			public int compare(D2ItemInstance a, D2ItemInstance b) {
				return metric.getMetric(a)-metric.getMetric(b);
			} };
	}
	
	public void tryItem(D2ItemInstance item) {
		attempts++;
		if (item == null) {
			return;
		}
		if (filter.isAcceptable(item)) {
			int val = this.metric.getMetric(item);
						
			if (freqs.containsKey(val)) {
				freqs.put(val, freqs.get(val)+1);
			} else {
				freqs.put(val, 1);
			}
			
			if (this.items.size() < sizeCap) {
				this.items.add(item);
				//System.out.println("Added Item : " + item.getDescription());
			} else {
				if (val <= currentMin) {
					return;
				}
				this.items.remove(0);
				this.items.add(item);
				Collections.sort(this.items,comparator);
				//System.out.println("Added Item with metric : " + val);
				//System.out.println(item.getDescription());
				this.currentMin = metric.getMetric(this.items.get(0));
				//System.out.println("New Minimum Val : " + this.currentMin);
			}
		}
	}
	
	
}