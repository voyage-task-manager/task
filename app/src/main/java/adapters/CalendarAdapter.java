package adapters;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.felipe.app.R;

import java.util.List;

import models.Day;
import models.Task;

/**
 * Created by felipe on 11/09/17.
 */

public class CalendarAdapter extends BaseAdapter {

    private final List<Day> days;
    private final Activity act;
    private final int TAG = 443;

    public CalendarAdapter (Activity act, List<Day> days) {
        this.days = days;
        this.act = act;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
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
            EventsAdapter.init(act, (ViewGroup) view);
            holder = new ViewHolder(view, act);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        Day day = days.get(position);
        List<Task> events = day.getEvents();
        holder.layout.removeAllViews();
        for (Task t : events)
            holder.layout.addView( EventsAdapter.inflate(t) );
        view.findViewById(R.id.events).setTag(position);
        //holder.layout.setAdapter(new EventsAdapter(day.getEvents(), act));

        holder.text.setText(day.toString());
        return view;
    }
}
