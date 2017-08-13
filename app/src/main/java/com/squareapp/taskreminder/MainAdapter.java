package com.squareapp.taskreminder;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Valentin Purrucker on 11.08.2017.
 */

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    public ArrayList<SectionOrTask> mData;


    private Context context;

    private SimpleDateFormat DATE_AND_TIME_FORMAT = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMM dd, yyyy");

    private Typeface typeface;


    private AllTasksFragment allTasksFragment;

    private Resources resources;

    private DatabaseHandler db;

    private FragmentManager fragmentManager;

    private MainActivity mainActivity;


    private int marginLeft, marginRight, marginTop, marginBottom;


    //Colors

    private String workColorStart;
    private String workColorEnd;

    private String travelColorStart;
    private String travelColorEnd;

    private String homeColorStart;
    private String homeColorEnd;


    public MainAdapter(Context context, ArrayList<SectionOrTask> data, FragmentManager fragmentManager)
    {

        mData = data;
        this.context = context;

        resources = context.getResources();

        mainActivity = (MainActivity)context;

        this.fragmentManager = fragmentManager;


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
            h.taskTimeText.setText(item.getTime());



            if(h.getAdapterPosition() == getItemCount() -1)
            {

                //left, top, right, bottom
                setMarginInPixelFromDP(30, 0, 30, 0);

                CardView.LayoutParams params = new CardView.LayoutParams(
                        CardView.LayoutParams.WRAP_CONTENT,
                        CardView.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(this.marginLeft, this.marginTop, this.marginRight, (int)(mainActivity.bottomNavigation.getHeight() / 1.5f));
                h.cardView.setLayoutParams(params);
            }


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
                        TextView taskName = (TextView)h.view.findViewById(R.id.taskName);
                        TextView taskCategory = (TextView)h.view.findViewById(R.id.taskCategoryText);
                        TextView taskCategoryIcon = (TextView)h.view.findViewById(R.id.taskCategoryIconText);
                        CheckBox taskCheckbox = (CheckBox)h.view.findViewById(R.id.statusCheckBox);
                        Pair<View, String> p1 = Pair.create((View)cardView, "cardTransition");
                        Pair<View, String> p2 = Pair.create((View)taskName, "cardTransition_Name");
                        Pair<View, String> p3 = Pair.create((View)taskCategory, "cardTransition_Category");
                        Pair<View, String> p4 = Pair.create((View)taskCategoryIcon, "cardTransition_Icon");
                        Pair<View, String> p5 = Pair.create((View)taskCheckbox, "cardTransition_CheckBox");


                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, p1, p2, p3, p4, p5);

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
            h.dateText.setText(getReadableDate(item.getSectionDate()));
            h.dateText.setTextColor(Color.parseColor("#626262"));
            h.dateTextIcon.setTextColor(Color.parseColor("#626262"));

        }





    }



    private void setMarginInPixelFromDP(int dp_margin_Left, int dp_margin_Top, int dp_margin_Right, int dp_margin_Bottom)
    {
        this.marginLeft = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp_margin_Left,
            resources.getDisplayMetrics()
    );

        this.marginTop = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp_margin_Top,
                resources.getDisplayMetrics()
        );

        this.marginRight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp_margin_Right,
                resources.getDisplayMetrics()
        );

        this.marginBottom = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp_margin_Bottom,
                resources.getDisplayMetrics()
        );
    }

    private String getReadableDate(String dateString)
    {
        Calendar calendar = Calendar.getInstance();
        try
        {
            calendar.setTime(DATE_AND_TIME_FORMAT.parse(dateString));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return simpleDateFormat.format(calendar.getTime());
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

    class TaskViewHolder extends RecyclerView.ViewHolder
    {

        TextView taskName;
        TextView taskCategoryText;
        TextView taskCategoryIconText;
        TextView taskTimeText;

        CheckBox statusCheckBox;


        CardView cardView;

        View view;


        public TaskViewHolder(View itemView)
        {
            super(itemView);

            this.view = itemView;


            taskName = (TextView) itemView.findViewById(R.id.taskName);
            taskCategoryText = (TextView) itemView.findViewById(R.id.taskCategoryText);
            taskCategoryIconText = (TextView) itemView.findViewById(R.id.taskCategoryIconText);
            taskTimeText = (TextView)itemView.findViewById(R.id.taskTimeText);
            taskCategoryIconText.setTypeface(typeface);
            taskCategoryIconText.setText(R.string.fa_icon_tag);


            statusCheckBox = (CheckBox) itemView.findViewById(R.id.statusCheckBox);


            cardView = (CardView) itemView.findViewById(R.id.cardItem);




        }




    }

}

