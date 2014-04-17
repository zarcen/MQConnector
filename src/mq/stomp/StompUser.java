package mq.stomp;

import mq.stomp.Listener;

import org.apache.activemq.transport.stomp.StompConnection;
import org.apache.activemq.transport.stomp.StompFrame;

public class StompUser implements Runnable, MessageListener {
	String host;
	int port;
	String destination; // topic or queue
	String username;
	String password;
	
	StompConnection connection;
	long check_timeout;
	boolean connecting = false;
	Listener listener;
	MessageListener msgListener;
	
	
	StompUser () {
		host = "localhost";
		port = 61613;
		destination = "/topic/test";
		username = "";
		password = "";
		check_timeout = 200;
		//connect();
	}
	
	StompUser (String _host, int _port, String _destination) {
		host = _host;
		port = _port;
		destination = _destination;
		username = "";
		password = "";
		check_timeout = 500;
		//connect();
	}
	
	public void addListener(Listener l) {
		listener = l;
	}
	
	public void addMsgListener(MessageListener l) {
		msgListener = l;
	}
	
	void setHost(String _host) {
		host = _host;
	}
	
	void setDestination(String _destination) {
		destination = _destination;
	}
	
	void connect() {
		connection = new StompConnection();
		try {
			connection.open(host, port);
			connection.connect(username,password);
			connection.subscribe(destination);
			addMsgListener(this); // make self as a listener
			connecting = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isStarted() {
		return connecting;
	}

	
	// send to the default/listening topic or queue
	void send(String message) {
		try {
			connection.send(destination, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// send to other destination(topic/queue)
	void send(String _destination, String message) {
		try {
			connection.send(_destination, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void disconnect() {
		try {
			connection.disconnect();		
			connecting = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void stopListen() {
		connecting = false;
	}
	
	
	@Override
	public void run() {
		StompFrame message = null;
		while(msgListener.continueListen()) {
			try {
				message = connection.receive(check_timeout); // default: 500 msec timeout
			} catch (java.net.SocketTimeoutException se) {
				continue; // receive nothing
			} catch (Exception e) {
				e.printStackTrace();
			}
			// if receive something, do onMessage();
			onMessage(message.getBody());
		}
		this.disconnect();
		
	}

	@Override
	public boolean continueListen() {
		return connecting;
	}

	@Override
	public void onMessage(String str) {
		listener.onEvent(str);	
	}

	
}
