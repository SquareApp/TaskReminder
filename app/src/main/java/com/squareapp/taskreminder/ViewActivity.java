package com.squareapp.taskreminder;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.CheckBox;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity
{

    private int taskID;

    private DatabaseHandler myDb;

    private CardView taskCard;

    private TextView taskNameText;
    private TextView taskCategoryText;
    private TextView taskCategoryIconText;

    private CheckBox taskCheckbox;


    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        myDb = new DatabaseHandler(this);

        typeface = FontCache.get("fonts/fontawesome-webfont.ttf", this);

        taskID = getIntent().getIntExtra("Task_ID", 1);

        SectionOrTask task = new SectionOrTask();
        task = myDb.getTask(taskID);

        taskCard = (CardView) findViewById(R.id.taskCardItem);

        taskNameText = (TextView)taskCard.findViewById(R.id.taskName);
        taskCategoryText = (TextView)taskCard.findViewById(R.id.taskCategoryText);
        taskCategoryIconText = (TextView)taskCard.findViewById(R.id.taskCategoryIconText);

        taskCheckbox = (CheckBox)taskCard.findViewById(R.id.statusCheckBox);

        taskNameText.setText(task.getName());
        taskCategoryText.setText(task.getCategory());
        taskCategoryIconText.setTypeface(typeface);
        taskCategoryIconText.setText(R.string.fa_icon_tag);

        if(task.getStatus() == 0)
        {
            taskCheckbox.setChecked(false);
        }
        else
        {
            taskCheckbox.setChecked(true);
        }








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
}
