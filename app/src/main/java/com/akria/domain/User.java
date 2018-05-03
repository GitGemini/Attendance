package com.akria.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class User {
	private static String DEFAULT_ICON = "defaulticon.png";

	private String userid;
	private String password;
	private String realname;
	private String phonenumber;
	private String username;
	private String studentid;
	private String usericon;
	private String groups;

	public static List<User> parseJsonArray(String jsonStr) {
		JSONArray arr = JSONObject.parseArray(jsonStr);
		List<User> users = new ArrayList<User>();
		for (Object object : arr) {
			User user = User.parseJsonString(object.toString());
			users.add(user);
		}
		return users;
	}
	
	/*
	 * 这里逐个取出数据进行赋值 也可以用这个方法进行数据转换 JSON.parseObject(json, new TypeReference<T>()
	 * {});
	 */
	public static User parseJsonString(String jsonStr) {
		JSONObject json = JSONObject.parseObject(jsonStr);
		User user = new User();
		// 必须有
		user.setPassword(json.getString("password"));
		user.setStudentid(json.getString("studentid"));
		user.setRealname(json.getString("realname"));
		user.setPhonenumber(json.getString("phonenumber"));
		
		//为空 注册；不为空 修改
		user.setUserid(json.getString("userid"));

		// 可能为空
		String username = json.getString("username");
		if (username == null) {// 如果用户名为空,则默认为他的真实姓名
			user.setUsername(json.getString("realname"));
		} else {
			user.setUsername(json.getString("username"));
		}

		String usericon = json.getString("usericon");
		if (usericon == null) {
			user.setUsericon(DEFAULT_ICON);
		} else {
			user.setUsericon(json.getString("usericon"));
		}
		return user;
	}
	
	public static User ResultSet2User(ResultSet rs) {
		User user = new User();
		try {
			user.setUserid(rs.getString(1));
			user.setUsername(rs.getString(2));
			user.setPassword(rs.getString(3));
			user.setRealname(rs.getString(4));
			user.setStudentid(rs.getString(5));
			user.setPhonenumber(rs.getString(6));			
			user.setUsericon(rs.getString(7));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return user;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getUsericon() {
		return usericon;
	}

	public void setUsericon(String usericon) {
		this.usericon = usericon;
	}

	public String getGroups() {
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}
}
