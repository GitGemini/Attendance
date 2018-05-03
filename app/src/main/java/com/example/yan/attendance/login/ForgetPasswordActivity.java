package com.example.yan.attendance.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.R;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private Button changeBtn;
    private EditText phoneEdit, pinEdit;
    private Button timeBtn;
    private String phone, pin;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();

        timeBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        phone = phoneEdit.getText().toString();
        switch (view.getId()){
            case R.id.btn_again_forget:
                checkAndSend();
                break;
            case R.id.change_password_button:
                checkSMS();
                break;
            case R.id.back_icon:
                finish();
                break;
        }

    }

    private void checkAndSend(){

        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。

        if (phone.length() != 11 || !phone.matches(telRegex)){
            toast("请输入11位有效手机号码");
//            snack("请输入11位有效手机号码");
//            输入时键盘会挡住信息的显示
        }
        else {
            BmobSMS.requestSMSCode(this, phone, "smsDemo", new RequestSMSCodeListener() {
                @Override
                public void done(Integer integer, BmobException e) {
                    if (e == null) {
                        //发送成功

                        toast("已发送短信验证");
                    } else {
                        toast("验证码发送失败，请检查网络连接");
                    }

                }
            });


            //倒计时
            new CountDownTimer(60000, 1000){
                @Override
                public void onTick(long millisUntilFinished) {
                    timeBtn.setText(millisUntilFinished/1000+"秒");
                }

                @Override
                public void onFinish() {
                    timeBtn.setClickable(true);
                    timeBtn.setText("重新发送");
                    checkAndSend();
                }
            }.start();
        }


    }

    private void checkSMS() {
        pin = pinEdit.getText().toString();
        if (pin.length() != 6) {
            toast("验证码为六位");
        } else {

//            BmobSMS.verifySmsCode(ForgetPasswordActivity.this, phone, pin, new VerifySMSCodeListener() {
//                @Override
//                public void done(BmobException e) {
//                    if (e == null) {
//                        Log.e("message", "7");
//                        toast("验证成功");
//                        SharedPreferences.Editor editor = getSharedPreferences("userData", MODE_PRIVATE).edit();
//                        editor.putString("phone", phone);
//                        editor.commit();
//                        Intent go_change_password = new Intent(ForgetPasswordActivity.this, ChangePasswordActivity.class);
//                        startActivity(go_change_password);
//
//                    } else {
//                        Log.e("message", "8");
//                        toast("验证码错误");
//                    }
//                }
//            });
            Intent intent = new Intent(ForgetPasswordActivity.this, ChangePasswordActivity.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }
    }

    private void initView(){
        changeBtn = (Button) findViewById(R.id.change_password_button);
        phoneEdit = (EditText) findViewById(R.id.edit_phone_forget);
        pinEdit = (EditText) findViewById(R.id.edit_pin_forget);
        timeBtn = (Button) findViewById(R.id.btn_again_forget);
        backBtn = (ImageView) findViewById(R.id.back_icon);
    }


    private void toast(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
}
