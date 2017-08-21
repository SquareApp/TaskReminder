package com.squareapp.taskreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class AddNewTaskActivity extends AppCompatActivity
{

    private RecyclerView myRecyclerView;

    private LinearLayoutManager lm;

    private ArrayList<String> categoryList;

    public AddNewTaskAdapter addNewTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        init();
    }


    private void init()
    {
        myRecyclerView = (RecyclerView)findViewById(R.id.addNewTaskRecyclerView);

        lm = new LinearLayoutManager(this);

        addNewTaskAdapter = new AddNewTaskAdapter(this, getFragmentManager());

        categoryList = new ArrayList<>();
        categoryList = new ArrayList<>();
        categoryList.add("To-Do");
        categoryList.add("Work");
        categoryList.add("Travel");
        categoryList.add("Home");


        myRecyclerView.setAdapter(addNewTaskAdapter);
        myRecyclerView.setLayoutManager(lm);



    }
}
