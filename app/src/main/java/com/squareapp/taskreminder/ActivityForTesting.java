package com.squareapp.taskreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityForTesting extends AppCompatActivity implements View.OnClickListener, TextWatcher, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, ColorChooserDialog.ColorCallback
{

    private DatabaseHandler myDb;

    private Toolbar myToolbar;

    private Calendar nowCalendar = Calendar.getInstance();

    private ArrayList<String> categoryList;

    private ArrayAdapter<String> spinnerAdapter;

    private String dateStringForDatabase = null;
    private String timeStringForDatabase = null;
    private String taskTitle = null;
    private String taskDescription = null;
    private String taskCategory = null;

    private int taskColor = 0;

    private TextView titleAmnText;

    private EditText titleEditText;
    private EditText timeEditText;
    private EditText dateEditText;
    private EditText colorEditText;
    private EditText descriptionEditText;

    private ImageView colorIconDrawable;
    private ImageView toolbar_saveIcon;

    private MaterialBetterSpinner categorySpinner;

    private ArrayList<Integer> colorList;

    private Calendar alarmCalendar;

    private int[] colorArray;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_test);
        init();
        initColors();
    }


    private void init()
    {
        this.myDb = new DatabaseHandler(this);

        this.colorList = new ArrayList<>();

        this.alarmCalendar = Calendar.getInstance();



        this.myToolbar = (Toolbar)findViewById(R.id.myToolbar);

        this.titleAmnText = (TextView)findViewById(R.id.titleAmnText);
        this.titleEditText = (EditText)findViewById(R.id.titleEditText);
        this.timeEditText = (EditText)findViewById(R.id.timeEditText);
        this.dateEditText = (EditText)findViewById(R.id.dateEditText);
        this.colorEditText = (EditText)findViewById(R.id.colorEditText);
        this.descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);

        this.categorySpinner = (MaterialBetterSpinner)findViewById(R.id.categorySpinner);

        this.colorIconDrawable = (ImageView)findViewById(R.id.colorIcon);
        this.toolbar_saveIcon = (ImageView)myToolbar.findViewById(R.id.toolbar_saveIcon);


        this.categoryList = new ArrayList<>();

        categoryList.add("To-Do");
        categoryList.add("Work");
        categoryList.add("Travel");
        categoryList.add("Home");

        this.spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, categoryList);
        this.categorySpinner.setAdapter(this.spinnerAdapter);

        this.titleEditText.addTextChangedListener(this);

        this.timeEditText.setOnClickListener(this);
        this.dateEditText.setOnClickListener(this);
        this.colorEditText.setOnClickListener(this);
        this.toolbar_saveIcon.setOnClickListener(this);



        this.dateStringForDatabase = DateFormatClass.setDateToDatabase(nowCalendar);
        this.timeStringForDatabase = DateFormatClass.setTimeToDatabase(nowCalendar);
        this.taskCategory = spinnerAdapter.getItem(0);

        this.dateEditText.setText(DateFormatClass.getDateFromDatePicker(nowCalendar));
        this.timeEditText.setText(DateFormatClass.getTimeFromTimePicker(nowCalendar));








    }

    private void initColors()
    {

        colorList.add(Color.parseColor("#F44336"));
        colorList.add(Color.parseColor("#E91E63"));
        colorList.add(Color.parseColor("#880E4F"));
        colorList.add(Color.parseColor("#9C27B0"));
        colorList.add(Color.parseColor("#673AB7"));
        colorList.add(Color.parseColor("#4A148C"));
        colorList.add(Color.parseColor("#3F51B5"));
        colorList.add(Color.parseColor("#2196F3"));
        colorList.add(Color.parseColor("#03A9F4"));
        colorList.add(Color.parseColor("#00BCD4"));
        colorList.add(Color.parseColor("#009688"));
        colorList.add(Color.parseColor("#4CAF50"));
        colorList.add(Color.parseColor("#8BC34A"));
        colorList.add(Color.parseColor("#CDDC39"));
        colorList.add(Color.parseColor("#FFC107"));
        colorList.add(Color.parseColor("#FF9800"));
        colorList.add(Color.parseColor("#FF5722"));
        colorList.add(Color.parseColor("#795548"));
        colorList.add(Color.parseColor("#9E9E9E"));
        colorList.add(Color.parseColor("#607D8B"));



        this.colorArray = new int[colorList.size()];

        for (int i = 0; i < colorList.size(); i++)
        {
            this.colorArray[i] = colorList.get(i);
        }
    }



    private void openTimePickerDialog()
    {
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,
                nowCalendar.get(Calendar.HOUR_OF_DAY),
                nowCalendar.get(Calendar.MINUTE),
                true);


        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    private void openDatePickerDialog()
    {

        DatePickerDialog dpd = DatePickerDialog.newInstance(this,
                nowCalendar.get(Calendar.YEAR),
                nowCalendar.get(Calendar.MONTH),
                nowCalendar.get(Calendar.DAY_OF_MONTH));

        dpd.show(getFragmentManager(), "Datepickerdialog");
    }


    private void openColorPickerDialog()
    {




        new ColorChooserDialog.Builder(this, R.string.select_colour)
                .accentMode(false)
                .allowUserColorInput(false)
                .customColors(this.colorArray, null)
                .show();


    }


    private void setTaskTitle(String title)
    {
        this.taskTitle = title;
    }

    private void setTaskDescription(String description)
    {
        this.taskDescription = description;
    }

    private void setTaskCategory(String category)
    {
        this.taskCategory = category;
    }

    private void setTaskDate(String date)
    {
        this.dateStringForDatabase = date;
    }

    private void setTaskTime(String time)
    {
        this.timeStringForDatabase = time;
    }

    private void setTaskColor(int color)
    {
        this.taskColor = color;
    }


    private void addTaskToDatabase()
    {

        setTaskTitle(this.titleEditText.getText().toString());
        setTaskDescription(this.descriptionEditText.getText().toString());
        setTaskCategory(this.categorySpinner.getText().toString());


        SectionOrTask task = new SectionOrTask();
        task = SectionOrTask.createTask(taskTitle, taskCategory, taskDescription, 0, 0, this.dateStringForDatabase, this.timeStringForDatabase, this.taskColor);


        myDb.addTask(task);
        scheduleAlarmForTask();
        Log.d("AddTask", "Success");
        SectionOrTask sectionOrTask = myDb.getTask(myDb.task_ID);
        Log.d("Task", sectionOrTask.getName());


    }


    private void scheduleAlarmForTask()
    {
        int taskID = myDb.task_ID;
        myDb.close();

        long alertTime = alarmCalendar.getTimeInMillis();


        Intent alarmReceiverIntent = new Intent(this, AlertReceiver.class);
        alarmReceiverIntent.putExtra("Notification_ID", taskID);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, alarmReceiverIntent.getIntExtra("Notification_ID", 0), alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        finish();



    }




    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id)
        {


            case R.id.timeEditText:
                openTimePickerDialog();
                break;

            case R.id.dateEditText:
                openDatePickerDialog();
                break;

            case R.id.colorEditText:
                openColorPickerDialog();
                break;

            case R.id.toolbar_saveIcon:
                addTaskToDatabase();
                break;


        }
    }




    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if(this.titleEditText.length() == 0)
        {
            this.titleEditText.setError(getString(R.string.titleEditTextError));
        }
        this.titleAmnText.setText(String.valueOf(this.titleEditText.length() + "/70"));
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }




    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        alarmCalendar.set(Calendar.YEAR, year);
        alarmCalendar.set(Calendar.MONTH, monthOfYear);
        alarmCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        this.dateEditText.setText(DateFormatClass.getDateFromDatePicker(date));
        setTaskDate(DateFormatClass.setDateToDatabase(date));
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second)
    {
        Calendar time = Calendar.getInstance();

        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
        time.set(Calendar.MINUTE, minute);
        time.set(Calendar.SECOND, 0);

        alarmCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        alarmCalendar.set(Calendar.MINUTE, minute);
        alarmCalendar.set(Calendar.SECOND, 0);


        this.timeEditText.setText(DateFormatClass.getTimeFromTimePicker(time));
        setTaskTime(DateFormatClass.setTimeToDatabase(time));

    }


    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor)
    {
        this.colorEditText.setText(String.format("#%06X", 0xFFFFFF & selectedColor));
        this.colorEditText.setTextColor(Color.parseColor(String.format("#%06X", 0xFFFFFF & selectedColor)));
        this.colorIconDrawable.setColorFilter(Color.parseColor(String.format("#%06X", 0xFFFFFF & selectedColor)));
        Log.d("Color", String.valueOf(selectedColor));
        setTaskColor(selectedColor);
    }
}
