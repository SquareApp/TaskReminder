package com.squareapp.taskreminder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;

/**
 * Created by Valentin Purrucker on 03.08.2017.
 */

class EditTextCardViewHolder extends RecyclerView.ViewHolder
{


    private ArrayAdapter<String> adapter;

    private ArrayList<String> categoryList;

    private CardView cardView;

    public MaterialBetterSpinner spinner;

    public EditText nameEditText;
    public EditText descriptionEditText;

    private Context context;

    public EditTextCardViewHolder(View itemView, Context context)
    {
        super(itemView);


        this.context = context;
        categoryList = new ArrayList<>();
        categoryList.add("To-Do");
        categoryList.add("Work");
        categoryList.add("Travel");
        categoryList.add("Home");

        adapter = new ArrayAdapter<String>(context, R.layout.spinner_item_custom, categoryList);

        cardView = (CardView) itemView.findViewById(R.id.edittextCardView);
        spinner = (MaterialBetterSpinner)itemView.findViewById(R.id.categorySpinner);
        spinner.setAdapter(adapter);
        spinner.setTextColor(Color.parseColor("#00117c"));
        nameEditText = (EditText)itemView.findViewById(R.id.nameEditText);
        descriptionEditText = (EditText)itemView.findViewById(R.id.descriptionEditText);

        //nameEditText.getBackground().mutate().setColorFilter(Color.parseColor("#8495f7"), PorterDuff.Mode.SRC_ATOP);
        //descriptionEditText.getBackground().mutate().setColorFilter(Color.parseColor("#8495f7"), PorterDuff.Mode.SRC_ATOP);


    }
}
