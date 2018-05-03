package com.example.yan.attendance.login;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.Db.UserDb;
import com.example.yan.attendance.R;

import cn.bmob.sms.BmobSMS;

import static android.os.Build.VERSION_CODES.O;
import static android.webkit.WebSettings.PluginState.ON;

public class LoginStartActivity extends BaseActivity {
    private Button loginUpButton;
    private Button registerButton;
    private static final String TAG = "login";
    private UserDb mUserDb = new UserDb();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_start);
        initView();
        loginUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_login = new Intent(LoginStartActivity.this, LoginUpActivity.class );
                startActivity(to_login);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_register = new Intent(LoginStartActivity.this, RegisterFirstActivity.class);
                startActivity(to_register);
            }
        });

    }

    //初始化
    private void initView(){

        mUserDb.setPassword("12345");
//       Bmob初始化
        BmobSMS.initialize(getBaseContext(),"87588623b9a255fcf1cf1345b9962650" );

        loginUpButton = (Button) findViewById(R.id.login_up_button);
        registerButton = (Button) findViewById(R.id.register_button);


//        功能：更改系统状态栏的颜色
//        来源：http://blog.csdn.net/zbh_1042354552/article/details/53215316
//        http://blog.csdn.net/jdsjlzx/article/details/41643587
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){
            int myColor = getResources().getColor(R.color.top_bar);
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(myColor);
        }
    }
}
