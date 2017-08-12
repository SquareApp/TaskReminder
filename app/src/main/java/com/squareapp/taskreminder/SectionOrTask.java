package com.squareapp.taskreminder;

/**
 * Created by Valentin Purrucker on 25.07.2017.
 */

public class SectionOrTask
{


    private String section;
    private String name;
    private String category;
    private String date;
    private String time;

    private int status;
    private int id;

    private boolean isTask;




    public static SectionOrTask createSection(String section)
    {
        SectionOrTask sec = new SectionOrTask();
        sec.section = section;
        sec.isTask = false;

        return sec;
    }



    public static SectionOrTask createTask(String name, String category, int status, int id, String date, String time)
    {
        SectionOrTask task = new SectionOrTask();
        task.name = name;
        task.category = category;
        task.status = status;
        task.isTask = true;
        task.id = id;
        task.date = date;
        task.time = time;

        return task;
    }



    public String getSection()
    {
        return section;
    }


    public boolean isTask()
    {
        return isTask;
    }

    public String getName()
    {
        return name;
    }

    public String getCategory()
    {
        return category;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return status;
    }


    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }
}

