package com.squareapp.taskreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;

public class ActivityForTesting extends AppCompatActivity implements View.OnClickListener, TextWatcher, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener
{

    private DatabaseHandler myDb;

    private Calendar nowCalendar = Calendar.getInstance();

    private ArrayList<String> categoryList;

    private ArrayAdapter<String> spinnerAdapter;

    private String dateStringForDatabase = null;
    private String timeStringForDatabase = null;
    private String taskTitle = null;
    private String taskDescription = null;
    private String taskCategory = null;

    private TextView titleAmnText;

    private EditText titleEditText;
    private EditText timeEditText;
    private EditText dateEditText;
    private EditText colorEditText;
    private EditText descriptionEditText;

    private MaterialBetterSpinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task_test);
        init();
    }


    private void init()
    {
        this.myDb = new DatabaseHandler(this);


        this.titleAmnText = (TextView)findViewById(R.id.titleAmnText);
        this.titleEditText = (EditText)findViewById(R.id.titleEditText);
        this.timeEditText = (EditText)findViewById(R.id.timeEditText);
        this.dateEditText = (EditText)findViewById(R.id.dateEditText);
        this.colorEditText = (EditText)findViewById(R.id.colorEditText);
        this.descriptionEditText = (EditText)findViewById(R.id.descriptionEditText);

        this.categorySpinner = (MaterialBetterSpinner)findViewById(R.id.categorySpinner);

        categoryList = new ArrayList<>();

        categoryList.add("To-Do");
        categoryList.add("Work");
        categoryList.add("Travel");
        categoryList.add("Home");

        this.spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item_custom, categoryList);
        this.categorySpinner.setAdapter(this.spinnerAdapter);

        this.titleEditText.addTextChangedListener(this);
        if(this.titleEditText.length() == 0)
        {
            this.titleEditText.setError(getString(R.string.titleEditTextError));
        }

        this.timeEditText.setOnClickListener(this);
        this.dateEditText.setOnClickListener(this);



        this.dateStringForDatabase = DateFormatClass.setDateToDatabase(nowCalendar);
        this.timeStringForDatabase = DateFormatClass.setTimeToDatabase(nowCalendar);
        this.taskCategory = spinnerAdapter.getItem(0);

        this.dateEditText.setText(DateFormatClass.getDateFromDatePicker(nowCalendar));
        this.timeEditText.setText(DateFormatClass.getTimeFromTimePicker(nowCalendar));







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


    private void getTitleFromEditText()
    {
        this.taskTitle = this.titleEditText.getText().toString();
    }

    private void getDescriptionFromEditText()
    {
        this.taskDescription = this.descriptionEditText.getText().toString();
    }


    private void addTaskToDatabase()
    {
        SectionOrTask task = new SectionOrTask();

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
        this.titleAmnText.setText(String.valueOf(50 - this.titleEditText.length()));
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

        this.dateEditText.setText(DateFormatClass.getDateFromDatePicker(date));
        this.dateStringForDatabase = DateFormatClass.setDateToDatabase(date);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second)
    {
        Calendar time = Calendar.getInstance();

        time.set(Calendar.HOUR_OF_DAY, hourOfDay);
        time.set(Calendar.MINUTE, minute);
        time.set(Calendar.SECOND, 0);


        this.timeEditText.setText(DateFormatClass.getTimeFromTimePicker(time));
        this.timeStringForDatabase = DateFormatClass.setTimeToDatabase(time);

    }


}
