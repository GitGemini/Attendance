package com.example.yan.attendance.Serivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.NotificationCompat;

import com.example.yan.attendance.Db.GroupDb;
import com.example.yan.attendance.R;
import com.example.yan.attendance.main.GroupsSecondActivity;

public class GetNotificationService extends Service {
    public GetNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        getNotification();
        return super.onStartCommand(intent, flags, startId);
    }
    public void getNotification(){
        Bundle bundle = new Bundle();
        GroupDb mgroup = new GroupDb(1,"数据结构",88);
        bundle.putSerializable("groupInfo",mgroup);
        Intent go_attend = new Intent(getApplication(), GroupsSecondActivity.class);
        go_attend.putExtras(bundle);

        PendingIntent pendingIntent =PendingIntent.getActivity(this,0,go_attend, 0);
        NotificationManager manager = (NotificationManager)
                getSystemService(Service.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_logo)
                .setTicker("你有一门课需要签到")
                .setContentTitle("数据结构")
                .setContentText("请在十分钟之内签到")
                .setContentIntent(pendingIntent);

        manager.notify(1,builder.build());

    }
}
