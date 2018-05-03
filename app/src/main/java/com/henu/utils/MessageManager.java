package com.henu.utils;

import java.util.LinkedList;
import java.util.List;

import com.akria.domain.SocketMessage;

public class MessageManager {
	private static List<SocketMessage> messages = new LinkedList<SocketMessage>();

	public static SocketMessage getMessage(String mid) {
//		System.out.println("到消息队列中取");
		synchronized (messages) {
			for (SocketMessage sm : messages) {
				if (mid.equals(sm.getMid())) {
					System.out.println("从消息队列中取走一条");
					messages.remove(sm);
					return sm;
				}
			}
		}
		return null;
	}

	public static void putMessage(SocketMessage message) {
//		System.out.println("向消息队列中存放一条消息");
		synchronized (messages) {
			messages.add(message);
		}
	}
}
