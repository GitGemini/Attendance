package com.akria.domain;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Group implements Serializable {
	private static String DEFAULT_ICON = "defaulticon.png";

	private String gid;
	private String gname;
	private String gowner;
	private int gnumber;
	private String gicon;
	
	public static Group ResultSet2Group(ResultSet rs) {
		Group group = new Group();
		try {
			group.setGid(rs.getString(1));
			group.setGname(rs.getString(2));
			group.setGowner(rs.getString(3));
			group.setGnumber(rs.getInt(4));
			group.setGicon(rs.getString(5));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return group;
	}
	
	public static List<Group> parseJsonArray(String jsonStr) {
		JSONArray arr = JSONObject.parseArray(jsonStr);
		List<Group> groups = new ArrayList<Group>();
		for (Object object : arr) {
			Group group = Group.parseJsonString(object.toString());
			groups.add(group);
		}
		return groups;
	}
	
	public static Group parseJsonString(String jsonStr) {
		JSONObject json = JSONObject.parseObject(jsonStr);
		Group group = new Group();
		group.setGid(json.getString("gid"));
		group.setGname(json.getString("gname"));
		group.setGowner(json.getString("gowner"));

		// 成员数默认为0
		Integer num = json.getInteger("gnumber");
		if (num == null) {
			group.setGnumber(0);
		} else {
			group.setGnumber(json.getInteger("gnumber"));
		}

		// 设置默认图片
		String icon = json.getString("gicon");
		if (icon == null) {
			group.setGicon(DEFAULT_ICON);
		} else {
			group.setGicon(icon);
		}
		group.setGicon(json.getString("gicon"));
		return group;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getGowner() {
		return gowner;
	}

	public void setGowner(String gowner) {
		this.gowner = gowner;
	}

	public int getGnumber() {
		return gnumber;
	}

	public void setGnumber(int gnumber) {
		this.gnumber = gnumber;
	}

	public String getGicon() {
		return gicon;
	}

	public void setGicon(String gicon) {
		this.gicon = gicon;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
