package com.example.felipe.app;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import models.Task;

public class MainFragment extends Fragment {

    private LinearLayout linear;
    private ArrayList<Task> tasks;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        linear = activity.findViewById(R.id.linear);
        Bundle bundle = getArguments();
        if (bundle != null) {
            tasks = bundle.getParcelableArrayList("tasks");
            PersonalAdapter adapter = new PersonalAdapter(getActivity(), tasks);
            for (int i=0; i<tasks.size(); i++)
                if (linear != null)
                    linear.addView(adapter.getView(i, null, linear));
                else
                    Log.d("INFO::", "NOOOOO!");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (Activity) context;
    }
}
