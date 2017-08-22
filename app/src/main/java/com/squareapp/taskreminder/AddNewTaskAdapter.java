package com.squareapp.taskreminder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Valentin Purrucker on 01.08.2017.
 */

public class AddNewTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private Context context;

    private LayoutInflater inflater;

    public int amn = 2;

    public EditTextCardViewHolder editTextCardViewHolder;

    public DateCardViewHolder dateCardViewHolder;

    public RepetitionCardViewHolder repetitionCardViewHolder;



    private android.app.FragmentManager fragmentManager;

    public AddNewTaskAdapter(Context context, android.app.FragmentManager fragmentManager)
    {
        this.context = context;

        inflater = LayoutInflater.from(context);

        this.fragmentManager = fragmentManager;

    }





    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if(viewType == 0)
        {
            View editTextView = inflater.inflate(R.layout.add_new_task_edittext_card, parent, false);
            EditTextCardViewHolder editTextCardViewHolder = new EditTextCardViewHolder(editTextView, context);
            return editTextCardViewHolder;
        }
        else
        {
            if(viewType == 1)
            {
                View dateView = inflater.inflate(R.layout.add_new_task_date_card, parent, false);
                DateCardViewHolder dateCardViewHolder = new DateCardViewHolder(dateView, fragmentManager);
                return dateCardViewHolder;
            }
            else
            {
                View repetitionView = inflater.inflate(R.layout.add_new_task_repetition, parent, false);
                RepetitionCardViewHolder repetitionCardViewHolder = new RepetitionCardViewHolder(repetitionView, context, this);
                return repetitionCardViewHolder;
            }
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(getItemViewType(position) == 0)
        {
            editTextCardViewHolder = (EditTextCardViewHolder)holder;
        }
        else
        {
            if(getItemViewType(position) == 1)
            {
                dateCardViewHolder = (DateCardViewHolder)holder;
            }
            else
            {
                repetitionCardViewHolder = (RepetitionCardViewHolder)holder;
            }

        }
    }

    @Override
    public int getItemCount()
    {
        return amn;
    }

    @Override
    public int getItemViewType(int position)
    {


        int viewType = 0;


        if(position == 0)
        {
            viewType = 0;
        }
        else
        if(position == 1)
        {
            viewType = 1;
        }
        else
        {
            viewType = 2;
        }





        return viewType;
    }

}
