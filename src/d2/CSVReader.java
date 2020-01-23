package d2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class CSVReader {
	
	public static String readString(String x) {
		if (x == null || x.isEmpty()) {
			return null;
		}
		return x.trim();
	}
	
	public static int readInt(String x, int ifNull) {
		if (x == null || x.isEmpty()) {
			return ifNull;
		}
		return Integer.parseInt(x);
	}
	
	public static void main(String[] args) {
		
		
		
	}
	
	public static List<String[]> getRows(String filename) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
						
			ArrayList<String[]> rows = new ArrayList<String[]>();
			String line = null;
			while ( (line = in.readLine()) != null) {
				rows.add(line.split("\t",-1));
			}
			return rows;
		} catch (IOException e) {
			System.out.println("Problem when reading " + filename);
			throw new RuntimeException(e);
		}
	}
	

}
