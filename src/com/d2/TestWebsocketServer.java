package com.d2;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.glassfish.tyrus.server.Server;

public class TestWebsocketServer {

	
	public static void main(String[] args) {
		runServer();
	}
	
	
	public static void runServer() {
	    Server server = new Server("localhost", 8025, "/websockets", null, D2ItemsEndpoint.class);
	    
	    
	    
	    try {
	        server.start();
	    } catch (Exception e) {
	        e.printStackTrace();
	        server.stop();
	    }
	}
}