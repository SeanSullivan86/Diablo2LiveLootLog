package d2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import d2.items.instance.D2ItemInstance;

public class NetworkUtils {

	public static void main(String[] args) {
		ArrayList<String> res = getResponse("http://www.seansullivan86.com/d2/showTotals.php");
		for (String r: res) {
			System.out.println(r);
		}
	}
	

	
	public static ArrayList<Integer> getExistingItems(int searchId, int user) {
		String url = "http://www.seansullivan86.com/d2/getSearchItems.php?user="+user+"&search_id="+searchId+"&version="+Diablo2Items.VERSION;
		ArrayList<String> str = getResponse(url);
		
		ArrayList<Integer> ints = new ArrayList<Integer>();
		
		if (str == null || str.isEmpty()) {
			return ints;
		}
				
		for (String s : str.get(0).split("\\x7c",-1)) {
			if (s == null || s.isEmpty()) {
				continue;
			}
			try {
			    ints.add(Integer.parseInt(s));
			} catch (NumberFormatException e) {
				System.out.println("Unexpected Number : " + s);
			}
		}
		return ints;
	}
	
	
	public static ArrayList<String> getResponse(String urlString) {
		
		ArrayList<String> result = new ArrayList<String>();
		try {
			URL url = new URL(urlString);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.add(line);
			}
			rd.close();
		} catch (IOException e) {
			return null;
		}
		return result;
		
	}
	
}
