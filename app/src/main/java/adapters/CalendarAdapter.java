package adapters;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.felipe.app.R;

import java.util.List;

import models.Day;

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

        holder.text.setText(day.toString());
        return view;
    }

    /**
     * Created by felipe on 14/09/17.
     */
    public static class EventsViewHolder extends RecyclerView.ViewHolder {
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
}
