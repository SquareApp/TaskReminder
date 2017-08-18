package com.squareapp.taskreminder;

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
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewActivity extends AppCompatActivity implements View.OnClickListener
{

    private DatabaseHandler myDb;

    private CardView taskCard;

    private TextView taskNameText;
    private TextView taskCategoryText;
    private TextView taskCategoryIconText;
    private TextView taskTimeText;
    private TextView taskDateText;
    private TextView taskRepetitionText;
    private TextView taskDescriptionText;

    private CheckBox taskCheckbox;


    private LinearLayout headerLayout;



    private ImageView toolbar_backIcon;
    private ImageView toolbar_deleteIcon;
    private ImageView toolbar_editIcon;


    private Toolbar myToolbar;

    private Typeface typeface;

    private String time;
    private String date;
    private String description;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
    private SimpleDateFormat calendarFormat = new SimpleDateFormat("yyyyMMdd");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setupTransitions();



        myDb = new DatabaseHandler(this);

        typeface = FontCache.get("fonts/fontawesome-webfont.ttf", this);

        //Toolbar

        myToolbar = (Toolbar)findViewById(R.id.toolbar);

        taskCard = (CardView) findViewById(R.id.taskCardItem);

        taskNameText = (TextView)taskCard.findViewById(R.id.taskName);
        taskCategoryText = (TextView)taskCard.findViewById(R.id.taskCategoryText);
        taskCategoryIconText = (TextView)taskCard.findViewById(R.id.taskCategoryIconText);


        taskTimeText = (TextView)findViewById(R.id.timeTaskText);
        taskDateText = (TextView)findViewById(R.id.dateTaskText);
        taskRepetitionText = (TextView)findViewById(R.id.repetitionTaskText);
        taskDescriptionText = (TextView)findViewById(R.id.descriptionTaskText);



        taskCheckbox = (CheckBox)taskCard.findViewById(R.id.statusCheckBox);



        headerLayout = (LinearLayout)findViewById(R.id.headerLayout);
        headerLayout.setTransitionGroup(true);

        toolbar_backIcon = (ImageView)myToolbar.findViewById(R.id.toolbar_backIcon);
        toolbar_deleteIcon = (ImageView)myToolbar.findViewById(R.id.toolbar_deleteTaskIcon);
        toolbar_editIcon = (ImageView)myToolbar.findViewById(R.id.toolbar_editTaskIcon);

        toolbar_editIcon.setOnClickListener(this);
        toolbar_deleteIcon.setOnClickListener(this);
        toolbar_backIcon.setOnClickListener(this);

        //Toolbar end



        int taskID = 0;

        taskID = getIntent().getIntExtra("Task_ID", 1);
        getDataFromTask(taskID);
















    }


    public void setupTransitions()
    {
        TransitionSet setEnter = new TransitionSet();

        Transition slideLeft = new Slide(Gravity.LEFT);
        slideLeft.setDuration(1000);
        slideLeft.excludeTarget(android.R.id.navigationBarBackground, true);
        slideLeft.excludeTarget(android.R.id.statusBarBackground, true);


        setEnter.addTransition(slideLeft);




        TransitionSet setReturn = new TransitionSet();

        Transition slideRight = new Slide(Gravity.RIGHT);
        slideRight.excludeTarget(android.R.id.navigationBarBackground, true);
        slideRight.excludeTarget(android.R.id.statusBarBackground, true);
        slideRight.excludeTarget(headerLayout, true);
        slideRight.setDuration(1000);

        setReturn.addTransition(slideRight);



        getWindow().setEnterTransition(setEnter);
        getWindow().setReturnTransition(setReturn);

    }



    private void getDataFromTask(int taskID)
    {


        SectionOrTask task = new SectionOrTask();
        task = myDb.getTask(taskID);




        this.taskNameText.setText(task.getName());
        this.taskCategoryText.setText(task.getCategory());
        this.taskCategoryIconText.setTypeface(typeface);
        this.taskCategoryIconText.setText(R.string.fa_icon_tag);
        String description = task.getDescription();
        if(description.length() > 0)
        {
            this.taskDescriptionText.setText(description);
        }
        else
        {
            this.taskDescriptionText.setText("No more detailed description");
        }
        this.taskTimeText.setText(task.getTime());
        try
        {
            Calendar date = Calendar.getInstance();
            date.setTime(calendarFormat.parse(task.getDate()));
            this.taskDateText.setText(dateFormat.format(date.getTime()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        if(task.getStatus() == 0)
        {
            taskCheckbox.setChecked(false);
        }
        else
        {
            taskCheckbox.setChecked(true);
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
            taskCard.setBackground(gd);
        }


        if(task.getCategory().equals("Travel"))
        {

            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.parseColor(getString(R.string.colorTravelStart)), Color.parseColor(getString(R.string.colorTravelEnd))});
            gd.setCornerRadius(2f);
            taskCard.setBackground(gd);
        }

        if(task.getCategory().equals("Home"))
        {

            GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{Color.parseColor(getString(R.string.colorHomeStart)), Color.parseColor(getString(R.string.colorHomeEnd))});
            gd.setCornerRadius(2f);
            taskCard.setBackground(gd);
        }
    }





    @Override
    public void onClick(View v)
    {
        int id = v.getId();


        switch (id)
        {

            case R.id.toolbar_deleteTaskIcon:

                break;


            case R.id.toolbar_backIcon:
                Log.d("ViewActivity toolbar", "BackIcon");
                onBackPressed();
                break;

            case R.id.toolbar_editTaskIcon:

                break;





        }
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
