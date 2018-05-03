package com.example.yan.attendance.ActivityFunction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.yan.attendance.login.LoginUpActivity;
import com.example.yan.attendance.main.HomeActivity;

/**
 * Created by yan on 2017/12/29.
 */

public class ForceOffLine2Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityCollector.finishAll();
        Intent intent1 = new Intent(context, LoginUpActivity.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    //这里之前为intent Android5.0报错，6.0却不报错
        context.startActivity(intent1);
    }
}
