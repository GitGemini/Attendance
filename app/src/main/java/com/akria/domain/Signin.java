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

public class Signin {
	private Integer id;
	private Integer originator;
	private Integer gid;
	private String time;
	private String logintude;
	private String latitude;
	private String region;
	private Boolean state;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOriginator() {
		return originator;
	}
	public void setOriginator(Integer originator) {
		this.originator = originator;
	}
	public Integer getGid() {
		return gid;
	}
	public void setGid(Integer gid) {
		this.gid = gid;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLogintude() {
		return logintude;
	}
	public void setLogintude(String logintude) {
		this.logintude = logintude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
	
	public static List<Signin> parseJsonArray(String jsonStr) {
		JSONObject json = JSONObject.parseObject(jsonStr);
		String arrStr = json.getString("signs");
		JSONArray arr = JSONObject.parseArray(arrStr);
		List<Signin> signs = new ArrayList<Signin>();
		for (Object object : arr) {
			Signin sign = Signin.parseJson(object.toString());
			signs.add(sign);
		}
		return signs;
	}
	
	public static Signin parseJson(String jsonStr) {
		Signin sign = new Signin();
		JSONObject json = JSONObject.parseObject(jsonStr);
		sign.setId(json.getInteger("id"));
		sign.setOriginator(json.getInteger("originator"));
		sign.setGid(json.getInteger("gid"));
		
		String time = json.getString("time");
		if(time==null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINESE);
			sign.setTime(sdf.format(new Date()));
		}else {
			sign.setTime(time);
		}
		sign.setLatitude(json.getString("latitude"));
		sign.setLogintude(json.getString("logintude"));
		sign.setRegion(json.getString("region"));
		boolean state = json.getBoolean("state");
		if(state) {
			sign.setState(true);
		}else {
			sign.setState(state);
		}
		return sign;
	}
	
	public void diff() {
		
	}
}
