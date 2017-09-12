package com.example.felipe.app;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import models.Day;
import models.Task;

/**
 * Created by felipe on 11/09/17.
 */

public class CalendarAdapter extends BaseAdapter {

    private final List<Day> days;
    private final Activity act;

    public CalendarAdapter (Activity act, List<Day> days) {
        this.days = days;
        this.act = act;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater()
                .inflate(R.layout.day_layout, parent, false);
        Day day = days.get(position);
        TextView text = (TextView)
                view.findViewById(R.id.date);
        text.setText("" + day.toString());
        return view;
    }

}
