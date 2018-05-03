package com.example.yan.attendance.login;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.R;

public class RegisterFirstActivity extends BaseActivity implements View.OnClickListener {

    private Button mSendAndNext;
    private EditText mPhoneText, mPasswordEdit;
    private static final int REQUEST_CODE =  1000;
    private String phone, password;
    private ImageView mBackImg, mDelectImg;
    private CheckBox mShowCheckBox;
    private static final String TAG = "register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_first);

        initView();
        mSendAndNext.setOnClickListener(this);
        mDelectImg.setOnClickListener(this);
        mBackImg.setOnClickListener(this);


        //        显示和隐藏密码
        mShowCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mPasswordEdit.setSelection(mPasswordEdit.length());
                }else {
                    mPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mPasswordEdit.setSelection(mPasswordEdit.length());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send_and_next:
                //                动态获取权限
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onClick: 获取权限失败");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},REQUEST_CODE);
                }else {
                    send();
                }
                break;
            case R.id.back_icon:
                finish();
                break;
            case R.id.delete_phone:
                mPhoneText.setText("");
                break;
            default:
                break;
        }
    }

    //    初始化
    private void initView(){
//        mPhoneEdit = (EditText) findViewById(R.id.phone_edit);
//        mPinEdit = (EditText) findViewById(R.id.pin_edit);
        mBackImg = (ImageView) findViewById(R.id.back_icon);
        mPhoneText = (EditText) findViewById(R.id.phone_text);
        mDelectImg = (ImageView) findViewById(R.id.delete_phone);
        mPasswordEdit = (EditText) findViewById(R.id.register_password);
        mShowCheckBox = (CheckBox) findViewById(R.id.password_checkbox);
        mSendAndNext = (Button) findViewById(R.id.send_and_next);

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.LOLLIPOP){
            int myColor = getResources().getColor(R.color.registerButton);
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(myColor);
        }
    }

    //动态获取权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if(grantResults.length >0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    //用户同意授权
                    send();
                }else{
                    //用户拒绝授权
                }
                break;
        }
    }


    //检测手机号和密码是否正确,并发送短信
    private void send(){
        Log.e("message", "2");
        phone = mPhoneText.getText().toString();
        password = mPasswordEdit.getText().toString();
//      来源：http://blog.csdn.net/qq_24956515/article/details/50910513
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。

        if (phone.length() != 11 || !phone.matches(telRegex)){
            toast("请输入11位有效手机号码");
//            snack("请输入11位有效手机号码");
//            输入时键盘会挡住信息的显示
        }
        else if (password.length()<6){
            toast("密码格式不正确");

        }
        else{
            Log.e("message", "3");

        //            .requestSMSCode(this, phone, "smsDemo", new RequestSMSCodeListener() {
//                @Override
//                public void done(Integer integer, BmobException e) {
//                    if (e == null){
//                        //发送成功
//
//                        toast("已发送短信验证");
//
//                        SharedPreferences.Editor editor = getSharedPreferences("userData",MODE_PRIVATE).edit();
//                        editor.putString("phone",phone);
//
//                        //密码加密
//                        editor.putString("password", MD5Utils.encode(password));
//                        editor.commit();
//                        Log.d(TAG, "send: editor ");
//                        Intent go_second = new Inntent(RegisterFirstActivity.this, RegisterSecondActivity.class);
//                        startActivity(go_second);
//
//                    }
//                    else {
//                        toast("验证码发送失败，请检查网络连接");
//                    }
//
//                }
//            });

            //测试用
            SharedPreferences.Editor editor = getSharedPreferences("userData",MODE_PRIVATE).edit();
            editor.putString("phone",phone);
            editor.putString("password", password);
            editor.commit();
            Log.d(TAG, "send: editor ");

            Intent intent = new Intent(RegisterFirstActivity.this, RegisterThirdActivity.class);
            startActivity(intent);
        }

    }

    private void toast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
}
