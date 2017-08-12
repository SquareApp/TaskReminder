package com.squareapp.taskreminder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

/**
 * Created by Valentin Purrucker on 03.08.2017.
 */

public class RepetitionCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{


    private ArrayAdapter<String> adapter;
    private ArrayList<String> repetitionList;

    private MaterialBetterSpinner spinner;


    private Context context;

    private LinearLayout addLayout;
    private LinearLayout repetitionLayout;

    private ImageView repetitionIcon;

    private AddNewTaskAdapter addNewTaskAdapter;

    private boolean isrepeating = false;

    private Drawable addIcon;
    private Drawable deleteIcon;


    public RepetitionCardViewHolder(View itemView, Context context, AddNewTaskAdapter addNewTaskAdapter)
    {
        super(itemView);

        this.context = context;

        repetitionList = new ArrayList<>();

        this.addNewTaskAdapter = addNewTaskAdapter;

        addIcon = ContextCompat.getDrawable(context, R.drawable.ic_add_circle_white);
        deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_cancel_white);


        repetitionList.add("Once a day");
        repetitionList.add("Once a week");
        repetitionList.add("Once a month");
        repetitionList.add("Once a year");



        adapter = new ArrayAdapter<String>(context, R.layout.spinner_item_custom_repetition, repetitionList);

        spinner = (MaterialBetterSpinner)itemView.findViewById(R.id.repetitionSpinner);
        spinner.setAdapter(adapter);
        spinner.setTextColor(Color.parseColor("#780000"));

        addLayout = (LinearLayout)itemView.findViewById(R.id.addLayout);
        repetitionLayout = (LinearLayout)itemView.findViewById(R.id.repetitionLayout);

        repetitionIcon = (ImageView)itemView.findViewById(R.id.addIcon);


        repetitionIcon.setOnClickListener(this);







    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        switch (id)
        {
            case R.id.addIcon:

                if(!isrepeating)
                {
                    repetitionLayout.setVisibility(View.VISIBLE);
                    addLayout.setVisibility(View.GONE);
                    repetitionIcon.setImageDrawable(deleteIcon);
                    isrepeating = true;

                }
                else
                {
                    repetitionLayout.setVisibility(View.GONE);
                    addLayout.setVisibility(View.VISIBLE);
                    repetitionIcon.setImageDrawable(addIcon);
                    spinner.setText(null);
                    isrepeating = false;
                }


                break;


        }
    }
}
