package com.d2;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Affix {
	private String name;
	private int id;
	private boolean isSuffix;
	
	public Affix(int id, byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		int len = 0;
		String name = ""; // the "name" here isn't the true in-game string.
		for (len = 0; len < 144; len++) {
			if (buffer.get(len) == 0) break;
			name += (char) (buffer.get(len));
		}
		
		int mod1Min = buffer.getInt(0x2C);
		int mod1Max = buffer.getInt(0x30);
		
		this.id = id;
		this.name = name;
		this.isSuffix = name.startsWith("of ");
		
		int stringId = buffer.getShort(0x20);
		name = CombinedStringsDao.get().getString(stringId);
		
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public boolean isSuffix() {
		return isSuffix;
	}
	
	
}