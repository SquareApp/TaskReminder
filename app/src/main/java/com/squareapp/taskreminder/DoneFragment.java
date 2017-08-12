package com.squareapp.taskreminder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Valentin Purrucker on 12.08.2017.
 */

public class DoneFragment extends Fragment
{

    private RecyclerView recyclerView;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.done_fragment, container, false);











        return rootView;
    }
}
