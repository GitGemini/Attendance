package com.example.yan.attendance.Broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.akria.domain.Group;
import com.example.yan.attendance.ActivityFunction.BaseActivity;
import com.example.yan.attendance.MainActivity;
import com.example.yan.attendance.R;
import com.example.yan.attendance.main.GroupsSecondActivity;

public class ServiceReceiver extends BroadcastReceiver {

    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        this.mContext = context;
        showNotification(intent);
        Log.d("ServiceReceiver", "onReceive: 接受到广播");

    }
    private void showNotification(Intent intent){
        Log.d("showNotification", "showNotification: "+intent);
        SharedPreferences shared = BaseActivity.getContext().getSharedPreferences("SingIn", Context.MODE_PRIVATE);
        String gId = shared.getString("Gid","错误");
        String Latitude = shared.getString("Latitude","");
        String Logintude = shared.getString("Logintude","");
        Log.d("service", "showNotification: "+gId+Latitude+Logintude);
        Intent intent1[] = new Intent[1];
//        Bundle bundle = new Bundle();
//        Group group = new Group();
//        group.setGid(gId);
//        group.setGname("myname");
//        group.setGnumber(3);
//        bundle.putSerializable("groupInfo",group);

//        bundle.putSerializable("groupInfo", gro1.get(childPosition));
//        Intent to_attend_group = new Intent(getContext(),GroupsSecondActivity.class);
//
//        to_attend_group.putExtras(bundle);
//        startActivity(to_attend_group);

        intent1[0] = new Intent(mContext, GroupsSecondActivity.class);
//        intent1[0].putExtra("gId",gId);
//        intent1[0].putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivities(mContext, 0, intent1,0);

//        Group group = new Group();
//        group.setGid(gId);
//        group.setGname(shared.getString(""));
        Log.d("showNotification", "showNotification: "+gId+Latitude+Logintude);
        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti= new Notification.Builder(mContext)
                .setContentTitle("签到信息")
                .setContentText(gId+"群需要签到,Lat:"+Latitude+"Log:"+Logintude)
                .setSmallIcon(R.drawable.head)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(1,noti);
    }
}
