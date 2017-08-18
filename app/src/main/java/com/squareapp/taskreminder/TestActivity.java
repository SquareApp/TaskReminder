package com.squareapp.taskreminder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestActivity extends AppCompatActivity
{

    private SimpleDateFormat databaseformat = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat toUserFormat = new SimpleDateFormat("E, MMM dd, yyyy");

    private DatabaseHandler myDb;

    private CardView taskCardItem;

    private CheckBox taskStatusCheckbox;

    private TextView taskNameText;
    private TextView taskCategoryText;
    private TextView taskCategoryIconText;
    private TextView taskDateText;
    private TextView taskTimeText;
    private TextView taskDescriptionText;
    private TextView taskRepeitionText;


    private Typeface fontawesomeTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        setupTransitions();
        init();
        setTaskData(getIntent().getIntExtra("Task_ID", 1));
    }


    private void init()
    {

        this.myDb = new DatabaseHandler(this);

        fontawesomeTypeface = FontCache.get("fonts/fontawesome-webfont.ttf", this);

        this.taskCardItem = (CardView)findViewById(R.id.taskCardItem);

        this.taskStatusCheckbox = (CheckBox)taskCardItem.findViewById(R.id.statusCheckBox);

        this.taskNameText = (TextView)taskCardItem.findViewById(R.id.taskName);
        this.taskCategoryText = (TextView)taskCardItem.findViewById(R.id.taskCategoryText);
        this.taskCategoryIconText = (TextView)taskCardItem.findViewById(R.id.taskCategoryIconText);
        taskCategoryIconText.setTypeface(fontawesomeTypeface);
        this.taskDateText = (TextView)findViewById(R.id.dateTaskText);
        this.taskTimeText = (TextView)findViewById(R.id.timeTaskText);
        this.taskDescriptionText = (TextView)findViewById(R.id.descriptionTaskText);
        this.taskRepeitionText = (TextView)findViewById(R.id.repetitionTaskText);



    }


    private void setTaskData(int taskID)
    {
        SectionOrTask task = myDb.getTask(taskID);

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
            taskCalendar.setTime(databaseformat.parse(taskDate));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }


        this.taskNameText.setText(taskName);
        this.taskCategoryText.setText(taskCategory);
        this.taskCategoryIconText.setText(getString(R.string.fa_icon_tag));
        this.taskDescriptionText.setText(taskDescription);
        this.taskDateText.setText(toUserFormat.format(taskCalendar.getTime()));
        this.taskTimeText.setText(taskTime);

        if(task.getStatus() == 0)
        {
            this.taskStatusCheckbox.setChecked(false);
        }
        else
        {
            this.taskStatusCheckbox.setChecked(true);
        }

        setBackground(task);


    }


    private void setBackground(SectionOrTask task)
    {
        if(task.getCategory().equals("Work"))
        {

            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.parseColor(getString(R.string.colorWorkStart)), Color.parseColor(getString(R.string.colorWorkEnd))});
            gd.setCornerRadius(2f);
            this.taskCardItem.setBackground(gd);
        }


        if(task.getCategory().equals("Travel"))
        {

            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.parseColor(getString(R.string.colorTravelStart)), Color.parseColor(getString(R.string.colorTravelEnd))});
            gd.setCornerRadius(2f);
            this.taskCardItem.setBackground(gd);
        }

        if(task.getCategory().equals("Home"))
        {

            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.parseColor(getString(R.string.colorHomeStart)), Color.parseColor(getString(R.string.colorHomeEnd))});
            gd.setCornerRadius(2f);
            this.taskCardItem.setBackground(gd);
        }

        if(task.getCategory().equals("To-Do"))
        {

            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.parseColor(getString(R.string.colorTodoStart)), Color.parseColor(getString(R.string.colorTodoEnd))});
            gd.setCornerRadius(2f);
            this.taskCardItem.setBackground(gd);
        }
    }


    private void setupTransitions()
    {



        TransitionSet enterSet = new TransitionSet();

        Transition slideHeaderEnter = new Slide(Gravity.TOP);
        slideHeaderEnter.setDuration(500);
        slideHeaderEnter.addTarget(R.id.header);
        slideHeaderEnter.excludeTarget(android.R.id.navigationBarBackground, true);
        slideHeaderEnter.excludeTarget(android.R.id.statusBarBackground, true);
        slideHeaderEnter.excludeTarget(R.id.test, true);

        enterSet.addTransition(slideHeaderEnter);

        Transition slideTaskLayoutEnter = new Slide(Gravity.BOTTOM);
        slideTaskLayoutEnter.addTarget(R.id.test);
        slideTaskLayoutEnter.setDuration(500);

        enterSet.addTransition(slideTaskLayoutEnter);


        this.getWindow().setEnterTransition(enterSet);



        TransitionSet setReturn = new TransitionSet();

        Transition toolbarSlide = new Slide(Gravity.TOP);
        toolbarSlide.setDuration(570);
        toolbarSlide.addTarget(R.id.header);
        toolbarSlide.excludeTarget(android.R.id.navigationBarBackground, true);
        toolbarSlide.excludeTarget(android.R.id.statusBarBackground, true);
        toolbarSlide.excludeTarget(R.id.test, true);


        setReturn.addTransition(toolbarSlide);


        Transition slideTaskLayoutExit = new Slide(Gravity.BOTTOM);
        slideTaskLayoutExit.addTarget(R.id.test);
        slideTaskLayoutExit.setDuration(280);

        setReturn.addTransition(slideTaskLayoutExit);






        this.getWindow().setReturnTransition(setReturn);
        getWindow().setSharedElementsUseOverlay(true);





    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
