package com.henu.utils;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Base64.Encoder;

public class UserUtil {	
	
	public static String md5(String msg) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] data = md.digest(msg.getBytes("utf-8"));
			
			Encoder encoder = Base64.getEncoder();
			return new String(encoder.encode(data));			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
}
