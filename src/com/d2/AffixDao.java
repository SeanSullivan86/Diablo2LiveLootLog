package com.d2;
import java.io.File;
import java.util.Map;

public class AffixDao {
	
	static AffixDao instance;
	
	private Map<Integer,Affix> affixesById; 
	
	public static AffixDao get() {
		if (instance == null) {
			instance = new AffixDao();
		}
		return instance;
	}
	
	private AffixDao() {
		affixesById = AffixDataLoader.loadItemPropertiesFromFile(new File("d2AFfixes.CEM"));
	}
	
	public Affix getById(int id) {
		return affixesById.get(id);
	}
	
	public static void main(String[] args) {
		AffixDao.get();
	}
}