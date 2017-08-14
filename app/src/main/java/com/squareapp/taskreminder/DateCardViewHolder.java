package com.squareapp.taskreminder;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Valentin Purrucker on 03.08.2017.
 */

class DateCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener
{


    private android.app.FragmentManager fragmentManager;

    private CardView cardView;

    public EditText timeEt;
    public EditText dateEt;

    public String actualDate = null;
    public String databaseFormattedDate = null;
    public String databaseFormattedTime = null;

    public Calendar eventCalendar = null;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
    private SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");


    private TextView notificationTextView;





    public DateCardViewHolder(View itemView, android.app.FragmentManager fragmentManager)
    {
        super(itemView);

        this.fragmentManager = fragmentManager;

        eventCalendar = Calendar.getInstance();

        cardView = (CardView)itemView.findViewById(R.id.dateCardView);

        timeEt = (EditText)itemView.findViewById(R.id.timeEditText);
        dateEt = (EditText)itemView.findViewById(R.id.dateEditText);

        notificationTextView = (TextView)itemView.findViewById(R.id.notificationTextView);
        notificationTextView.setPaintFlags(notificationTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        timeEt.getBackground().mutate().setColorFilter(Color.parseColor("#15a48c"), PorterDuff.Mode.SRC_ATOP);
        dateEt.getBackground().mutate().setColorFilter(Color.parseColor("#15a48c"), PorterDuff.Mode.SRC_ATOP);

        timeEt.setOnClickListener(this);
        dateEt.setOnClickListener(this);



        dateEt.setText("Today");

        this.databaseFormattedDate = databaseFormat.format(eventCalendar.getTime());
        this.databaseFormattedTime = timeFormat.format(eventCalendar.getTime());
        timeEt.setText("Now");

        Log.d("Date and Time", databaseFormattedDate + " " + databaseFormattedTime);





    }








    private void showTimeFragment()
    {
        eventCalendar = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                DateCardViewHolder.this,
                eventCalendar.get(Calendar.HOUR_OF_DAY),
                eventCalendar.get(Calendar.MINUTE),
                true
        );

        tpd.show(fragmentManager, "Timepickerdialog");

    }


    private void showDateFragment()
    {
        eventCalendar = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                DateCardViewHolder.this,
                eventCalendar.get(Calendar.YEAR),
                eventCalendar.get(Calendar.MONTH),
                eventCalendar.get(Calendar.DAY_OF_MONTH)
        );


        dpd.show(fragmentManager, "Datepickerdialog");

    }








//Override methods

    @Override
    public void onClick(View v)
    {
        int id = v.getId();


        switch (id)
        {
            case R.id.timeEditText:
                showTimeFragment();

                break;

            case R.id.dateEditText:
                showDateFragment();
                break;
        }
    }







    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth)
    {
        String dateString;
        String actualDate;
        String dateForamt;




        this.actualDate = String.valueOf(dayOfMonth) + "." + String.valueOf(monthOfYear) + "." + String.valueOf(year);
        dateString = String.valueOf(dayOfMonth) + "." + String.valueOf(monthOfYear +1) + "." + String.valueOf(year);


        this.eventCalendar.set(Calendar.YEAR, year);
        this.eventCalendar.set(Calendar.MONTH, monthOfYear);
        this.eventCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);




        String date = simpleDateFormat.format(this.eventCalendar.getTime());
        this.databaseFormattedDate = databaseFormat.format(this.eventCalendar.getTime());
        Log.d("onDateSet", databaseFormattedDate);
        this.actualDate = date;


        dateEt.setText(date);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second)
    {
        String timeString;




        eventCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        eventCalendar.set(Calendar.MINUTE, minute);
        eventCalendar.set(Calendar.SECOND, 0);


        this.databaseFormattedTime = timeFormat.format(eventCalendar.getTime());


        timeEt.setText(databaseFormattedTime);
    }
}
