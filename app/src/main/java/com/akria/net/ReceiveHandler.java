package com.akria.net;

import com.akria.api.Callback;
import com.akria.domain.SocketMessage;
import com.henu.utils.MessageManager;

public class ReceiveHandler implements Runnable {
	private final Callback callback;
	private String mid;

	public ReceiveHandler(String mid, Callback callback) {
		this.callback = callback;
		this.mid = mid;
	}

	@Override
	public void run() {
		SocketMessage sm = MessageManager.getMessage(mid);		
		try {
			while (sm == null) {
				Thread.sleep(100);
				sm = MessageManager.getMessage(mid);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (SocketMessage.TYPE_EVENT == sm.getType()) {
			callback.onSuccess(sm.getMessage());
		} else if (SocketMessage.TYPE_ERROR == sm.getType()) {
			callback.onFail(sm.getMessage());
		}
	}	
}
