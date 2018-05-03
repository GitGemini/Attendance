package com.example.yan.attendance.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.UserAPI;
import com.akria.domain.Signin;
import com.akria.domain.User;
import com.alibaba.fastjson.JSONObject;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.MainActivity;
import com.example.yan.attendance.R;
import com.example.yan.attendance.Utils.MD5Utils;
import com.henu.utils.SocketUtil;

public class LoginUpActivity extends BaseActivity implements View.OnClickListener {

    private EditText mPhoneEdit, passwordEdit;
    private CheckBox passwordCheckbox;
    private ImageView mBackImg, mDeleteImg;
    private Button mLoginUpBtn;
    private TextView mForgetPwdText;
    private String phone, password;
    private static final String TAG = "test";
    private static final String ip = "10.248.65.35";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_up);

        initView();

        //登录功能
        mLoginUpBtn.setOnClickListener(this);
        //清除按钮功能
        mDeleteImg.setOnClickListener(this);
        //返回上一个页面功能
        mBackImg.setOnClickListener(this);
        //忘记密码，找回功能
        mForgetPwdText.setOnClickListener(this);


        //监控输入框内容，显示和隐藏清除按钮
        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            //文字改变时触发的事件
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    mDeleteImg.setVisibility(View.VISIBLE);
                }else {
                    mDeleteImg.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        //功能：显示和隐藏密码
        //来源：http://www.jb51.net/article/90004.htm
        passwordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_up_button:
                //判断功能
                phone = mPhoneEdit.getText().toString();
                password = passwordEdit.getText().toString();
                check(phone);
//            Intent intent = new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
//            sendBroadcast(intent);
                break;
            case R.id.forget_password:
                Intent intent1 = new Intent(LoginUpActivity.this, ForgetPasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.delete_phone:
                mPhoneEdit.setText("");
                break;
            case R.id.back_icon:
                finish();
                break;
            default:
                break;
        }
    }

    //检查手机号和密码是否正确,正确进入主页面
    private void check(String phone){
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        Log.d(TAG, "check: check");
        if (phone.length() != 11 || !phone.matches(telRegex)){

            Toast.makeText(LoginUpActivity.this,"请输入11位有效手机号码",Toast.LENGTH_SHORT ).show();
//            snack("请输入11位有效手机号码");
//            输入时键盘会挡住信息的显示
        }else {
            Log.d(TAG, "check: fetchitemtask");
            new FetchItemTask().execute();
//
//            Intent intent = new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
//            sendBroadcast(intent);

//
        }

    }

    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Log.d(TAG, "doInBackground: ");
                SocketUtil.setIPAdress(ip);
                UserAPI userAPI = new UserAPI();
                Log.d(TAG, "doInBackground: doit"+phone+password);

                //密码加密MD5Utils.encode(password)
                userAPI.login(phone, password , new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.d(TAG, "onSuccess: "+msg);
                        User user = User.parseJsonString(msg);
                        SharedPreferences.Editor editor = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE).edit();
                        editor.putString("UserId", user.getUserid());
                        editor.putString("UserPhone",user.getPhonenumber());
                        editor.putString("UserPassword",user.getPassword());
                        editor.putString("RealName", user.getRealname());
                        editor.putString("StudentId",user.getStudentid());
                        editor.putString("UserName", user.getUsername());
                        editor.commit();
                        Intent intent = new Intent("com.example.broadcastbestpractice.FORCE_OFFLINE");
                        sendBroadcast(intent);
                    }

                    @Override
                    public void onFail(String msg) {
                        Log.d(TAG, "onFail: ");
                        Toast.makeText(LoginUpActivity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                    }
                });



            }catch (Exception ioe){
                Log.d(TAG, "doInBackground: "+ioe);
            }
            return null;
        }
    }

    //初始化
    private void initView(){
        mPhoneEdit = (EditText) findViewById(R.id.phone_edit);
        passwordEdit = (EditText) findViewById(R.id.login_password);
        passwordCheckbox = (CheckBox) findViewById(R.id.password_checkbox);
        mBackImg = (ImageView) findViewById(R.id.back_icon);
        mDeleteImg = (ImageView) findViewById(R.id.delete_phone);
        mLoginUpBtn = (Button) findViewById(R.id.login_up_button);
        mForgetPwdText = (TextView) findViewById(R.id.forget_password);


//        功能：更改系统状态栏的颜色
//        来源：http://blog.csdn.net/zbh_1042354552/article/details/53215316
//        http://blog.csdn.net/jdsjlzx/article/details/41643587
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){
            int myColor = getResources().getColor(R.color.loginButton);
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(myColor);
        }
    }
}
