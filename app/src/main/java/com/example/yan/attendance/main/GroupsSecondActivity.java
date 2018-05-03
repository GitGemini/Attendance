package com.example.yan.attendance.main;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.GroupAPI;
import com.akria.api.SigninAPI;
import com.akria.domain.Group;
import com.akria.domain.Signin;
import com.akria.domain.SigninMember;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.ActivityFunction.ToastShow1;
import com.example.yan.attendance.Db.GroupDb;
import com.example.yan.attendance.R;

import java.util.List;

public class GroupsSecondActivity extends AppCompatActivity {
    private TextView mGroupNameText;
    private TextView mNumberText;
    private ImageView backImg;
    private Button startBtn;
    private LocationManager mLocationManager;
    private String provider;
    public static final int SHOW_LOCATION =0;
    private double lngA, latA, eachlocation;
    private ImageView  mClipLeftImageView;
    private Button myTryBtn;
    private TextView attendedNumText;
    private Group mGroup ;
    private String gId, Latitude, Logintude;
    private String name, number;
    private String mId;
    private TextView groupIDText;
    private Button outGroupText;


    private final double EARTH_RADIUS = 6378137.0;
    private Handler handler2 = new Handler(){
        public  void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    mGroupNameText.setText(name);
                    mNumberText.setText("群人数"+number);
                    groupIDText.setText("群ID:"+gId);
                    attendedNumText.setText("已签到人数：1/"+number);
                    break;
                case 2:
                    outGroupText.setEnabled(false);
                    finish();
                    break;
                default:
                        break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_second);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            getLocation();
        }
        SharedPreferences shared2 = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        mId = shared2.getString("UserId","-1");

//        SharedPreferences shared = getBaseContext().getSharedPreferences("SingIn", Context.MODE_PRIVATE);
//        mGroup.setGid(shared.getString("Gid","0"));
//        mGroup.setGid(getIntent().getStringExtra("gId"));
//        mGroup.setGid("2");
        mGroup =(Group) getIntent().getSerializableExtra("groupInfo");
//        Log.d("second", "onCreate: "+mGroup.getGid()+"-----");
        //new FetchItemTask().execute();
       // mGroupDb =(GroupDb) getIntent().getSerializableExtra("groupInfo");

        Log.d("second", "onCreate: "+mGroup);

        SharedPreferences shared = BaseActivity.getContext().getSharedPreferences("SingIn", Context.MODE_PRIVATE);
        gId = shared.getString("Gid","错误");
        Latitude = shared.getString("Latitude","");
        Logintude = shared.getString("Logintude","");
        Log.d("second", "showNotification: "+gId+Latitude+Logintude);

        mGroupNameText = (TextView) findViewById(R.id.group_second_name_text_view);
        mNumberText = (TextView) findViewById(R.id.attend_group_number_text_view);
        groupIDText = (TextView) findViewById(R.id.group_Id_text);
        mClipLeftImageView = (ImageView) findViewById(R.id.iv_image_clip_left);
        attendedNumText =(TextView)findViewById(R.id.attended_number_text_view);
        startBtn = (Button) findViewById(R.id.to_attendance_btn);
        mClipLeftImageView = (ImageView) findViewById(R.id.iv_image_clip_left);
        outGroupText = (Button) findViewById(R.id.out_group_btn);
        outGroupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outGroup();
                ToastShow1.ToastShow1("退群成功");
            }
        });
//        String name = mGroup.getGname();
//        Log.d("myname", "onCreate: "+name);
        if (mGroup == null) {
            addMsg();
            mClipLeftImageView.setVisibility(View.VISIBLE);
            attendedNumText.setVisibility(View.VISIBLE);
            startBtn.setBackgroundResource(R.drawable.button_groups_circular);
            startBtn.setText("点击签到");
            startBtn.setEnabled(true);
        }else {
            name = mGroup.getGname();
            number = "该群人数："+mGroup.getGnumber();
            gId = mGroup.getGid();
            Log.d("second", "onCreate: "+name+number);
            mGroupNameText.setText(""+name);
            mNumberText.setText("群人数："+number);
            groupIDText.setText("群ID："+gId);
            mClipLeftImageView.setVisibility(View.GONE);
            attendedNumText.setVisibility(View.GONE);
            startBtn.setBackgroundResource(R.drawable.button_group_false_circular);
            startBtn.setText("未发起签到");
            startBtn.setEnabled(false);

        }

        backImg = (ImageView) findViewById(R.id.back);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mClipLeftImageView.setImageLevel(10000);
        mClipLeftImageView.getDrawable().setLevel(0);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eachlocation = getDistance(latA, lngA,Double.valueOf(Logintude) ,Double.valueOf(Latitude));
                int eachInt = (int) eachlocation;
                if (eachInt>100){
                    Log.d("msg", "onClick: "+eachlocation);
                    Toast.makeText(GroupsSecondActivity.this,"你的位置太远，无法签到"+
                            eachInt+"lng:"+lngA+"lat:"+latA+"Lat:"+Latitude+"Log:"+Logintude,Toast.LENGTH_SHORT).show();


                }else {

                    handler.postDelayed(runnable,2000);
                    Toast.makeText(GroupsSecondActivity.this,"签到成功，lnga:"+lngA+"\nlata:"+latA+"\n距离："+eachInt,Toast.LENGTH_SHORT).show();
                    new FetchItemTask().execute();
                }


            }
        });

        //测试功能
