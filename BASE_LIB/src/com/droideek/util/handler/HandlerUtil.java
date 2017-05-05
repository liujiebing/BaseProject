package com.droideek.util.handler;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class HandlerUtil extends Handler {
	
	private static HandlerUtil instance;
	
	public static HandlerUtil instance() {
		if(instance == null) {
			instance = new HandlerUtil();
		}
		return instance;
	}

	public String toStriong() {
		return ""+list.size();
	}
	
	private HandlerUtil() {
	}

	private List<HandlerObserver> list = new ArrayList<HandlerObserver>();

	@Override
	public void handleMessage(android.os.Message msg) {
		setChanged();
		notifyObservers(msg);
	}

	boolean changed = false;

	public void addObserver(HandlerObserver observer) {
		if (observer == null) {
			throw new NullPointerException("observer == null");
		}
		synchronized (this) {
//			if (!list.contains(observer))
//				list.add(observer);
			final String name = observer.getName();
			HandlerObserver[] arrays = null;
			int size = list.size();
			arrays = new HandlerObserver[size];
			list.toArray(arrays);
			for (HandlerObserver hand : arrays) {
				if (hand.getName().equals(name)) {
					list.remove(hand);
				}
			}
			list.add(observer);
		}
	}

	protected void clearChanged() {
		changed = false;
	}

	public int countObservers() {
		return list.size();
	}

	public synchronized void deleteObserver(HandlerObserver observer) {
		list.remove(observer);
	}

	public synchronized void deleteObservers() {
		list.clear();
	}
	
	public static void clear() {
		if(instance != null) {
			instance.deleteObservers();
			instance.removeCallbacksAndMessages(null);
		}
		instance = null;
	}

	public boolean hasChanged() {
		return changed;
	}

	public void notifyObservers() {
		notifyObservers(null);
	}

	public void notifyObservers(Message msg) {
		int size = 0;
		HandlerObserver[] arrays = null;
		synchronized (this) {
			if (hasChanged()) {
				clearChanged();
				size = list.size();
				arrays = new HandlerObserver[size];
				list.toArray(arrays);
			}
		}
		if (arrays != null && arrays.length >0) {
			for (HandlerObserver observer : arrays) {
				if (msg != null && (msg.arg2 == observer.getName().hashCode())) {

					observer.handleMessage(msg);
				}
			}
		}
	}

	protected void setChanged() {
		changed = true;
	}

}
