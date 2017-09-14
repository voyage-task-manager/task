package com.example.felipe.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.Day;
import models.Task;

public class CalendarFragment extends Fragment {

    Activity activity;
    CalendarAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            ArrayList<Day> days = new ArrayList<>();
            ArrayList<Task> tasks = bundle.getParcelableArrayList("tasks");

            int today = bundle.getInt("today");
            for(int i=today; i <= bundle.getInt("last"); i++) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, i);
                ArrayList<Task> events = new ArrayList<>(tasksByDay(tasks, i));
                if (events.size() == 0) continue;
                Day d = new Day(c, events);
                days.add(d);
            }
            adapter = new CalendarAdapter(activity, days);
        }
    }

    public List<Task> tasksByDay (List<Task> tasks, int day) {
        ArrayList<Task> arr = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for(Task t : tasks) {
            calendar.setTime(t.getDate());
            if (calendar.get(Calendar.DAY_OF_MONTH) == day)
                arr.add(t);
        }
        return arr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ListView list = (ListView) view.findViewById(R.id.scroll);
        list.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }
}
