package com.squareapp.taskreminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Valentin Purrucker on 23.08.2017.
 */

public class DateFormatClass
{


    private static final SimpleDateFormat databaseFormat_Date = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat userForamt_Date = new SimpleDateFormat("E, MMM dd, yyyy");
    private static final SimpleDateFormat databaseFormat_Time = new SimpleDateFormat("HH:mm");



    public static String getDateFromDatabase(String date)
    {
        String userDate;

        Calendar userDateCalendar = Calendar.getInstance();

        try {
            userDateCalendar.setTime(databaseFormat_Date.parse(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        userDate = userForamt_Date.format(userDateCalendar.getTime());

        return userDate;

    }

    public static String getDateFromDatePicker(Calendar dateCalendar)
    {
        String date = null;

        date = userForamt_Date.format(dateCalendar.getTime());


        return date;
    }

    public static String setDateToDatabase(Calendar dateCalendar)
    {
        String date = null;

        date = databaseFormat_Date.format(dateCalendar.getTime());


        return date;
    }






    public static String getTimeFromDatabase(String time)
    {

        String userTime = null;

        Calendar timeCalendar = Calendar.getInstance();
        try
        {
            timeCalendar.setTime(databaseFormat_Time.parse(time));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        userTime = databaseFormat_Time.format(timeCalendar.getTime());

        return userTime;





    }

    public static String getTimeFromTimePicker(Calendar timeCalendar)
    {
        String time;

        time = databaseFormat_Time.format(timeCalendar.getTime());


        return time;
    }


    public static String setTimeToDatabase(Calendar timeCalendar)
    {
        String time = null;

        time = databaseFormat_Time.format(timeCalendar.getTime());

        return time;
    }



}
