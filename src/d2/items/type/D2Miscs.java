package d2.items.type;
import java.util.Hashtable;


public class D2Miscs {
	private static Hashtable<String, D2Misc> miscs;
	
	public static void addMisc(D2Misc misc) {
		if (miscs == null) {
			miscs = new Hashtable<String,D2Misc>();
		}
		miscs.put(misc.code,misc);
	}
	
	public static D2Misc getMisc(String code) {
		D2Misc misc = miscs.get(code);
		if (misc == null) {
			return null;
		}
		return new D2Misc(misc);
	}
	
}