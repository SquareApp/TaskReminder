package com.squareapp.taskreminder;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity
{

    private int taskID;

    private DatabaseHandler myDb;

    private CardView taskCard;

    private TextView taskNameText;
    private TextView taskDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);


        myDb = new DatabaseHandler(this);

        taskID = getIntent().getIntExtra("Task_ID", 1);

        SectionOrTask task = new SectionOrTask();
        task = myDb.getTask(taskID);

        taskCard = (CardView) findViewById(R.id.taskCardItem);

        taskNameText = (TextView)taskCard.findViewById(R.id.taskName);
        taskDateText = (TextView)taskCard.findViewById(R.id.taskDateText);

        taskNameText.setText(task.getName());
        taskDateText.setText(task.getDate());








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
