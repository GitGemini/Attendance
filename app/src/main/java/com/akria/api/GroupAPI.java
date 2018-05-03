package com.akria.api;

import com.akria.domain.Group;
import com.akria.net.ClientRequest;
import com.alibaba.fastjson.JSONObject;

public class GroupAPI {

	public void createGroup(Group group, Callback callback) {
		JSONObject json = (JSONObject) JSONObject.toJSON(group);
		ClientRequest.Request(json, "101", callback);
	}

	public void updateGroup(Group group, Callback callback) {
		JSONObject json = (JSONObject) JSONObject.toJSON(group);
		ClientRequest.Request(json, "102", callback);
	}

	public void dissGroup(String gid, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("gid", gid);
		ClientRequest.Request(json, "103", callback);
	}

	public void joinGroup(String gid, String uid, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("gid", gid);
		json.put("uid", uid);
		ClientRequest.Request(json, "104", callback);
	}

	public void exitGroup(String gid, String uid, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("gid", gid);
		json.put("uid", uid);
		ClientRequest.Request(json, "105", callback);
	}

	public void getGroup(String gid, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("gid", gid);
		ClientRequest.Request(json, "106", callback);
	}

	public void getGroupList(Callback callback) {
		JSONObject json = new JSONObject();
		ClientRequest.Request(json, "107", callback);
	}

	public void getGroupMember(String gid, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("gid", gid);
		ClientRequest.Request(json, "108", callback);
	}
	
	public void getOwnGroups(String gowner, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("gowner", gowner);
		ClientRequest.Request(json, "109", callback);
	}

	public void getInGroups(String userid, Callback callback) {
		JSONObject json = new JSONObject();
		json.put("userid", userid);
		ClientRequest.Request(json, "110", callback);
	}
}
