package com.example.yan.attendance.Db;

/**
 * Created by yan on 2017/10/28.
 */

public class UserDb {
    private String mPhone;
    private String mPassword;
    private String mRealName;
    private String mStudentID;
    private String mID;

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        mRealName = realName;
    }

    public String getStudentID() {
        return mStudentID;
    }

    public void setStudentID(String studentID) {
        mStudentID = studentID;
    }

    public String getID() {
        return mID;
    }

    public void setID(String id) {
        mID = id;
    }
}
