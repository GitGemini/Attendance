package com.example.yan.attendance.ActivityFunction;

import com.akria.domain.Group;
import com.example.yan.attendance.Db.GroupDb;

/**
 * Created by yan on 2017/12/22.
 */

public class GroToGroup {

    public static void change(Group group, GroupDb groupDb){
        groupDb.setName(group.getGname());
    }
}
