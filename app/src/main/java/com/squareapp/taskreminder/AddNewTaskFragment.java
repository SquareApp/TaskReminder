package com.squareapp.taskreminder;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Valentin Purrucker on 29.07.2017.
 */

public class AddNewTaskFragment extends Fragment implements View.OnClickListener
{


    private Context context;

    private ArrayList<String> categoryList;

    private RecyclerView recyclerView;

    private LinearLayoutManager lm;

    public AddNewTaskAdapter addNewTaskAdapter;

    private FragmentManager fragmentManager;

    private ActivityOptionsCompat optionsCompat;

    private MainActivity mainActivity;











    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.add_new_task_fragment, container, false);


        init(rootView);
        //mainActivity.openAddNewTaskFragment();

        setHasOptionsMenu(true);









        return rootView;
    }


    private void init(View rootView)
    {
        this.context = getActivity().getApplicationContext();


        mainActivity = (MainActivity)getActivity();
        mainActivity.openAddNewTaskFragment();

        mainActivity.bottomNavigation.animate().translationY(mainActivity.bottomNavigation.getHeight()).setDuration(100);


        recyclerView = (RecyclerView)rootView.findViewById(R.id.addNewTaskRecyclerView);

        fragmentManager = getFragmentManager();

        addNewTaskAdapter = new AddNewTaskAdapter(context, fragmentManager);

        lm = new LinearLayoutManager(context);
        recyclerView.setAdapter(addNewTaskAdapter);
        recyclerView.setLayoutManager(lm);








        categoryList = new ArrayList<>();
        categoryList.add("To-Do");
        categoryList.add("Work");
        categoryList.add("Travel");
        categoryList.add("Home");



    }






    @Override
    public void onClick(View v)
    {
        int id = v.getId();


        switch (id)
        {

        }
    }





}
