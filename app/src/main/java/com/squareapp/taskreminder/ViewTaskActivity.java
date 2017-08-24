package com.squareapp.taskreminder;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewTaskActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener
{

    private SimpleDateFormat databaseDateformat = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat databaseTimeFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat toUserFormat = new SimpleDateFormat("E, MMM dd, yyyy");

    private DatabaseHandler myDb;

    private CardView taskCardItem;

    private Toolbar myToolbar;

    private ImageView editIcon;
    private ImageView deleteIcon;
    private ImageView backIcon;

    private CheckBox taskStatusCheckbox;

    private TextView taskNameText;
    private TextView taskCategoryText;
    private TextView taskCategoryIconText;
    private TextView taskDateText;
    private TextView taskTimeText;
    private TextView taskDaysLeftText;
    private TextView taskDescriptionText;
    private TextView taskRepeitionText;

    private LinearLayout taskCardBackground;

    private Typeface fontawesomeTypeface;

    private Resources resources;

    private SectionOrTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setupTransitions();
        init();

        setTaskData();
    }


    private void init()
    {

        this.myDb = new DatabaseHandler(this);

        this.task = myDb.getTask(getIntent().getIntExtra("Task_ID", 1));

        fontawesomeTypeface = FontCache.get("fonts/fontawesome-webfont.ttf", this);

        this.taskCardItem = (CardView)findViewById(R.id.taskCardItem);
        this.taskCardItem.setOnLongClickListener(this);

        this.myToolbar = (Toolbar)findViewById(R.id.toolbar);
        this.editIcon = (ImageView)myToolbar.findViewById(R.id.toolbar_editTaskIcon);
        this.editIcon.setOnClickListener(this);
        this.deleteIcon = (ImageView)myToolbar.findViewById(R.id.toolbar_deleteTaskIcon);
        this.deleteIcon.setOnClickListener(this);
        this.backIcon = (ImageView)myToolbar.findViewById(R.id.toolbar_backIcon);
        this.backIcon.setOnClickListener(this);

        this.taskCardBackground = (LinearLayout)findViewById(R.id.taskCardBackground);

        this.taskStatusCheckbox = (CheckBox)taskCardItem.findViewById(R.id.statusCheckBox);

        this.taskNameText = (TextView)taskCardItem.findViewById(R.id.taskName);
        this.taskCategoryText = (TextView)taskCardItem.findViewById(R.id.taskCategoryText);
        this.taskCategoryIconText = (TextView)taskCardItem.findViewById(R.id.taskCategoryIconText);
        this.taskDaysLeftText = (TextView)taskCardItem.findViewById(R.id.taskDaysLeftText);
        taskCategoryIconText.setTypeface(fontawesomeTypeface);
        this.taskDateText = (TextView)findViewById(R.id.dateTaskText);
        this.taskTimeText = (TextView)findViewById(R.id.timeTaskText);
        this.taskDescriptionText = (TextView)findViewById(R.id.descriptionTaskText);
        this.taskRepeitionText = (TextView)findViewById(R.id.repetitionTaskText);

        this.resources = getResources();



    }


    private void setTaskData()
    {


        String taskName;
        String taskCategory;
        String taskDescription;
        String taskDate;
        String taskTime;
        String taskRepetition;

        Calendar taskCalendar = Calendar.getInstance();




        taskName = task.getName();
        taskCategory = task.getCategory();
        taskDescription = task.getDescription();
        taskDate = task.getDate();
        taskTime = task.getTime();

        if(taskDescription.length() == 0)
        {
            taskDescription = "No further description";
        }


        try
        {
            taskCalendar.setTime(databaseDateformat.parse(taskDate));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        this.taskNameText.setText(taskName);
        this.taskCategoryText.setText(taskCategory);
        this.taskCategoryIconText.setText(getString(R.string.fa_icon_tag));
        this.taskCategoryText.setTextColor(setTaskTextColor(task));
        this.taskCategoryIconText.setTextColor(setTaskTextColor(task));
        this.taskDescriptionText.setText(taskDescription);
        this.taskDateText.setText(toUserFormat.format(taskCalendar.getTime()));
        this.taskTimeText.setText(taskTime);
        this.taskDaysLeftText.setText(setDaysLeft(taskDate));
        this.taskRepeitionText.setText("Just once");



        if(task.getStatus() == 0)
        {
            this.taskStatusCheckbox.setChecked(false);
        }
        else
        {
            this.taskStatusCheckbox.setChecked(true);
        }

        setTaskCardColors(task);
        //this.taskCardBackground.setBackground(setLayoutBackground(task));


        Log.d("TestActivity", "Time " + taskTime);


    }




    private int setTaskTextColor(SectionOrTask task)
    {


        int colorCode;

  /*
        myDb.addColor("#781054", "Travel");
        myDb.addColor("#1d976c", "Work");
        myDb.addColor("#1d5e97", "Home");
        myDb.addColor("#0065ab", "To-Do");

   */



        colorCode = task.getColorCode();

        return colorCode;


    }


    private String setDaysLeft(String date)
    {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        Calendar taskDateCalendar = Calendar.getInstance();

        String daysLeftString = null;

        try
        {
            taskDateCalendar.setTime(databaseDateformat.parse(date));
            Log.d("TestActivity", "Parse date -> Success");
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }




        long diff = taskDateCalendar.getTimeInMillis() - now.getTimeInMillis();

        float dayCount = (float) diff / (24.0f * 60.0f * 60.0f * 1000.0f);


        if(dayCount < 1 && dayCount > 0)
        {
            dayCount = 1;
        }

        if(dayCount < 0 && dayCount >= -1)
        {
            dayCount = -1;
        }




        Log.d("TestActivity", "Exact value: " + String.valueOf(dayCount));
        dayCount = Math.round(dayCount *100) / 100;


        if((Math.round(dayCount * 100) / 100) == 1)
        {
            dayCount = Math.round(dayCount * 100) / 100;
            daysLeftString = String.valueOf((int)dayCount) + " Day left";
        }

        if((Math.round(dayCount * 100) / 100) > 1)
        {
            dayCount = Math.round(dayCount * 100) / 100;
            daysLeftString = String.valueOf((int)dayCount) + " Days left";
        }


        if((Math.round(dayCount * 100) / 100) == -1)
        {
            dayCount = Math.abs(Math.round(dayCount * 100) / 100);
            daysLeftString = String.valueOf((int)dayCount) + " Day ago";
        }

        if((Math.round(dayCount * 100) / 100) < -1)
        {
            dayCount = Math.abs(Math.round(dayCount * 100) / 100);
            daysLeftString = String.valueOf((int)dayCount) + " Days ago";
        }


        return daysLeftString;

    }


    private void setTaskCardColors(SectionOrTask task)
    {


  /*
        myDb.addColor("#781054", "Travel");
        myDb.addColor("#1d976c", "Work");
        myDb.addColor("#1d5e97", "Home");
        myDb.addColor("#0065ab", "To-Do");

   */

        taskCardBackground.setBackgroundColor(task.getColorCode());

    }


    private float setCardViewCornerRadius(int radiusDP)
    {

        float cornerRadius = 0;

        cornerRadius = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                radiusDP,
                resources.getDisplayMetrics()
        );



        return cornerRadius;

    }


    private GradientDrawable setLayoutBackground(SectionOrTask task)
    {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor("#263238"));
        return gradientDrawable;
    }


    private void setupTransitions()
    {



        TransitionSet enterSet = new TransitionSet();

        Transition slideHeaderEnter = new Slide(Gravity.TOP);
        slideHeaderEnter.setDuration(300);
        slideHeaderEnter.addTarget(R.id.header);
        slideHeaderEnter.excludeTarget(android.R.id.navigationBarBackground, true);
        slideHeaderEnter.excludeTarget(android.R.id.statusBarBackground, true);
        slideHeaderEnter.excludeTarget(R.id.taskInfoLayout, true);

        enterSet.addTransition(slideHeaderEnter);

        Transition slideTaskLayoutEnter = new Slide(Gravity.BOTTOM);
        slideTaskLayoutEnter.addTarget(R.id.taskInfoLayout);
        slideTaskLayoutEnter.setDuration(300);

        enterSet.addTransition(slideTaskLayoutEnter);


        this.getWindow().setEnterTransition(enterSet);



        TransitionSet setReturn = new TransitionSet();

        Transition toolbarSlide = new Slide(Gravity.TOP);
        toolbarSlide.setDuration(300);
        toolbarSlide.addTarget(R.id.header);
        toolbarSlide.excludeTarget(android.R.id.navigationBarBackground, true);
        toolbarSlide.excludeTarget(android.R.id.statusBarBackground, true);
        toolbarSlide.excludeTarget(R.id.taskInfoLayout, true);


        setReturn.addTransition(toolbarSlide);


        Transition slideTaskLayoutExit = new Slide(Gravity.BOTTOM);
        slideTaskLayoutExit.addTarget(R.id.taskInfoLayout);
        slideTaskLayoutExit.setDuration(300);

        setReturn.addTransition(slideTaskLayoutExit);






        this.getWindow().setReturnTransition(setReturn);
        getWindow().setSharedElementsUseOverlay(true);





    }



    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v)
    {

        int id = v.getId();


        switch (id)
        {
            case R.id.toolbar_backIcon:
                onBackPressed();
                break;


        }



    }

    @Override
    public boolean onLongClick(View v)
    {
        int id = v.getId();

        switch (id)
        {
            case R.id.taskCardItem:

                if(this.task.getStatus() == 1)
                {
                    this.taskStatusCheckbox.setChecked(false);
                    this.task.setStatus(0);

                }
                else
                {
                    this.taskStatusCheckbox.setChecked(true);
                    this.task.setStatus(1);

                }


                myDb.updateTask(task);
                break;
        }

        return false;



    }
}
