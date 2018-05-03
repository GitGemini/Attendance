package com.akria.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SigninMember {
	private Integer gid;
	private Integer receiver;
	private String time;
	private String rlogintude;
	private String rlatitude;
	private Integer result;

	public Integer getGid() {
		return gid;
	}

	public void setGid(Integer gid) {
		this.gid = gid;
	}

	public Integer getReceiver() {
		return receiver;
	}

	public void setReceiver(Integer receiver) {
		this.receiver = receiver;
	}

	public String getRlogintude() {
		return rlogintude;
	}

	public void setRlogintude(String rlogintude) {
		this.rlogintude = rlogintude;
	}

	public String getRlatitude() {
		return rlatitude;
	}

	public void setRlatitude(String rlatitude) {
		this.rlatitude = rlatitude;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public static SigninMember parseJson(String jsonStr) {
		SigninMember signin = new SigninMember();
		JSONObject json = JSONObject.parseObject(jsonStr);
		signin.setGid(json.getInteger("gid"));
		signin.setReceiver(json.getInteger("receiver"));
		String time = json.getString("time");
		if (time == null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINESE);
			signin.setTime(sdf.format(new Date()));
		} else {
			signin.setTime(time);
		}
		signin.setRlatitude(json.getString("rlatitude"));
		signin.setRlogintude(json.getString("rlogintude"));
		signin.setResult(json.getInteger("result"));

		return signin;
	}

	public static List<SigninMember> parseJsonArray(String jsonStr) {
		JSONArray arr = JSONObject.parseArray(jsonStr);

		List<SigninMember> members = new ArrayList<SigninMember>();
		for (Object object : arr) {
			SigninMember member = parseJson(object.toString());
			members.add(member);
		}
		return members;
	}
}
