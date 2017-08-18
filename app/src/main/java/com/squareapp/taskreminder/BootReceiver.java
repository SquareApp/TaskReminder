package com.squareapp.taskreminder;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Valentin Purrucker on 13.08.2017.
 */

public class BootReceiver extends BroadcastReceiver
{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH:mm", Locale.getDefault());

    @Override
    public void onReceive(Context context, Intent intent)
    {

        Log.d("BootReceiver", "Received");

        DatabaseHandler myDb = new DatabaseHandler(context);

        Calendar now = Calendar.getInstance();


        ArrayList<SectionOrTask> taskList = new ArrayList<>();
        taskList = myDb.getAllTasks(0);
        myDb.close();



        for(int i = 0; i < taskList.size(); i++)
        {
            Calendar time = Calendar.getInstance();
            String timeString = myDb.getTask(i).getDate() + ":" + myDb.getTask(i).getTime();
            try
            {
                time.setTime(dateFormat.parse(timeString));
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            if(time.getTimeInMillis() < now.getTimeInMillis())
            {
                createNotification(context, taskList.get(i), myDb);
            }
            else
            {
                Intent alarmIntent = new Intent(context, AlertReceiver.class);
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                alarmIntent.putExtra("Notification_ID", taskList.get(i).getId());
                alarmManager.set(AlarmManager.RTC_WAKEUP, getStringInLong(taskList.get(i)),
                        PendingIntent.getBroadcast(context, alarmIntent.getIntExtra("Notification_ID", 0),
                                alarmIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT));
            }



            myDb.close();

        }








    }

    private void createNotification(Context context, SectionOrTask task, DatabaseHandler myDb)
    {



        PendingIntent viewActivityIntent = PendingIntent.getActivity(context, 0, new Intent(context, ViewActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

        if(task.getStatus() == 0)
        {
            task.setStatus(1);
        }
        else
        {
            task.setStatus(0);
        }


        myDb.updateTask(task);

        mBuilder.setSmallIcon(R.drawable.ic_access_alarm_black);
        mBuilder.setTicker(task.getName());
        mBuilder.setContentTitle(task.getName());
        mBuilder.setContentText(task.getCategory());
        mBuilder.setContentIntent(viewActivityIntent);
        mBuilder.setColor(Color.rgb(16,171,110));

        mBuilder.setAutoCancel(true);

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(task.getId(), mBuilder.build());


    }


    private long getStringInLong(SectionOrTask task)
    {
        Calendar alarmCalendar = Calendar.getInstance();

        String calendarInString = task.getDate() + task.getTime();

        long alertTime = 0;

        try
        {
            alarmCalendar.setTime(dateFormat.parse(calendarInString));

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        alertTime = alarmCalendar.getTimeInMillis();

        return alertTime;
    }
}
