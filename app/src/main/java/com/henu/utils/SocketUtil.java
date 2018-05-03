package com.henu.utils;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketUtil {
//	//	private static String IPAdress = "127.0.0.1";
	private static String IPAdress = "192.168.43.62";
//	private static String IPAdress = "192.168.1.100";
//	private static String IPAdress = "10.248.64.109";
	private static final int PORT = 52068;

	private static Socket socket;

	static {
		try {
			Log.e("socket", "breforeInit");
			socket = new Socket(IPAdress, PORT);
			Log.e("socket", "afterInit");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void setIPAdress(String ip) {
//		IPAdress = ip;
	}

//	private static class SocketHolder {
//        private static final SocketUtil INSTANCE = new SocketUtil();
//    }

//    public static final SocketUtil getInstance() {
//        Log.e("socket", "getInstance");
//        return SocketHolder.INSTANCE;
//    }
//
//	private SocketUtil() {	}

	public static Socket getSocket() {
		Log.e("socket", "getSocket"+(socket==null));
		return socket;
	}
}
