package com.example.felipe.app;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
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

        View view;
        ViewHolder holder;

        if( convertView == null) {
            view = act.getLayoutInflater().inflate(R.layout.day_layout, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Day day = days.get(position);
        holder.layout.setAdapter(new EventsAdapter(day.getEvents(), act));
        RecyclerView.LayoutManager layout = new LinearLayoutManager(act, LinearLayoutManager.VERTICAL, false);
        holder.layout.setLayoutManager(layout);

        holder.text.setText(day.toString());
        return view;
    }
}
