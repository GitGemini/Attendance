package com.akria.net;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.akria.domain.Signin;
import com.akria.domain.SocketMessage;
import com.example.yan.attendance.ActivityFunction.ToastShow1;
import com.example.yan.attendance.MainActivity;
import com.henu.utils.MessageManager;
import com.henu.utils.SocketUtil;

public class ListenerHandler implements Runnable {

	private Socket s = SocketUtil.getSocket();
	
	@Override
	public void run() {
		System.out.println("开始监听服务器消息推送");
		try {			
			InputStream is = s.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			while (true) {
				sb.setLength(0);
				String msg = null;
				Log.d("guangbo", "run: 消息2");
				while (!"over".equals(msg = in.readLine())) {
					sb.append(msg);
				}
				msg = sb.toString();
				Log.d("guangbo", "run: 消息3");

				SocketMessage message = SocketMessage.parseJson(msg);
				if (SocketMessage.TYPE_SIGN == message.getType()) {// 签到消息
					// 发送广播

					ToastShow1.Broadcasts(message.getMessage());
					Log.d("guangbo", "run: 消息"+msg);
				} else {
					SocketMessage sm = SocketMessage.parseJson(msg);
					MessageManager.putMessage(sm);
					
//					System.out.println("收到一条普通消息\n"+sm);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
