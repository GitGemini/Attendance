package com.akria.net;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.henu.utils.SocketUtil;

public class SendMsgHandler implements Runnable{
	private Socket socket = SocketUtil.getSocket();
	private String msg;
	
	public SendMsgHandler(String msg) {
		this.msg = msg;
	}
	
	@Override
	public void run() {
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			out.println(msg);
			out.println("over");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
