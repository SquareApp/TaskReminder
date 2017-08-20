package com.squareapp.taskreminder;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
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

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    private Typeface typeface;


    private AllTasksFragment allTasksFragment;

    private Resources resources;

    private DatabaseHandler db;

    private FragmentManager fragmentManager;

    private MainActivity mainActivity;

    private Calendar now = Calendar.getInstance();


    private int marginLeft, marginRight, marginTop, marginBottom;





    public MainAdapter(Context context, ArrayList<SectionOrTask> data, FragmentManager fragmentManager)
    {

        mData = data;
        this.context = context;

        resources = context.getResources();

        mainActivity = (MainActivity)context;

        this.fragmentManager = fragmentManager;



        typeface = FontCache.get("fonts/fontawesome-webfont.ttf", context);


        db = new DatabaseHandler(context);





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
            h.taskTimeLeft.setText(setDaysLeft(item.getDate()));



            if(h.getAdapterPosition() == getItemCount() -1)
            {

                //left, top, right, bottom
                setMarginInPixelFromDP(20, 7.5f, 20, 7.5f);

                CardView.LayoutParams params = new CardView.LayoutParams(
                        CardView.LayoutParams.WRAP_CONTENT,
                        CardView.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(this.marginLeft, this.marginTop, this.marginRight, (int)(mainActivity.bottomNavigation.getHeight() / 1.5f));
                h.cardView.setLayoutParams(params);
            }


            h.taskCategoryText.setText(item.getCategory());
            h.taskCategoryText.setTextColor(Color.parseColor(setTaskCardColors(item)));
            h.taskCategoryIconText.setTextColor(Color.parseColor(setTaskCardColors(item)));

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
                    Intent viewIntent = new Intent(context, TestActivity.class);
                    viewIntent.putExtra("Task_ID", item.getId());

                    DoneFragment doneFragment = (DoneFragment) fragmentManager.findFragmentByTag("DoneFragment");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                    {
                        CardView cardView = (CardView) h.view.findViewById(R.id.cardItem);




                        TransitionSet setExit = new TransitionSet();

                        Transition slide = new Slide(Gravity.RIGHT);
                        slide.setDuration(50);
                        slide.excludeTarget(android.R.id.navigationBarBackground, true);
                        slide.excludeTarget(android.R.id.statusBarBackground, true);
                        //slide.excludeTarget(mainActivity.bottomNavigation, true);
                        slide.excludeTarget(cardView, true);

                        setExit.addTransition(slide);


                        TransitionSet setReenter = new TransitionSet();

                        Transition reenter = new Slide(Gravity.LEFT);
                        slide.setDuration(25);
                        slide.excludeTarget(android.R.id.navigationBarBackground, true);
                        slide.excludeTarget(android.R.id.statusBarBackground, true);
                        //slide.excludeTarget(mainActivity.bottomNavigation, true);
                        setReenter.addTransition(reenter);



                        ((Activity)context).getWindow().setReenterTransition(null);
                        ((Activity)context).getWindow().setExitTransition(setExit);




                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)context, cardView, "cardTransition");
                        ActivityCompat.startActivity(context, viewIntent, options.toBundle());


                    }
                    else
                    {
                        context.startActivity(viewIntent);
                    }


                }
            });


            //h.cardView.setBackground(setTaskCardBackground(item));


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

    private String setTaskCardColors(SectionOrTask task)
    {


        String colorCode;

  /*
        myDb.addColor("#781054", "Travel");
        myDb.addColor("#1d976c", "Work");
        myDb.addColor("#1d5e97", "Home");
        myDb.addColor("#0065ab", "To-Do");

   */



        colorCode = db.getColor(task.getCategory());

        return colorCode;


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


    private String setDaysLeft(String date)
    {
        Calendar now = Calendar.getInstance();
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.HOUR_OF_DAY, 0);

        Calendar taskDateCalendar = Calendar.getInstance();

        String daysLeftString = null;

        try
        {
            taskDateCalendar.setTime(dateFormat.parse(date));
            Log.d("MainAdapter", "Parse date -> Success");
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






        Log.d("MainAdapter", "Exact value: " + String.valueOf(dayCount));
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


    private void setMarginInPixelFromDP(float dp_margin_Left, float dp_margin_Top, float dp_margin_Right, float dp_margin_Bottom)
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
            calendar.setTime(dateFormat.parse(dateString));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return simpleDateFormat.format(calendar.getTime());
    }







    class SectionViewHolder extends RecyclerView.ViewHolder
    {

        TextView dateText;
        TextView dateTextIcon;

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
        TextView taskTimeLeft;

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
            taskTimeLeft = (TextView)itemView.findViewById(R.id.taskDaysLeftText);
            taskCategoryIconText.setTypeface(typeface);
            taskCategoryIconText.setText(R.string.fa_icon_tag);


            statusCheckBox = (CheckBox) itemView.findViewById(R.id.statusCheckBox);


            cardView = (CardView) itemView.findViewById(R.id.cardItem);




        }




    }

}

