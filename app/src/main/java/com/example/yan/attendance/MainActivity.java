package com.example.yan.attendance;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akria.api.UserAPI;
import com.alibaba.fastjson.JSONObject;
import com.henu.utils.SocketUtil;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private static final String ip = "10.248.117.63";
    private static final String TAG = "mainActivity";


    @Override

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = (Button) findViewById(R.id.btn_link);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                SharedPreferences pref = getSharedPreferences("userData",MODE_PRIVATE);
////                Log.d(TAG, "onClick: 手机号"+pref.getString("phone","")+"\n密码："
////                        +pref.getString("password","")+"\n真实姓名："
////                        +pref.getString("realName","")+"\n学号："
////                        +pref.getString("studentID",""));
//                new FetchItemTask().execute();
//
//            }
//        });
    }


    private class FetchItemTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                SocketUtil.setIPAdress(ip);
                UserAPI userAPI = new UserAPI();
//                userAPI.findPhonenumber("18637680982", new Callback() {
//                    @Override
//                    public void onSuccess(String s) {
//                        JSONObject jason = JSONObject.parseObject(s);
//                        String st = jason.getString("info");
//                        String sb = jason.getString("code");
//                        Log.d(TAG, "onSuccess: "+st+"--"+sb);
//                    }
//
//                    @Override
//                    public void onFail(String s) {
//                        JSONObject jason = JSONObject.parseObject(s);
//                        String st = jason.getString("info");
//                        String sb = jason.getString("code");
//                        Log.d(TAG, "onSuccess: "+st+"--"+sb);
//                    }
//                });
            }catch (Exception ioe){
                Log.d(TAG, "doInBackground: "+ioe);
            }
            return null;
        }
    }
}
