package com.squareapp.taskreminder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Valentin Purrucker on 11.08.2017.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public ArrayList<SectionOrTask> mData;


    private Context context;


    private Typeface typeface;


    private AllTasksFragment allTasksFragment;

    private DatabaseHandler db;


    //Colors

    private String workColorStart;
    private String workColorEnd;

    private String travelColorStart;
    private String travelColorEnd;

    private String homeColorStart;
    private String homeColorEnd;


    public MainAdapter(Context context, ArrayList<SectionOrTask> data)
    {

        mData = data;
        this.context = context;


        typeface = FontCache.get("fonts/fontawesome-webfont.ttf", context);


        db = new DatabaseHandler(context);


        workColorStart = context.getResources().getString(R.string.colorWorkStart);
        workColorEnd = context.getResources().getString(R.string.colorWorkEnd);

        travelColorStart = context.getResources().getString(R.string.colorTravelStart);
        travelColorEnd = context.getResources().getString(R.string.colorTravelEnd);

        homeColorStart = context.getString(R.string.colorHomeStart);
        homeColorEnd = context.getString(R.string.colorHomeEnd);


    }


    @Override
    public int getItemCount()
    {
        return mData.size();
    }


    @Override
    public int getItemViewType(int position)
    {
        super.getItemViewType(position);

        SectionOrTask item = mData.get(position);

        if (!item.isTask()) {
            return 0;
        } else {
            return 1;
        }


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (viewType == 0) {
            View v = LayoutInflater.from(context).inflate(R.layout.theme_header, parent, false);

            return new SectionViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.task_card, parent, false);

            return new TaskViewHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position)
    {
        final SectionOrTask item = mData.get(position);


        if (item.isTask())
        {
            final TaskViewHolder h = (TaskViewHolder) holder;
            h.taskName.setText(item.getName());


            h.taskCategoryText.setText(mData.get(h.getAdapterPosition()).getCategory());

            if (item.getStatus() == 0)
            {
                h.statusCheckBox.setChecked(false);
            }
            else
            {
                h.statusCheckBox.setChecked(true);
            }


            h.view.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent viewIntent = new Intent((Activity) context, ViewActivity.class);
                    viewIntent.putExtra("Task_ID", item.getId());

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        CardView cardView = (CardView) h.view.findViewById(R.id.cardItem);


                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, cardView, cardView.getTransitionName());

                        context.startActivity(viewIntent, optionsCompat.toBundle());
                    } else
                        {
                        context.startActivity(viewIntent);
                    }


                }
            });


            if (item.getCategory().equals("Work")) {

                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[]{Color.parseColor(workColorStart), Color.parseColor(workColorEnd)});
                gd.setCornerRadius(2f);
                h.cardView.setBackground(gd);
            }

            if (item.getCategory().equals("Travel")) {

                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[]{Color.parseColor(travelColorStart), Color.parseColor(travelColorEnd)});
                gd.setCornerRadius(2f);
                h.cardView.setBackground(gd);
            }

            if (item.getCategory().equals("Home")) {

                GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                        new int[]{Color.parseColor(homeColorStart), Color.parseColor(homeColorEnd)});
                gd.setCornerRadius(2f);
                h.cardView.setBackground(gd);
            }


        }
        //Item is a section
        else
            {

            SectionViewHolder h = (SectionViewHolder) holder;
            h.dateTextIcon.setTypeface(typeface);
            h.dateText.setText(item.getSection());
            h.dateText.setTextColor(Color.parseColor("#626262"));
            h.dateTextIcon.setTextColor(Color.parseColor("#626262"));

        }





    }


    private String getThemeColor(String theme)
    {
        if (theme.equals("Work")) {
            return workColorStart;
        } else {
            if (theme.equals("Travel")) {
                return travelColorStart;
            } else
                return homeColorStart;
        }
    }


    class SectionViewHolder extends RecyclerView.ViewHolder
    {

        TextView dateText;
        TextView dateTextIcon;
        TextView themeAmount;

        public SectionViewHolder(View itemView)
        {
            super(itemView);

            dateText = (TextView) itemView.findViewById(R.id.dateText);
            dateTextIcon = (TextView) itemView.findViewById(R.id.themeTextIcon);
            dateTextIcon.setText(context.getString(R.string.fa_icon_date));

        }
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
    {

        TextView taskName;
        TextView taskCategoryText;
        TextView taskCategoryIconText;

        CheckBox statusCheckBox;


        CardView cardView;

        View view;


        public TaskViewHolder(View itemView)
        {
            super(itemView);

            this.view = itemView;


            taskName = (TextView) itemView.findViewById(R.id.taskName);
            taskCategoryText = (TextView) itemView.findViewById(R.id.taskDateText);
            taskCategoryIconText = (TextView) itemView.findViewById(R.id.taskDateIconText);
            taskCategoryIconText.setTypeface(typeface);
            taskCategoryIconText.setText(R.string.fa_icon_tag);


            statusCheckBox = (CheckBox) itemView.findViewById(R.id.statusCheckBox);


            cardView = (CardView) itemView.findViewById(R.id.cardItem);


            cardView.setOnLongClickListener(this);


        }

        @Override
        public boolean onLongClick(View v)
        {

            SectionOrTask task = mData.get(getAdapterPosition());
            Log.d("Database", "Item at pos: " + String.valueOf(task.getId()));

            if (mData.get(getAdapterPosition()).getStatus() == 0) {
                //mData.get(getAdapterPosition()).setStatus(1);
                task.setStatus(1);
                db.updateTask(task);
                statusCheckBox.setChecked(true);
            } else {
                if (mData.get(getAdapterPosition()).getStatus() == 1) {
                    //mData.get(getAdapterPosition()).setStatus(0);
                    task.setStatus(0);
                    db.updateTask(task);
                    statusCheckBox.setChecked(false);
                }
            }


            return true;
        }


    }

}

