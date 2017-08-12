package com.squareapp.taskreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private Toolbar toolbar;

    private BottomBar bottomBar;

    private android.app.FragmentManager fragmentManager = getFragmentManager();

    private TextView toolbarTitle;

    private ImageView toolbar_addNewTaskIcon;
    private ImageView toolbar_saveNewTaskIcon;
    private ImageView toolbar_cancelNewTaskIcon;


    private DatabaseHandler myDb;

    private SimpleDateFormat sdf = new SimpleDateFormat("E, MMM dd, yyyy");

    private AddNewTaskFragment addNewTaskFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        bottomBar = (BottomBar)findViewById(R.id.bottomBar);


        toolbarTitle = (TextView)toolbar.findViewById(R.id.toolbarTitle);
        toolbar_addNewTaskIcon = (ImageView)toolbar.findViewById(R.id.toolbar_addNewTaskIcon);
        toolbar_saveNewTaskIcon = (ImageView)toolbar.findViewById(R.id.toolbar_saveNewTaskIcon);
        toolbar_cancelNewTaskIcon = (ImageView)toolbar.findViewById(R.id.toolbar_cancelNewTaskIcon);
        toolbar_cancelNewTaskIcon.setOnClickListener(this);
        toolbar_saveNewTaskIcon.setOnClickListener(this);
        toolbar_addNewTaskIcon.setOnClickListener(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        bottomBar.selectTabAtPosition(1, true);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener()
         {
            @Override
            public void onTabSelected(@IdRes int tabId)
            {

                if (tabId == R.id.tab_recent)
                {
                    getWindow().setNavigationBarColor(Color.parseColor("#5c2178"));
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new AllTasksFragment(), "AllTasksFragment").commit();
                }

                if (tabId == R.id.tab_done)
                {
                    getWindow().setNavigationBarColor(Color.parseColor("#d45244"));
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new DoneFragment()).commit();
                }

                if(tabId == R.id.tab_friends)
                {
                    getWindow().setNavigationBarColor(Color.parseColor("#db8100"));
                }
            }
        });



        myDb = new DatabaseHandler(this);
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
                String dateString;
                String timeString;

                int hour;
                int minute;
                int taskID;









                //endregion


                //region DATE





                dateString = this.sdf.format(eventCalendar.getTime());

                hour = eventCalendar.get(Calendar.HOUR_OF_DAY);
                minute = eventCalendar.get(Calendar.MINUTE);

                timeString = String.valueOf(hour) + ":" + String.valueOf(minute);





                //endregion


                //region TASK_FIELDS

                name = addNewTaskFragment.addNewTaskAdapter.editTextCardViewHolder.nameEditText.getText().toString();
                category = addNewTaskFragment.addNewTaskAdapter.editTextCardViewHolder.spinner.getText().toString();


                //endregion









                //region INSERT_IN_DATABSE

                myDb.addTask(SectionOrTask.createTask(name, category, 0, 0, dateString, timeString));

                //endregion




                //region create alarmManager

                taskID = myDb.task_ID;
                long alertTime = eventCalendar.getTimeInMillis();






                AlertReceiver receiver = new AlertReceiver();


                Intent alertIntent = new Intent(this, receiver.getClass());
                alertIntent.putExtra("Notification_ID", taskID);
                Log.d("AlertIntent", "ID" + String.valueOf(alertIntent.getIntExtra("Notification_ID", 0)));

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
            case R.id.saveTaskIcon:
                getData();
                break;
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
                Toast.makeText(this, "Add new task", Toast.LENGTH_SHORT).show();
                openAddNewTaskFragment();
                break;

            case R.id.toolbar_saveNewTaskIcon:
                getData();
                closeAddNewTaskFragment();
                break;

            case R.id.toolbar_cancelNewTaskIcon:
                closeAddNewTaskFragment();
                break;




        }
    }



    private void closeAddNewTaskFragment()
    {
        fragmentManager.beginTransaction().replace(R.id.content_frame, new AllTasksFragment(), "AllTasksFragment").commit();
        toolbar_addNewTaskIcon.setVisibility(View.VISIBLE);
        toolbar_saveNewTaskIcon.setVisibility(View.GONE);
        toolbar_cancelNewTaskIcon.setVisibility(View.GONE);
    }

    private void openAddNewTaskFragment()
    {
        fragmentManager.beginTransaction().replace(R.id.content_frame, new AddNewTaskFragment(), "AddNewTaskFragment").commit();
        toolbar_addNewTaskIcon.setVisibility(View.GONE);
        toolbar_saveNewTaskIcon.setVisibility(View.VISIBLE);
        toolbar_cancelNewTaskIcon.setVisibility(View.VISIBLE);
    }


}
