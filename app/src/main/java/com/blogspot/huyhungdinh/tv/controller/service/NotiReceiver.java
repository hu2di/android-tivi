package com.blogspot.huyhungdinh.tv.controller.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.blogspot.huyhungdinh.tv.R;
import com.blogspot.huyhungdinh.tv.controller.database.DBChannelController;
import com.blogspot.huyhungdinh.tv.model.Schedule;
import com.blogspot.huyhungdinh.tv.view.channel.WatchTvActivity;
import com.blogspot.huyhungdinh.tv.view.navi.MainActivity;

/**
 * Created by HUNGDH on 5/25/2016.
 */
public class NotiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Schedule schedule = (Schedule) bundle.get("schedule");
            Notification(context, schedule);

            DBChannelController db = new DBChannelController(context);
            db.deleteSchedule(schedule);
        } catch (Exception e) {
            Log.d("myLog", "Error Receiver: " + e.toString());
        }
    }

    private void Notification(Context context, Schedule schedule) {
        // Set Notification Title
        String strText = schedule.getContent();

        // Open App on Notification Click
        Intent intentOpen = new Intent(context, MainActivity.class);
        /*Bundle bundle = new Bundle();
        bundle.putInt("id", schedule.getIdChannel());
        Log.d("myLog", "id: " + schedule.getId() + "idC: " + schedule.getIdChannel());
        intentOpen.putExtras(bundle);*/

        intentOpen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intentOpen, 0);

        // Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                // Set Icon
                .setSmallIcon(R.drawable.ic_launcher)
                //Set Large Icon
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_live))
                // Set Ticker Message
                .setTicker(strText)
                // Set Title
                .setContentTitle(context.getResources().getString(R.string.app_name))
                // Set Text
                .setContentText(strText)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);
        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(schedule.getId(), builder.build());
    }
}
