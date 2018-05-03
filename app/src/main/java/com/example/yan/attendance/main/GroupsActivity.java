package com.example.yan.attendance.main;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.akria.api.Callback;
import com.akria.api.GroupAPI;
import com.akria.api.SigninAPI;
import com.akria.domain.Group;
import com.akria.domain.Signin;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.ActivityFunction.ToastShow1;
import com.example.yan.attendance.Broadcast.AlarmReceiver;
import com.example.yan.attendance.Db.GroupDb;
import com.example.yan.attendance.MainActivity;
import com.example.yan.attendance.R;
import com.henu.utils.SocketUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.os.Build.VERSION_CODES.O;

public class GroupsActivity extends AppCompatActivity {

    private GroupDb mGroupDb;
    private Group mGroup;
    private TextView mGroupNameText;
    private TextView mNumberText;
    private ImageView backImg;
    private Button startBtn;
    private TextView IdText;
    private LocationManager mLocationManager;
    private String provider;
    public static final int SHOW_LOCATION =0;
    private double lngA, latA, eachlocation;
    private String mId;
    private ListView lsvMore;
    private String gid;


    private final double EARTH_RADIUS = 6378137.0;

    private ImageView popupImg;
    private String[] datas={"查看群成员","查看群信息","解散该群"};
    private Handler handler2 = new Handler(){
        public  void handleMessage(Message msg){
            switch (msg.what){
                case 1:
                    ToastShow1.ToastShow1("解散成功");
//                    finish();
                    break;
                case  2:
                    mGroupNameText.setText(mGroup.getGname());
                    mNumberText.setText(mGroup.getGnumber());
                    IdText.setText("群ID:"+mGroup.getGid());
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            getLocation();
        }

        SharedPreferences shared = BaseActivity.getContext().getSharedPreferences("User", Context.MODE_PRIVATE);
        mId = shared.getString("UserId","-1");



//        mGroupDb =(GroupDb) getIntent().getSerializableExtra("groupInfo");
        mGroup =(Group) getIntent().getSerializableExtra("groupInfo");

        mGroupNameText = (TextView) findViewById(R.id.group_name_text_view);
        mNumberText = (TextView) findViewById(R.id.group_number_text_view);
        IdText = (TextView) findViewById(R.id.Id_text_view);
        if (mGroup == null){
            SharedPreferences mshare = BaseActivity.getContext().getSharedPreferences("SingIn", Context.MODE_PRIVATE);
             gid= mshare.getString("Gid","错误");
             addMsg();


        }else {
            mGroupNameText.setText(mGroup.getGname());
            String numMsg = "该群人数："+mGroup.getGnumber();
            IdText.setText("群ID:"+mGroup.getGid());
//        mGroupNameText.setText(mGroupDb.getName());
//        String numMsg = "该群人数："+mGroupDb.getNumber();
            mNumberText.setText(numMsg);
        }

        View popupView = GroupsActivity.this.getLayoutInflater().inflate(R.layout.groups_popup_window,null);
        lsvMore = (ListView) popupView.findViewById(R.id.popup_list);
        //pop弹出框功能
        //参考链接：http://blog.csdn.net/csdnzouqi/article/details/51433633
        popupImg = (ImageView) findViewById(R.id.popup_image_view);
        popupImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                lsvMore.setAdapter(new ArrayAdapter<String>(GroupsActivity.this, android.R.layout.simple_list_item_1, datas));
                PopupWindow window = new PopupWindow(popupView,400,520);
                window.setAnimationStyle(R.style.popup_window_anim);
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                window.setFocusable(true);
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(popupImg,-300,60);
            }
        });
        lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 2){
                    outGroups();
                }
            }
        });

        backImg = (ImageView) findViewById(R.id.back);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        startBtn = (Button) findViewById(R.id.start_attendance_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new FetchItemTask().execute();
                Intent intent = new Intent(GroupsActivity.this, AlarmReceiver.class);
                intent.setAction("NOTIFICATION");
                PendingIntent pi = PendingIntent.getBroadcast(GroupsActivity.this, 0, intent, 0);
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                int type = AlarmManager.RTC_WAKEUP;
                //new Date()：表示当前日期，可以根据项目需求替换成所求日期
                //getTime()：日期的该方法同样可以表示从1970年1月1日0点至今所经历的毫秒数
                long triggerAtMillis = new Date().getTime();
                long intervalMillis = 1000 * 60;
                manager.set(type,triggerAtMillis+intervalMillis,pi);
//                manager.setInexactRepeating(type, triggerAtMillis, intervalMillis, pi);

                SharedPreferences.Editor editor = BaseActivity.getContext().getSharedPreferences("AttenGroup", Context.MODE_PRIVATE).edit();
                editor.putString("Gid", mGroup.getGid());
                editor.commit();

                Toast.makeText(GroupsActivity.this,"lnga:"+lngA+"lata:"+latA,Toast.LENGTH_SHORT).show();
                startBtn.setEnabled(true);
                startBtn.setBackgroundResource(R.drawable.button_group_false_circular);
                //
            }
        });

    }


    public void addMsg(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    GroupAPI groupAPI = new GroupAPI();

                    groupAPI.getGroup(gid, new Callback() {
                        @Override
                        public void onSuccess(String msg) {
                            mGroup = Group.parseJsonString(msg);
                        }

                        @Override
                        public void onFail(String msg) {

                        }
                    });

                }catch (Exception ioe){
                    Log.d("msg", "doInBackground: "+ioe);
                }
                Message message = new Message();
                message.what = 2;
                handler2.sendMessage(message);
            }
        }).start();
    }

    private void outGroups(){

        Log.d("jiesan", "run: 按时发");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    Log.d("jiesan", "onSuccess: ");
                    GroupAPI groupAPI = new GroupAPI();
                    groupAPI.dissGroup(mGroup.getGid(), new Callback() {
                        @Override
                        public void onSuccess(String msg) {
                            Log.d("jiesan", "onSuccess: "+msg);
                        }

                        @Override
                        public void onFail(String msg) {
                            Log.d("jiesan", "onFail: "+msg);
                        }
                    });
                }catch (Exception e){
                    Log.d("jiesan", "run: "+e);
                }
