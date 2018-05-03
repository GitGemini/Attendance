package com.example.yan.attendance.ActivityFunction;

/**
 * Created by yan on 2017/12/21.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.akria.domain.Signin;
import com.alibaba.fastjson.JSONObject;


public class ToastShow1 {
    public static void ToastShow1(String Msg) {
        Toast.makeText(BaseActivity.getContext(),Msg,Toast.LENGTH_LONG).show();
    }
    public static void Broadcasts(String msg){
        Log.d("border", "Broadcasts: "+msg);

        SharedPreferences.Editor editor = BaseActivity.getContext().getSharedPreferences("SingIn", Context.MODE_PRIVATE).edit();
        editor.putString("Gid", Signin.parseJson(msg).getGid().toString());
        editor.putString("Latitude", String.valueOf(Signin.parseJson(msg).getLatitude()));
        editor.putString("Logintude", String.valueOf(Signin.parseJson(msg).getLogintude()));
        editor.commit();
        Intent to_receiver = new Intent("com.example.broadcast.service");
        BaseActivity.getContext().sendBroadcast(to_receiver);
    }
}