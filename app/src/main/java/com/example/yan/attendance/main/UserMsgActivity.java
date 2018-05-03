package com.example.yan.attendance.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.akria.api.Callback;
import com.akria.api.SigninAPI;
import com.akria.api.UserAPI;
import com.akria.domain.SigninMember;
import com.akria.domain.User;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.ActivityFunction.ToastShow1;
import com.example.yan.attendance.R;
import com.henu.utils.SignTest;

import java.io.File;
import java.util.List;

public class UserMsgActivity extends AppCompatActivity {
    private ImageView backImg;
    private EditText nameEdit, phoneEdit,realNameEdit, studentIDEdit;
    private Button upMsgBtn;
    private String name, phone, realName, studentId, Id,password;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);
        initView();
        SharedPreferences shared3 = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        Id = shared3.getString("UserId","0");
        phone = shared3.getString("UserPhone","0");
        name = shared3.getString("UserName","0");
        realName = shared3.getString("RealName","0");
        studentId = shared3.getString("StudentId","0");
        password = shared3.getString("UserPassword","0");
        nameEdit.setText(name);
        phoneEdit.setText(phone);
        realNameEdit.setText(realName);
        studentIDEdit.setText(studentId);
        upMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = new User();
                user.setUserid(Id);
                user.setPassword(password);
                user.setRealname(realNameEdit.getText().toString());
                user.setPhonenumber(phoneEdit.getText().toString());
                user.setUsername(nameEdit.getText().toString());
                user.setStudentid(studentIDEdit.getText().toString());
                new FetchItemTask().execute();
                Log.d("changeUserMsg", "onClick: "+user);
                ToastShow1.ToastShow1("修改信息成功");
                SharedPreferences.Editor editor2 = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE).edit();
                editor2.putString("UserId", user.getUserid());
                editor2.putString("UserPhone",user.getPhonenumber());
                editor2.putString("UserPassword",user.getPassword());
                editor2.putString("RealName", user.getRealname());
                editor2.putString("StudentId",user.getStudentid());
                editor2.putString("UserName",user.getUsername());
                editor2.commit();
            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initView(){
        backImg = (ImageView) findViewById(R.id.back);
        nameEdit = (EditText) findViewById(R.id.user_msg_name);
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        studentIDEdit = (EditText) findViewById(R.id.studentIdEdit);
        realNameEdit = (EditText) findViewById(R.id.realNameEdit);
        upMsgBtn = (Button) findViewById(R.id.UpUserMsg);
    }


    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                UserAPI userAPI = new UserAPI();
                userAPI.changeUserInfo(user, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.d("changeuser", "onSuccess: "+msg);
                    }

                    @Override
                    public void onFail(String msg) {
                        Log.d("changeuser", "onSuccess: "+msg);

                    }
                });


            }catch (Exception ioe){
                Log.d("msg", "doInBackground: "+ioe);
            }
            return null;
        }
    }



}