//                Message message = new Message();
//                message.what = 1;
//                handler2.sendMessage(message);
            }
        }).start();
    }
    private class FetchItemTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Signin signin = new Signin();
                signin.setGid(Integer.parseInt(mGroup.getGid()));
                signin.setOriginator(Integer.parseInt(mId));
                signin.setLatitude(String.valueOf(latA));
                signin.setLogintude(String.valueOf(lngA));
                signin.setRegion("100");
                Log.d("youmeiyou", "doInBackground: "+mGroup.getGid()+mId+latA+lngA);
                SigninAPI sign = new SigninAPI();
                sign.createSignin(signin, new Callback() {
                    @Override
                    public void onSuccess(String msg) {
                        SharedPreferences.Editor editor = BaseActivity.getContext().getSharedPreferences("StartAttend", Context.MODE_PRIVATE).edit();
                        editor.putString("Gid", Signin.parseJson(msg).getGid().toString());
                        editor.commit();
//                        String fromDate = simpleFormat.format("2016-05-01 12:00");
//                        String toDate = simpleFormat.format("2016-05-01 12:50");
//                        long from = simpleFormat.parse(fromDate).getTime();
//                        long to = simpleFormat.parse(toDate).getTime();
//                        int minutes = (int) ((to - from)/(1000 * 60));
                        Log.d("signin", "onSuccess: ok"+Signin.parseJson(msg).getGid().toString());
                    }

                    @Override
                    public void onFail(String msg) {

                    }
                });

            }catch (Exception ioe){
                Log.d("msg", "doInBackground: "+ioe);
            }
            return null;
        }
    }

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
                    latA = location.getLatitude();
                    lngA = location.getLongitude();



                    double a1 = 114.318815;
                    double a2 = 34.82418;
                    double b1 = 114.359347;
                    double b2 = 34.777525;
                    double s2 = getDistance(a1, a2, b1, b2);
                    Log.d("message", "run: ok"+s2);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static double rad(double d){
        return d * Math.PI / 180.0;
    }
    public double getDistance(double lng1, double lat1, double lng2, double lat2){
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(
                Math.sqrt(
                        Math.pow(Math.sin(a/2),2)
                                + Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)
                )
        );
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
