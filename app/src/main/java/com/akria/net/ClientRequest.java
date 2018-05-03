package com.akria.net;

import com.akria.api.Callback;
import com.akria.domain.SocketMessage;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ClientRequest {
	private SocketMessage message;

	private static Thread thread = new Thread(new ListenerHandler());

	public ClientRequest(SocketMessage message) {
		this.message = message;
	}

	public void sendRequest() {
		SendMsgHandler handler = new SendMsgHandler(JSON.toJSONString(message));
		Thread t = new Thread(handler);
		t.start();
	}
	
	public static void Request(JSONObject args, String mid, Callback callback) {
		SocketMessage sm = new SocketMessage();
		sm.setType(SocketMessage.TYPE_EVENT);
		sm.setMid(mid);
		sm.setMessage(args.toJSONString());
		ClientRequest request = new ClientRequest(sm);
		request.sendRequest();
		if(!thread.isAlive()) {
			thread.start();
		}
		Thread t = new Thread(new ReceiveHandler(sm.getMid(), callback));
		t.start();		
	}	
}
