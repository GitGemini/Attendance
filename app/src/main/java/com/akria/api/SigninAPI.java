package com.akria.api;

import com.akria.domain.Signin;
import com.akria.domain.SigninMember;
import com.akria.net.ClientRequest;
import com.alibaba.fastjson.JSONObject;

public class SigninAPI {


	public void createSignin(Signin signin, Callback callback) {
		JSONObject json = (JSONObject) JSONObject.toJSON(signin);
		ClientRequest.Request(json, "201", callback);
	}


	public void getSignins(String gid, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("gid", gid);
		ClientRequest.Request(json, "202", callback);
	}

	public void signin(SigninMember member, Callback callback) {
		JSONObject json = (JSONObject) JSONObject.toJSON(member);
		ClientRequest.Request(json, "203", callback);
	}


	public void findMembers(String gid, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("gid", gid);
		ClientRequest.Request(json, "204", callback);
	}


	public void findSigns(String uid, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("uid", uid);
		ClientRequest.Request(json, "205", callback);
	}
}
