package mq.stomp;

public interface Listener {
	void onEvent(String str);
}
