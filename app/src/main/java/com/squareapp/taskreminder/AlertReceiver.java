package com.squareapp.taskreminder;

/**
 * Created by Valentin Purrucker on 11.08.2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;




public class AlertReceiver extends BroadcastReceiver
{

    private Context context;



    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;
        createNotification(intent, context);
    }









    private void createNotification(Intent alarmIntent, Context context)
    {
        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        DatabaseHandler db = new DatabaseHandler(context);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        int taskID = alarmIntent.getIntExtra("Notification_ID", 1);
        Log.d("AlertReceiver", "Task ID from Intent = " + String.valueOf(taskID));

        SectionOrTask task = new SectionOrTask();
        task = db.getTask(taskID);

        mBuilder.setContentIntent(notificationIntent);
        mBuilder.setSmallIcon(R.drawable.ic_access_alarm_black);
        mBuilder.setContentTitle(task.getName());
        mBuilder.setContentText(task.getCategory());
        mBuilder.setTicker(task.getName());


        mBuilder.setAutoCancel(true);
        mBuilder.setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_ALL);

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(task.getId(), mBuilder.build());



    }
}
