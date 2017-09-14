package com.example.felipe.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by felipe on 13/09/17.
 */

public class ViewHolder {

    final RecyclerView layout;
    final TextView text;

    public ViewHolder(View view) {
        layout = (RecyclerView) view.findViewById(R.id.events);
        text = (TextView) view.findViewById(R.id.date);
    }
}
