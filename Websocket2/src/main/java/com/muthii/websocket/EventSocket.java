package com.muthii.websocket;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ClientEndpoint
@ServerEndpoint(value="/events/")
public class EventSocket {
	private Vector<Session> connections = new Vector<Session>();
	private ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
	public static LinkedList<String> incoming = new LinkedList<String>();
	public static LinkedList<String> outgoing = new LinkedList<String>();
	
	public EventSocket(){
		keepAlive();
	}
	
	@OnOpen
	public void onWebSocketConnect(Session sess)
	{
		System.out.println("Socket Connected: " + sess);
		connections.add(sess);
	}
	@OnMessage
	public void onWebSocketText(Session session,String message)
	{
		System.out.println("Received TEXT message: " + message);
		session.getAsyncRemote().sendText("Response from server");
		System.out.println("Session ID: " + session.getId());
		incoming.add(message);
		
		//Broad cast incoming text to all clients
		broadcastMsg(message);
	}
	@OnClose
	public void onWebSocketClose(Session session,CloseReason reason)
	{
		System.out.println("Socket Closed: " + reason);
		connections.remove(session);
	}
	@OnError
	public void onWebSocketError(Throwable cause)
	{
		cause.printStackTrace(System.err);
	}	
	
	/**
	 *send messages to all connections every 15 seconds to keep the sessions alive.
	 */
	private void keepAlive(){		
		ses.scheduleAtFixedRate(new Runnable() {
		@Override
		public void run() {
			for (Session sess : connections) {
				try {
					sess.getBasicRemote().sendText("keep-alive");
				} catch (IOException ex) {
					connections.remove(sess);
				    System.out.println("Websocket Error " + ex.getMessage());
				}
			}
		}
		}, 15, 15, TimeUnit.SECONDS);
	}
	
	//call on connect
	public void addConnections(Session sess){
		this.connections.add(sess);
	}
	
	//broadcast to all connected clients
	public void broadcastMsg(String msg){
		for (Session sess : connections) {
			sess.getAsyncRemote().sendText(msg);
		}
	}

}
