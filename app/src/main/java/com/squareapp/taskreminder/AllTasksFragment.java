package com.squareapp.taskreminder;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Valentin Purrucker on 11.08.2017.
 */

public class AllTasksFragment extends Fragment
{

    public ArrayList<SectionOrTask> dataList;

    private MainAdapter mainAdapter;

    private RecyclerView recyclerView;

    private LinearLayoutManager lm;

    private DatabaseHandler myDb;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.all_tasks_fragment, container, false);


        dataList = new ArrayList<>();

        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview);

        lm = new LinearLayoutManager(getActivity());

        myDb = new DatabaseHandler(getActivity());




        //dataList.add(SectionOrTask.createTask("Name", "Work", 0, 0 , "12.01.17", "17:17"));
        //dataList.add(SectionOrTask.createTask("Name2", "Travel", 0, 0 , "12.01.17", "17:17"));

        dataList = myDb.getAllTasks(0);

        completeList(dataList);

        mainAdapter = new MainAdapter(getActivity(), dataList);


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

                //return position == 1 ? 0 : super.getSwipeDirs(recyclerView, holder);
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

                if(dataList.get(position).isTask() == true)
                {

                    dataList.remove(position);
                    myDb.deleteTask(taskID);
                    Toast.makeText(getActivity(), "Database at position: " + position, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(context, "RecyclerList " + myRecycler.mData.size(), Toast.LENGTH_SHORT).show();
                    mainAdapter.notifyItemRemoved(position);
                    mainAdapter.notifyItemRangeChanged(0, mainAdapter.getItemCount());

                    AlertReceiver receiver = new AlertReceiver();


                    Intent alertIntent = new Intent(getActivity(), receiver.getClass());
                    alertIntent.putExtra("Notification_ID", taskID);
                    Log.d("AlertIntent", "ID" + String.valueOf(alertIntent.getIntExtra("Notification_ID", 0)));

                    AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), taskID, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);




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


    private void completeList(ArrayList<SectionOrTask> list)
    {
        ArrayList<SectionOrTask> mList;
        ArrayList<ThemeHeader> themeList = new ArrayList<>();
        mList = list;




        String date = "00.00.00";


        if(mList.size() == 1)
        {
            date = mList.get(0).getDate();
            themeList.add(ThemeHeader.createThemeHeader(0, date));
        }
        else
        {
            for(int i = 0; i < mList.size() -1; i++)
            {
                date = mList.get(i).getDate();


                if(i == 0)
                {
                    themeList.add(ThemeHeader.createThemeHeader(i,mList.get(i).getDate()));
                }
                {
                    if(!mList.get(i +1).getDate().equals(date))
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


}
