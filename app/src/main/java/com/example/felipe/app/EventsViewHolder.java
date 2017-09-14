package com.example.felipe.app;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by felipe on 14/09/17.
 */

public class EventsViewHolder extends RecyclerView.ViewHolder {
    final TextView title;
    final TextView clock;
    final GradientDrawable shape;


    public EventsViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.event_title);
        clock = (TextView) view.findViewById(R.id.clock_event);
        shape = (GradientDrawable) view.findViewById(R.id.circle).getBackground();
    }
}