//        myTryBtn = (Button) findViewById(R.id.myTryBtn);
//        myTryBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (startBtn.getText()=="不能签到"){
//                    mClipLeftImageView.setVisibility(View.VISIBLE);
//                    attendedNumText.setVisibility(View.VISIBLE);
//                    startBtn.setBackgroundResource(R.drawable.button_groups_circular);
//                    startBtn.setText("点击签到");
//                    startBtn.setEnabled(true);
//                }else {
//                    mClipLeftImageView.setVisibility(View.GONE);
//                    attendedNumText.setVisibility(View.GONE);
//                    startBtn.setBackgroundResource(R.drawable.button_group_false_circular);
//                    startBtn.setText("不能签到");
//                    startBtn.setEnabled(false);
//                }
//            }
//        });


    }

    public void outGroup(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Log.d("tuiqun", "onSuccess: ");
                    GroupAPI groupAPI = new GroupAPI();
                    groupAPI.exitGroup(gId, mId, new Callback() {
                        @Override
                        public void onSuccess(String msg) {
                            Log.d("tuiqun", "onSuccess: "+msg);
                        }

                        @Override
                        public void onFail(String msg) {
                            Log.d("tuiqun", "onfail: "+msg);
                        }
                    });
                }catch (Exception e){
                    Log.d("tuiqun", "onSuccess: "+e);
                }
                Message message = new Message();
                message.what = 2;
                handler2.sendMessage(message);
            }
        }).start();
    }

    public void addMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    GroupAPI groupAPI = new GroupAPI();

                    groupAPI.getGroup(gId, new Callback() {
                        @Override
                        public void onSuccess(String msg) {
                            mGroup = Group.parseJsonString(msg);
                            name = mGroup.getGname();
                            number = String.valueOf(mGroup.getGnumber());
                            Log.d("mytag", "onSuccess: "+name+number+msg);

                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });

                }catch (Exception ioe){
                    Log.d("msg", "doInBackground: "+ioe);
                }
                Message message = new Message();
                message.what = 1;
                handler2.sendMessage(message);
            }
        }).start();
    }

    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{

                SigninAPI signinAPI = new SigninAPI();
                SigninMember signinMember = new SigninMember();
                signinMember.setGid(Integer.valueOf(gId));
                signinMember.setRlogintude(String.valueOf(lngA));
                signinMember.setRlatitude(String.valueOf(latA));
                signinMember.setResult(1);
                signinMember.setReceiver(Integer.valueOf(mId));
                signinAPI.signin(signinMember, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        Log.d("qiandao", "onSuccess: "+msg);
                    }

                    @Override
                    public void onFail(String msg) {
                        Log.d("qiandao", "onFail: "+msg);
                    }
                });


            }catch (Exception ioe){
                Log.d("msg", "doInBackground: "+ioe);
            }
            return null;
        }
    }






    private int mUnmber = 0;
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            if (mUnmber<=3000){
                mClipLeftImageView.getDrawable().setLevel(mUnmber);
                handler.postDelayed(runnable,20);
                mUnmber+=100;
            }
        }
    };

    private void getLocation(){
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        List<String> providerList = mLocationManager.getProviders(true);
        if (providerList.contains(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this,"GPS",Toast.LENGTH_SHORT).show();
            provider = LocationManager.GPS_PROVIDER;
        }else if (providerList.contains(LocationManager.NETWORK_PROVIDER)){
            Toast.makeText(this,"network",Toast.LENGTH_SHORT).show();
            provider = LocationManager.NETWORK_PROVIDER;
        }else {
            Toast.makeText(this, "no location provider to use", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            Location location = mLocationManager.getLastKnownLocation(provider);
            if (location != null){
                showLocation(location);
            }
            mLocationManager.requestLocationUpdates(provider, 5000,1,mLocationListener);
        }catch (SecurityException e){
            Toast.makeText(this, "getLastKnownLocation 出错", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                }else {
                    Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();

                }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager !=null){
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    private void showLocation(final Location location) {
        Log.d("message", "showLocation: start");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String latitude = location.getLatitude() + "";
                    String longitude = location.getLongitude() + "";
                    Log.d("message", "run: " + latitude + "....." + longitude);
                    latA =  location.getLatitude();
                    lngA = location.getLongitude();



                    double a1 = 114.315778;
                    double a2 = 34.82466;
                    double b1 = lngA;
                    double b2 = latA;
                    double s2 = getDistance(a1, a2, b1, b2);
                    Log.d("message", "run: ok"+s2+"\n"+lngA+"===="+latA);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static double rad(double d){
        return d * Math.PI / 180.0;
    }
    public double getDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(a / 2), 2)
                                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)
                )
        );
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
