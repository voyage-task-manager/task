package com.example.felipe.app;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;

import models.Task;

/**
 * Created by felipe on 14/09/17.
 */

public class EventsAdapter extends RecyclerView.Adapter {

    private List<Task> tasks;
    private Activity act;

    public EventsAdapter (List<Task> tasks, Activity act) {
        this.tasks = tasks;
        this.act = act;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = act.getLayoutInflater().inflate(R.layout.day_event_layout, parent, false);
        EventsViewHolder holder = new EventsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        EventsViewHolder holder = (EventsViewHolder) viewHolder;
        Task item = tasks.get(position);

        holder.title.setText(item.getTitle());
        holder.shape.setColor(Color.parseColor(item.getColor()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(item.getDate());
        int HOUR = Calendar.HOUR_OF_DAY;
        int MIN = Calendar.MINUTE;
        String hour = String.format("%02d:%02d", calendar.get(HOUR), calendar.get(MIN));
        calendar.setTime(item.getEnd());
        hour += " - " + String.format("%02d:%02d", calendar.get(HOUR), calendar.get(MIN));
        holder.clock.setText(hour);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}