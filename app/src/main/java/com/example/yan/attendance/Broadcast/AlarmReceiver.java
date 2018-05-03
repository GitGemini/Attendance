package com.example.yan.attendance.Broadcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.yan.attendance.R;
import com.example.yan.attendance.main.DownLoadActivity;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1000;
    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        ShowAlarm(intent);

    }
    private void ShowAlarm(Intent intent){
        if (intent.getAction().equals("NOTIFICATION")) {
            NotificationManager manager = (NotificationManager) mContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            Intent intent2 = new Intent(mContext, DownLoadActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent2, 0);
            Notification notify = new Notification.Builder(mContext)
                    .setSmallIcon(R.drawable.head)
                    .setTicker("您发起的签到已经结束，请及时处理！")
                    .setContentTitle("签到结束提醒")
                    .setStyle(new Notification.BigTextStyle().bigText("你有一项发起的签到结果已经生成，请点击查看"))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setNumber(1).build();
            manager.notify(NOTIFICATION_ID, notify);
        }
    }
}
