package com.example.yan.attendance.main;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.GroupAPI;
import com.akria.api.SigninAPI;
import com.akria.domain.Group;
import com.akria.domain.Signin;
import com.akria.domain.SigninMember;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.ActivityFunction.ToastShow1;
import com.example.yan.attendance.R;
import com.henu.utils.SignTest;

import java.io.File;
import java.util.List;

public class DownLoadActivity extends AppCompatActivity {
    private Button downBtn;
    private File file;
    private String gid;
    private Button shareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        SharedPreferences shared2 = BaseActivity.getContext().getSharedPreferences("StartAttend", Context.MODE_PRIVATE);
        gid = shared2.getString("Gid","-1");
        downBtn = (Button)findViewById(R.id.down_Group_Msg);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("down", "onClick: ");
                ToastShow1.ToastShow1("xiazai");
                cheack();

                Log.d("Download", "onSuccess: over");
                //addMsg();

//                shareMsg();
            }
        });
        shareBtn = (Button) findViewById(R.id.share_Group_Msg);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(Intent.ACTION_SEND);
                Uri uri=Uri.fromFile(new File("/storage/emulated/0/bill.xls"));
                intent2.putExtra(Intent.EXTRA_STREAM,uri);
                intent2.setType("application/vnd.ms-excel");
                startActivity(Intent.createChooser(intent2,"share"));


            }
        });
    }



    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Log.d("Download", "onSuccess: start");
                SigninAPI signinAPI = new SigninAPI();
                signinAPI.findMembers(gid, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.d("Download", "onSuccess: "+msg);
                        SigninMember sign = new SigninMember();
                        sign.setGid(1);
                        sign.setReceiver(2);
                        sign.setResult(2);
                        sign.setRlatitude("23");
                        sign.setRlogintude("322");
                        sign.setTime("232");
                        List<SigninMember> signinMembers = SigninMember.parseJsonArray(msg);
                        signinMembers.add(sign);
                        Log.d("Download", "onSuccess: "+signinMembers.size());


                        File file1 = new File(Environment.getExternalStorageDirectory( ).toString()+File.separator +"seantest");//仅创建路径的File对象
                        if(!file1.exists()){
                            file1.mkdir();//如果路径不存在就先创建路径
                        }
                        if (isExternalStorageWritable()){
                           try {
                               File sdCardDir = Environment.getExternalStorageDirectory();
                               Log.e("save", sdCardDir.getPath());
                               File targetFile = new File(sdCardDir.getCanonicalPath()+"/bill.xls");
                               SignTest.export(signinMembers,"/bill.xls");
                           }catch (Exception e){
                               Log.d("Download", "onSuccess: "+e);
                           }

//                            File picFile = new File(file1,"bill.xls");//然后再创建路径和文件的File对象
////                        String filePath = Environment.getExternalStorageDirectory() + "/family_bill";
//                            String filePath = Environment.getExternalStorageDirectory() + "/bill.xls";
//                            //file = new File(filePath, "bill.xls");
//                            Log.d("Download", "onSuccess: "+picFile);
//                            SignTest.export(signinMembers,picFile.toString());
                        }else {
                            ToastShow1.ToastShow1("没有写权限");
                        }


                    }

                    @Override
                    public void onFail(String msg) {

                        Log.d("Download", "onSuccess: "+msg);
                    }
                });


            }catch (Exception ioe){
                Log.d("msg", "doInBackground: "+ioe);
            }
            return null;
        }
    }
    //重写方法获取读取sd卡权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    new FetchItemTask().execute();
                }else {
                    Toast.makeText(DownLoadActivity.this,"权限被拒绝",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    private void cheack(){
        if (ContextCompat.checkSelfPermission(DownLoadActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(DownLoadActivity.this ,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        }else {
            new FetchItemTask().execute(); //如果获取继续执行方法
        }
    }
//    private void requestWritePermission(){
//        if(ContextCompat.checkSelfPermission(getBaseContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERMISSION);
//        }
//    }




    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }



    public void addMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    SigninAPI signinAPI = new SigninAPI();
                    signinAPI.findMembers("1", new Callback() {
                        @Override
                        public void onSuccess(String msg) {

                            Log.d("Download", "onSuccess: "+msg);
                            List<SigninMember> signinMembers = SigninMember.parseJsonArray(msg);
                            String filePath = Environment.getExternalStorageDirectory() + "/family_bill";
                            file = new File(filePath, "bill.xls");
                            Log.d("Download", "onSuccess: "+file.toString());
                            SignTest.export(signinMembers,file.toString());
                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });


                }catch (Exception ioe){
                    Log.d("msg", "doInBackground: "+ioe);
                }

            }
        }).start();
    }


}
