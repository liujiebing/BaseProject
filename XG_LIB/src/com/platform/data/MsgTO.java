package com.platform.data;


import com.droideek.entry.data.Entry;

public class MsgTO extends Entry {
	
	private static final long serialVersionUID = -699365187473337653L;
	private String msg;
	public static final int TYPE_PROGRAM_ERROR = -1;
	public static final int TYPE_NET_ERROR = -2;

	public int msgType;
	public int msgId;
	
	public MsgTO(String msg) {
		super();
		this.msg = msg;
	}

	
	public MsgTO() {
		super();
	}


	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public MsgTO(String msg, int msgType, int msgId) {
		this.msg = msg;
		this.msgType = msgType;
		this.msgId = msgId;
	}

	public MsgTO(int msgType) {
		this.msgType = msgType;
	}

	public MsgTO(int msgType, int msgId) {
		this.msgType = msgType;
		this.msgId = msgId;
	}
}
