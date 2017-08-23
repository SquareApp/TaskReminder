package com.squareapp.taskreminder;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;

public class AddNewTaskActivity extends AppCompatActivity implements TextWatcher
{

    public RecyclerView recyclerView;

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
        recyclerView = (RecyclerView)findViewById(R.id.addNewTaskRecyclerView);

        lm = new LinearLayoutManager(this);

        addNewTaskAdapter = new AddNewTaskAdapter(this, getFragmentManager());



        categoryList = new ArrayList<>();
        categoryList = new ArrayList<>();
        categoryList.add("To-Do");
        categoryList.add("Work");
        categoryList.add("Travel");
        categoryList.add("Home");


        recyclerView.setAdapter(addNewTaskAdapter);
        recyclerView.setLayoutManager(lm);



    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }
}
