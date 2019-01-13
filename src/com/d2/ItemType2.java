package com.d2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ItemType2 {
	private int id;
	private String code;
	private short nameStringId;
	
	ItemType2(int id, byte[] bytes) {
		this.id = id;
		ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
		this.code = "" +  ((char) buffer.get(0x80)) + ((char) buffer.get(0x81)) + ((char) buffer.get(0x82));
		
		this.nameStringId = buffer.getShort(0xF4);
		//System.out.println(id + " : " + code + " : " + nameStringId);
	}

	public int getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public short getNameStringId() {
		return nameStringId;
	}
	
	
	
}