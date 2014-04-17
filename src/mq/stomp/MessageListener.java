package mq.stomp;

public interface MessageListener {
	boolean continueListen();
	void onMessage(String str);
}
