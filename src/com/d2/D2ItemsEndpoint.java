package com.d2;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.common.base.Throwables;

@ServerEndpoint(value = "/echo")
public class D2ItemsEndpoint{
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public static Map<String,Session> sessions = new HashMap<>();
	
	
	@OnMessage
	public void onMessage(String message, Session session) throws IOException {
		System.out.println("Received message " + message);
		logger.info("got message");
        //session.getBasicRemote().sendText("Response from server");
        logger.info(""+session);
    }
	
    @OnOpen
    public void onOpen(Session session) throws IOException {
    	logger.info("Session " + session.getId() + " has connected");
        sessions.put(session.getId(), session);
        D2ItemListener.sendItems(session);
        
    }
  
    @OnClose
    public void onClose(Session session) throws IOException {
    	logger.info("Got Close Message from Session " + session.getId());
        sessions.remove(session.getId());
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println(throwable.getMessage() + " : " + Throwables.getStackTraceAsString(throwable));
    }
}