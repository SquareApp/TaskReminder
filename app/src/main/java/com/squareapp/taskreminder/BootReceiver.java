package com.squareapp.taskreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

        ArrayList<SectionOrTask> taskList = new ArrayList<>();
        taskList = myDb.getAllTasks(0);
        myDb.close();



        for(int i = 0; i < taskList.size(); i++)
        {
            Intent alarmIntent = new Intent(context, AlertReceiver.class);
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            alarmIntent.putExtra("Notification_ID", taskList.get(i).getId());
            alarmManager.set(AlarmManager.RTC_WAKEUP, getStringInLong(taskList.get(i)),
                    PendingIntent.getBroadcast(context, alarmIntent.getIntExtra("Notification_ID", 0),
                            alarmIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT));


        }








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
