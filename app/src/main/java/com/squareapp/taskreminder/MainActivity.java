package com.squareapp.taskreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigation.OnMenuItemSelectionListener
{

    private Toolbar toolbar;



    private android.app.FragmentManager fragmentManager = getFragmentManager();

    private TextView toolbarTitle;

    private ImageView toolbar_addNewTaskIcon;
    private ImageView toolbar_saveNewTaskIcon;
    private ImageView toolbar_cancelNewTaskIcon;

    private CoordinatorLayout view;

    private FrameLayout frameLayout;



    public BottomNavigation bottomNavigation;


    private DatabaseHandler myDb;

    private SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy");

    private AddNewTaskFragment addNewTaskFragment;
    private AllTasksFragment allTasksFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHandler(this);


        view = (CoordinatorLayout)findViewById(R.id.CoordinatorLayout01);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        frameLayout = (FrameLayout)findViewById(R.id.content_frame);



        bottomNavigation = (BottomNavigation)findViewById(R.id.BottomNavigation);
        //bottomNavigation.setDefaultSelectedIndex(1);
        bottomNavigation.setSelectedIndex(1, true);
        bottomNavigation.setOnMenuItemClickListener(this);






        toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbarTitle);
        toolbar_addNewTaskIcon = (ImageView)toolbar.findViewById(R.id.toolbar_addNewTaskIcon);
        toolbar_saveNewTaskIcon = (ImageView)toolbar.findViewById(R.id.toolbar_saveNewTaskIcon);
        toolbar_cancelNewTaskIcon = (ImageView)toolbar.findViewById(R.id.toolbar_cancelNewTaskIcon);
        toolbar_cancelNewTaskIcon.setOnClickListener(this);
        toolbar_saveNewTaskIcon.setOnClickListener(this);
        toolbar_addNewTaskIcon.setOnClickListener(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);








        //myDb.deleteDatabase();

        fragmentManager.beginTransaction().replace(R.id.content_frame, new AllTasksFragment(), "AllTasksFragment").commit();


    }








    public void getData()
    {

        AddNewTaskFragment addNewTaskFragment = (AddNewTaskFragment) getFragmentManager().findFragmentByTag("AddNewTaskFragment");

            Log.d("AddNewTask", "Success");

            Calendar eventCalendar = Calendar.getInstance();
            eventCalendar = addNewTaskFragment.addNewTaskAdapter.dateCardViewHolder.eventCalendar;


                //region VARIABLES
                String name;
                String category;
                String description;
                String dateString;
                String timeString;


                int taskID;









                //endregion


                //region DATE





                dateString = addNewTaskFragment.addNewTaskAdapter.dateCardViewHolder.databaseFormattedDate;

                timeString = addNewTaskFragment.addNewTaskAdapter.dateCardViewHolder.databaseFormattedTime;





                //endregion


                //region TASK_FIELDS

                name = addNewTaskFragment.addNewTaskAdapter.editTextCardViewHolder.nameEditText.getText().toString();
                category = addNewTaskFragment.addNewTaskAdapter.editTextCardViewHolder.spinner.getText().toString();
                description = addNewTaskFragment.addNewTaskAdapter.editTextCardViewHolder.descriptionEditText.getText().toString();


                //endregion









                //region INSERT_IN_DATABSE

                myDb.addTask(SectionOrTask.createTask(name, category, description,  0, 0, dateString, timeString));

                //endregion




                //region create alarmManager

                taskID = myDb.task_ID;
                long alertTime = eventCalendar.getTimeInMillis();






                AlertReceiver receiver = new AlertReceiver();


                Intent alertIntent = new Intent(this, receiver.getClass());
                alertIntent.putExtra("Notification_ID", taskID);

                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, alertIntent.getIntExtra("Notification_ID", 0), alertIntent, PendingIntent.FLAG_UPDATE_CURRENT));







                //endregion




                //For debugging purpose
                Log.d("Create new Task", "Success");

                fragmentManager.beginTransaction().replace(R.id.content_frame, new AllTasksFragment(), "AllTasksFragment").commit();


        }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        int id = item.getItemId();

        switch (id)
        {
           // case R.id.saveTaskIcon:
              //  getData();
              //  break;
        }



        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v)
    {
        int id = v.getId();


        switch (id)
        {

            case R.id.toolbar_addNewTaskIcon:
                Intent addNewTaskIntent = new Intent(this, AddNewTaskActivity.class);
                startActivity(addNewTaskIntent);
                break;

            case R.id.toolbar_saveNewTaskIcon:
                getData();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AllTasksFragment(), "AllTasksFragment").commit();
                break;

            case R.id.toolbar_cancelNewTaskIcon:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AllTasksFragment(), "AllTasksFragment").commit();
                break;




        }







    }



    public void openAllTasksFragment()
    {
        toolbar_addNewTaskIcon.setVisibility(View.VISIBLE);
        toolbar_saveNewTaskIcon.setVisibility(View.GONE);
        toolbar_cancelNewTaskIcon.setVisibility(View.GONE);
    }

    public void openAddNewTaskFragment()
    {
        toolbar_addNewTaskIcon.setVisibility(View.GONE);
        toolbar_saveNewTaskIcon.setVisibility(View.VISIBLE);
        toolbar_cancelNewTaskIcon.setVisibility(View.VISIBLE);
    }


    @Override
    public void onMenuItemSelect(@IdRes int i, int i1, boolean b)
    {
        switch (i)
        {
            case R.id.tab_done:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new DoneFragment(), "DoneFragment").commit();
                break;

            case R.id.tab_recent:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new AllTasksFragment(), "AllTaskFragment").commit();
                break;
        }
    }

    @Override
    public void onMenuItemReselect(@IdRes int i, int i1, boolean b)
    {

    }
}
