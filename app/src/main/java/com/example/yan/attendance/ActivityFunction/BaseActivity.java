package com.example.yan.attendance.ActivityFunction;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by yan on 2017/10/29.
 */


public class BaseActivity extends AppCompatActivity {
    private  static Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        context=getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
    public static Context getContext() {
        return context;
    }
}

