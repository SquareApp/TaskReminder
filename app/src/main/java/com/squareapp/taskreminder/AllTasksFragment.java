package com.squareapp.taskreminder;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Valentin Purrucker on 11.08.2017.
 */

public class AllTasksFragment extends Fragment
{

    public ArrayList<SectionOrTask> dataList  = new ArrayList<>();;

    private MainAdapter mainAdapter;

    private RecyclerView recyclerView;

    private LinearLayoutManager lm;

    private LinearLayout noTasksLayout;

    private DatabaseHandler myDb;

    private MainActivity mainActivity;

    private boolean isSwiping = false;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.all_tasks_fragment, container, false);


        noTasksLayout = (LinearLayout)rootView.findViewById(R.id.noTasksLayout);




        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview);

        lm = new LinearLayoutManager(getActivity());

        myDb = new DatabaseHandler(getActivity());

        mainActivity = (MainActivity)getActivity();
        mainActivity.openAllTasksFragment();

        mainActivity.bottomNavigation.animate().translationY(0).setDuration(100);



        mainAdapter = new MainAdapter(getActivity(), dataList, getFragmentManager());


        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(mainAdapter);







        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT)
        {






            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder holder)
            {
                int position = holder.getAdapterPosition();

                if(dataList.get(position).isTask() == false)
                    return 0;
                else
                    return super.getSwipeDirs(recyclerView,holder);

            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction)
            {




                final int position = viewHolder.getAdapterPosition(); //get position which is swipe
                final int taskID = dataList.get(position).getId();
                final SectionOrTask task = myDb.getTask(taskID);
                Log.d("Task swipe Time: ", task.getTime());
                final Handler deleteHandler = new Handler();
                final Runnable runnable = new Runnable()
                {
                    @Override
                    public void run()
                    {
                        deleteTask(taskID, position);
                    }
                };

                deleteHandler.postDelayed(runnable, 3500);

                if(dataList.get(position).isTask() == true)
                {

                    Snackbar snackbar = Snackbar.make(rootView, "1 item removed", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Undo", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            deleteHandler.removeCallbacks(runnable);
                            dataList.add(position, task);
                            mainAdapter.notifyItemInserted(position);
                            mainAdapter.notifyItemRangeChanged(position, mainAdapter.getItemCount());

                        }
                    });
                    snackbar.setActionTextColor(Color.WHITE);

                    snackbar.show();

                    dataList.remove(position);
                    mainAdapter.notifyItemRemoved(position);
                    mainAdapter.notifyItemRangeChanged(position, mainAdapter.getItemCount());

                }
                else
                {
                    Toast.makeText(getActivity(), "No", Toast.LENGTH_SHORT).show();
                }



            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recylcerview







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
        dataList.addAll(myDb.getAllTasks(0));
        completeList(dataList);
        mainAdapter.notifyDataSetChanged();

        if(dataList.size() > 0)
        {
            recyclerView.setVisibility(View.VISIBLE);
            noTasksLayout.setVisibility(View.GONE);
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            noTasksLayout.setVisibility(View.VISIBLE);
        }
    }



    private void deleteTask(int taskID, int position)
    {


        updateListAfterDeleteTask(taskID);





        //Alarm manager
        AlertReceiver receiver = new AlertReceiver();


        Intent alertIntent = new Intent(getActivity(), receiver.getClass());
        alertIntent.putExtra("Notification_ID", taskID);
        Log.d("AlertIntent", "ID" + String.valueOf(alertIntent.getIntExtra("Notification_ID", 0)));

        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), taskID, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
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



    public void updateListAfterDeleteTask(int id)
    {
        myDb.deleteTask(id);
        dataList.clear();
        dataList.addAll(myDb.getAllTasks(0));
        completeList(dataList);
        mainAdapter.notifyDataSetChanged();

        if(dataList.size() > 0)
        {
            recyclerView.setVisibility(View.VISIBLE);
            noTasksLayout.setVisibility(View.GONE);
        }
        else
        {
            recyclerView.setVisibility(View.GONE);
            noTasksLayout.setVisibility(View.VISIBLE);
        }
    }




    /**
     * Override methods
     */

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
