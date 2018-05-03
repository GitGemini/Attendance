package com.example.yan.attendance.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.UserAPI;
import com.akria.domain.User;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.R;
import com.henu.utils.SocketUtil;

public class ChangePasswordActivity extends BaseActivity {

    private Button sureBtn;
    private EditText passwordEdit;
    private String password;
    private CheckBox showPaswordBox;
    private ImageView backImg;
    private String phone;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        passwordEdit = (EditText) findViewById(R.id.change_password_edit);
        sureBtn = (Button) findViewById(R.id.sure_button);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = passwordEdit.getText().toString();
                if (password.length()>=6){
                    new FetchItemTask().execute();
//                    SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
//                    editor.putString("password", password);
//                    editor.commit();
//                    Intent intent = new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
//                    sendBroadcast(intent);
                }else {
                    Toast.makeText(ChangePasswordActivity.this,"密码不够六位",Toast.LENGTH_SHORT).show();
                }

            }
        });
        showPaswordBox = (CheckBox) findViewById(R.id.change_checkbox);
        showPaswordBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordEdit.setSelection(passwordEdit.length());
                }else {
                    passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordEdit.setSelection(passwordEdit.length());
                }
            }
        });
        backImg = (ImageView) findViewById(R.id.back_icon);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{

                SocketUtil.setIPAdress("192.168.43.62");
                UserAPI userAPI = new UserAPI();
                user = new User();
                userAPI.resetPassword(phone, password, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.d("s", "onSuccess: ");
                        Intent intent = new Intent(ChangePasswordActivity.this, LoginStartActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(String msg) {
                        Log.d("s", "onFail: ");

                    }
                });


            }catch (Exception ioe){
                Log.d("register", "doInBackground: "+ioe);
            }
            return null;
        }
    }

}
