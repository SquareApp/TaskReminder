package com.squareapp.taskreminder;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Valentin Purrucker on 12.08.2017.
 */

public class DoneFragment extends Fragment
{

    public RecyclerView recyclerView;

    private LinearLayout noTasksLayout;
    public LinearLayout recyclerViewLayout;

    private LinearLayoutManager lm;

    private MainAdapter mainAdapter;

    private DatabaseHandler myDb;

    private ArrayList<SectionOrTask> dataList = new ArrayList<>();

    private MainActivity mainActivity;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.done_fragment, container, false);

        myDb = new DatabaseHandler(getActivity());

        mainActivity = (MainActivity)getActivity();





        noTasksLayout = (LinearLayout) rootView.findViewById(R.id.noTasksLayout);
        recyclerViewLayout = (LinearLayout)rootView.findViewById(R.id.recyclerViewLayout);


        dataList = new ArrayList<>();


        recyclerView = (RecyclerView)rootView.findViewById(R.id.doneRecyclerView);



        mainAdapter = new MainAdapter(getActivity(), dataList, getFragmentManager());

        lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);

        recyclerView.setAdapter(mainAdapter);






        return rootView;
    }

    private BroadcastReceiver messageReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            updateList();
        }
    };


    private void updateList()
    {
        dataList.clear();
        dataList.addAll(myDb.getAllTasks(1));
        completeList(dataList);
        mainAdapter.notifyDataSetChanged();

        if(dataList.size() > 0)
        {
            recyclerViewLayout.setVisibility(View.VISIBLE);
            noTasksLayout.setVisibility(View.GONE);
        }
        else
        {
            recyclerViewLayout.setVisibility(View.GONE);
            noTasksLayout.setVisibility(View.VISIBLE);
        }
    }

    private void deleteTask(final int taskID, final int position)
    {
        //mainAdapter.notifyItemRemoved(position);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                updateListAfterDeleteTask(taskID);
            }
        }, 10);







        //Alarm manager
        AlertReceiver receiver = new AlertReceiver();


        Intent alertIntent = new Intent(getActivity(), receiver.getClass());
        alertIntent.putExtra("Notification_ID", taskID);
        Log.d("AlertIntent", "ID" + String.valueOf(alertIntent.getIntExtra("Notification_ID", 0)));

        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), taskID, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
    }


    public void updateListAfterDeleteTask(int id)
    {
        myDb.deleteTask(id);
        dataList.clear();
        dataList.addAll(myDb.getAllTasks(1));
        completeList(dataList);
        mainAdapter.notifyDataSetChanged();

        if(dataList.size() > 0)
        {
            recyclerViewLayout.setVisibility(View.VISIBLE);
            noTasksLayout.setVisibility(View.GONE);
        }
        else
        {
            recyclerViewLayout.setVisibility(View.GONE);
            noTasksLayout.setVisibility(View.VISIBLE);
        }
    }



    private void completeList(ArrayList<SectionOrTask> list)
    {
        ArrayList<SectionOrTask> mList;
        ArrayList<ThemeHeader> themeList = new ArrayList<>();
        mList = list;
        String dateString = null;





        if(mList.size() == 1)
        {
            dateString = mList.get(0).getDate();
            themeList.add(ThemeHeader.createThemeHeader(0, dateString));
        }
        else
        {
            for(int i = 0; i < mList.size() -1; i++)
            {
                dateString = mList.get(i).getDate();


                if(i == 0)
                {
                    themeList.add(ThemeHeader.createThemeHeader(i,mList.get(i).getDate()));
                }
                {
                    if(!mList.get(i +1).getDate().equals(dateString))
                    {
                        themeList.add(ThemeHeader.createThemeHeader((i +1), mList.get(i +1).getDate()));

                    }
                }

            }
        }



        createSections(themeList);

    }

    private void createSections(ArrayList<ThemeHeader> themesList)
    {

        int offset = 0;

        for (int i = 0; i < themesList.size(); i++)
        {
            dataList.add(themesList.get(i).getPosition() +offset, SectionOrTask.createSection(themesList.get(i).getDate()));
            offset +=1;
        }

    }


    @Override
    public void onResume()
    {
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(messageReceiver, new IntentFilter("BROADCAST_REFRESH"));
        updateList();



        super.onResume();
    }


    @Override
    public void onPause()
    {

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(messageReceiver);


        myDb.close();
        super.onPause();
    }
}
