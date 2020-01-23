package com.d2;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import javax.websocket.Session;

import com.d2.properties.ItemProperty;
import com.d2.properties.ItemPropertyDao;
import com.d2.properties.ItemPropertyValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class D2ItemListener {
	
	public static ObjectMapper jackson = new ObjectMapper();

	public static void main(String[] args) throws IOException {
		
		
		String inputFile = "C:\\Users\\sully\\d2log_new.txt";
		
		D2ItemListener listener = new D2ItemListener(new File(inputFile));
		listener.start();
	}
	
	
	private File inputFile;
	private ItemTypeDao itemTypeDao;
	
	public D2ItemListener(File inputFile) {
		this.inputFile = inputFile;
	}
	
	private BufferedInputStream in;
	private int bytesInSuccessfulItems = 0;
		
	public void start() throws IOException  {	
		
		//TestWebsocketServer.runServer();
		
		in = new BufferedInputStream(new FileInputStream( inputFile));
		
		while(true) {
			
			
			tryRead();
		}

	}
	
	
	private void tryRead() throws IOException {

		System.out.println("Skipping " + bytesInSuccessfulItems + " bytes");
		in.skip(bytesInSuccessfulItems);

		while(true) {
			while(true) {
				byte[] header = new byte[15];
				int bytesRead = in.read(header);
				//System.out.println(bytesRead);
				if (bytesRead < 15) break;
				ByteBuffer buffer = ByteBuffer.wrap(header);
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				int messageType = buffer.get(12);
				int messageLength = buffer.getShort(13);
				//System.out.println(messageType);
				//System.out.println(messageLength);
				int messageBodyLength = messageLength - 15 -10;
				
				byte[] messageBody = new byte[messageBodyLength];
				bytesRead = in.read(messageBody);
				if (bytesRead < messageBodyLength) break;
				handleItem(messageBody);
				
				byte[] footer = new byte[10];
				bytesRead = in.read(footer);
				if (bytesRead < 10) break;
				
				//System.out.println(messageType + " : " + messageLength);
				
				bytesInSuccessfulItems += messageLength;
			}
			
			//System.out.println("Sleeping");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		

	}
	
	public static List<D2Item> items = new ArrayList<>();

	/*
	public static void sendItems(Session session) {
		for (D2Item item : items) {
			try {
				session.getBasicRemote().sendText(jackson.writeValueAsString(item));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} */
	

	
	private void handleItem(byte[] inputArray) {
		//D2Item item = new D2Item(inputArray);
		ItemDropContext dropContext = new ItemDropContext(inputArray);
		
		/*
		System.out.println(item.getDisplayString());
		try {
			System.out.println(jackson.writeValueAsString(item));
		} catch (JsonProcessingException e1) {
			throw new RuntimeException(e1);
		}
		*/
		/*
        for (Session session : D2ItemsEndpoint.sessions.values()) {
        	try {
				session.getBasicRemote().sendText(jackson.writeValueAsString(item));
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        
        */
		/*
		if (item.getQuality() == ItemQuality.RARE || ((Math.random()<0.1))) {
			//items.add(item);
	        for (Session session : D2ItemsEndpoint.sessions.values()) {
	        	try {
					session.getBasicRemote().sendText(jackson.writeValueAsString(item));
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		}*/

	}
	
	private void getItemContext(byte[] msg) {
		
		
	}
	
	static enum ItemDropSource {
		MONSTER,
		OBJECT;
		
		public static ItemDropSource forId(int id) {
			switch (id) {
				case 1: return MONSTER;
				case 2: return OBJECT;
			}
			throw new RuntimeException("Unexpected ItemDropSource : " + id);
		}
	}
	
	static class ItemResult {
		D2Item item;
		ItemDropContext dropContext;
		
		
	}
	
	static class Monster {
		int monsterTypeId;
		Set<Integer> flags;
		int uniqueMonsterId;
		int stringId;
		String monsterName;
		
		public Monster(ByteBuffer data) {
			monsterTypeId = data.getInt(0);
			int flagByte = (int) (data.get(4) & 0xff);
			flags =  new HashSet<>();
			for (int i = 0; i < 8; i++) {
				if ((flagByte % 2) == 1) {
					flags.add(i);
				}
				flagByte = flagByte >> 1;
			}
			
			
			uniqueMonsterId = data.get(5);
			stringId = data.getShort(6);
			this.monsterName = CombinedStringsDao.get().getString(stringId);
		}

		public int getMonsterTypeId() {
			return monsterTypeId;
		}

		public Set<Integer> getFlags() {
			return flags;
		}

		public int getUniqueMonsterId() {
			return uniqueMonsterId;
		}

		public int getStringId() {
			return stringId;
		}

		public String getMonsterName() {
			return monsterName;
		}
		
		
	}
	
	static class ItemDropContext {
		private Monster monster;
		private List<D2Item> items;
		
		public ItemDropContext(byte[] msg) {
			ByteBuffer buffer = ByteBuffer.wrap(msg).order(ByteOrder.LITTLE_ENDIAN);
			monster = new Monster(buffer);
			try {
				System.out.println("Start of ItemDrop for " + jackson.writeValueAsString(monster));
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
			int nextId = 8;
			items = new ArrayList<>();
			//System.out.println("nextId = " + nextId  +" , length= " + msg.length);
			while (nextId < msg.length) {
				//System.out.println("nextId = " + nextId  +" , length= " + msg.length);
				byte[] remainingBytes = Arrays.copyOfRange(msg, nextId, msg.length);
				D2Item item = new D2Item(remainingBytes);
				System.out.println(item.getDisplayString());
				items.add(item);
				nextId += item.getByteSize();
				//System.out.println("nextId = " + nextId  +" , length= " + msg.length);
			}
			
			System.out.println("End of ItemDrop ------------");;
			
			/*
			int numStats1Index = 0x60 + 0x88 + 0x50;
			int statArray1Index = numStats1Index + 2;
			int numStats1 = buffer.getShort(numStats1Index);
			int numStats2Index = statArray1Index + 8*numStats1;
			int numStats2 = buffer.getShort(numStats2Index);
			
			int dropSourceOffset =  0x60 + 0x88 + 0x50 + 2 + 8*numStats1 + 2 + 8*numStats2;
			
			int unitType = buffer.get(dropSourceOffset);
			ItemDropSource dropSource = ItemDropSource.forId(unitType);
			System.out.println("Drop Source : " + dropSource.name());
			if (dropSource == ItemDropSource.MONSTER) {
				monsterType = buffer.getInt(dropSourceOffset + 1 + 4);
				System.out.println("Monster type : " + monsterType);
			} else if (dropSource == ItemDropSource.OBJECT) {
				objectType = buffer.getInt(dropSourceOffset + 1 + 4);
				int idx = dropSourceOffset + 1 + 0x10;
				StringBuilder name = new StringBuilder();
				for (;;) {
					char c = buffer.getChar(idx); idx += 2;
					if (c == 0) break;
					name.append(c);
				}
				objectName = name.toString();
				System.out.println("Object Name : " + objectName);
			}
			*/
		}
	}
	
	
}
