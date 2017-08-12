package com.squareapp.taskreminder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Valentin Purrucker on 12.08.2017.
 */

public class DoneFragment extends Fragment
{

    private RecyclerView recyclerView;

    private LinearLayoutManager lm;

    private MainAdapter myAdapter;

    private DatabaseHandler myDb;

    private ArrayList<SectionOrTask> dataList;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.done_fragment, container, false);

        myDb = new DatabaseHandler(getActivity());

        dataList = new ArrayList<>();


        recyclerView = (RecyclerView)rootView.findViewById(R.id.doneRecyclerView);

        dataList = myDb.getAllTasks(1);


        myAdapter = new MainAdapter(getActivity(), dataList);

        lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);

        recyclerView.setAdapter(myAdapter);








        return rootView;
    }
}
