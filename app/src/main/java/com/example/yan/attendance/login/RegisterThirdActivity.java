package com.example.yan.attendance.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.akria.api.Callback;
import com.akria.api.UserAPI;
import com.akria.domain.User;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.R;
import com.example.yan.attendance.Utils.MD5Utils;
import com.henu.utils.SocketUtil;

public class RegisterThirdActivity extends BaseActivity {

    private Button mOverBtn;
    private EditText realNameEdit, studentIDEdit;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_third);
        initView();
        mOverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("userData",MODE_PRIVATE).edit();
                editor.putString("realName",realNameEdit.getText().toString());
                editor.putString("studentID", studentIDEdit.getText().toString());
                editor.commit();
                SharedPreferences preferences = getSharedPreferences("userData",MODE_PRIVATE);
                user = new User();
                user.setPhonenumber( preferences.getString("phone",""));
                //user.setPassword(MD5Utils.encode(preferences.getString("password","")));
                user.setPassword(preferences.getString("password",""));
                user.setRealname(realNameEdit.getText().toString());
                user.setStudentid(studentIDEdit.getText().toString());
                Log.d("register", "onClick: "+user);

                new FetchItemTask().execute();
                Log.d("register", "onClick: 执行结束");


            }
        });
    }


    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{

                Log.d("register", "onClick: 方法执行"+user);
                SocketUtil.setIPAdress("192.168.43.62");
                UserAPI userAPI = new UserAPI();
                userAPI.register(user, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        user = User.parseJsonString(msg);
                        Log.d("register", "onSuccess: 注册成功");
                        SharedPreferences.Editor editor2 = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE).edit();
                        editor2.putString("UserId", user.getUserid());
                        editor2.putString("UserPhone",user.getPhonenumber());
                        editor2.putString("UserPassword",user.getPassword());
                        editor2.putString("RealName", user.getRealname());
                        editor2.putString("StudentId",user.getStudentid());
                        editor2.commit();
                        Intent intent2 = new Intent(RegisterThirdActivity.this,LoginUpActivity.class);
                        startActivity(intent2);
//                        Intent intent = new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
//                        sendBroadcast(intent);

                    }

                    @Override
                    public void onFail(String msg) {
                        Log.d("register", "onFail: msg");
                    }
                });



            }catch (Exception ioe){
                Log.d("register", "doInBackground: "+ioe);
            }
            return null;
        }
    }

    private void initView(){
        mOverBtn =(Button) findViewById(R.id.register_over);
        realNameEdit = (EditText) findViewById(R.id.real_name_edit_text);
        studentIDEdit = (EditText) findViewById(R.id.student_id_edit_text);

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){
            int myColor = getResources().getColor(R.color.registerButton);
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(myColor);
        }
    }
}
