package com.squareapp.taskreminder;

/**
 * Created by Valentin Purrucker on 11.08.2017.
 */

public class ThemeHeader
{


    private int position;
    private String date;


    public static ThemeHeader createThemeHeader(int pos, String date)
    {
        ThemeHeader themeHeader = new ThemeHeader();
        themeHeader.position = pos;
        themeHeader.date = date;

        return themeHeader;
    }



    public int getPosition()
    {
        return position;
    }

    public String getDate()
    {
        return date;
    }




}
