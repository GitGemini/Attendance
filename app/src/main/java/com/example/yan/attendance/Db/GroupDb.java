package com.example.yan.attendance.Db;

import java.io.Serializable;

import static android.R.attr.name;
import static android.R.attr.theme;

/**
 * Created by yan on 2017/11/2.
 */

public class GroupDb implements Serializable {
    private int style;

    private String mName;
    private int mNumber;

    public GroupDb(int style, String name, int number){
        this.style = style;
        this.mName = name;
        this.mNumber = number;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }


    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }
}
