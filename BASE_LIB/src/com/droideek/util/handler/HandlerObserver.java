package com.droideek.util.handler;

public interface HandlerObserver {
	
	void handleMessage(android.os.Message msg);
	
	String getName();
	
}
