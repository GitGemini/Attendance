package com.akria.api;

import com.akria.domain.User;
import com.akria.net.ClientRequest;
import com.alibaba.fastjson.JSONObject;

public class UserAPI{

	public void register(User user, Callback callback) {
		JSONObject json = (JSONObject) JSONObject.toJSON(user);
		ClientRequest.Request(json, "001", callback);
	}

	public void login(String phonenumber, String password, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("phonenumber", phonenumber);
		json.put("password", password);
		ClientRequest.Request(json, "002", callback);
	}

	public void resetPassword(String phonenumber, String password, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("phonenumber", phonenumber);
		json.put("password", password);
		ClientRequest.Request(json, "003", callback);
	}

	public void queryUserInfo(String phonenumber, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("phonenumber", phonenumber);
		ClientRequest.Request(json, "004", callback);
	}

	public void changeUserInfo(User user, Callback callback) {
		JSONObject json = (JSONObject) JSONObject.toJSON(user);
		ClientRequest.Request(json, "005", callback);
	}

	public void findPhonenumber(String phonenumber, Callback callback) {		
		JSONObject json = new JSONObject();
		json.put("phonenumber", phonenumber);		
		ClientRequest.Request(json, "006", callback);
	}
}
