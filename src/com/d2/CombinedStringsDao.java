package com.d2;

import java.io.File;

class CombinedStringsDao {
	public static CombinedStringsDao INSTANCE;
	
	private StringDB normalStrings;
	private StringDB patchStrings;
	private StringDB expansionStrings;
	
	public CombinedStringsDao() {
		normalStrings = new StringDB(
				new File("StringAddressTable.bin"),
				new File("StringIndexTable.bin"),
				new File("Strings.bin"));
		patchStrings = new StringDB(
				new File("patchStringsAddressTable.bin"),
				new File("patchStringsIndexTable.bin"),
				new File("patchStrings.bin"));
		expansionStrings = new StringDB(
				new File("expansionStringAddressTable.bin"),
				new File("expansionStringIndexTable.bin"),
				new File("expansionStrings.bin"));
	}
	
	public String getString(int id) {
		String result;
		if (id < 10000) {
			result = normalStrings.getString(id);
		} else if (id < 20000) {
			result = patchStrings.getString(id-10000);
		} else {
			result = expansionStrings.getString(id-20000);
		}
		
		if (result == null) {
			System.out.println("Error : Cannot find string with id " + id);
			return "UNKNOWN_STRING_" + id;
		}
		
		return result;

	}
	
	public synchronized static CombinedStringsDao get() {
		if (INSTANCE == null) {
			INSTANCE = new CombinedStringsDao();
		}
		return INSTANCE;
	}
}