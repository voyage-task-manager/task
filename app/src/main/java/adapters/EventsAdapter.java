package adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.felipe.app.R;

import java.util.Calendar;
import java.util.List;

import models.Day;
import models.Task;

/**
 * Created by felipe on 14/09/17.
 */

public class EventsAdapter {

    private static View view;
    private static Activity act;

    public static void init (Activity act, ViewGroup parent) {
        view = act.getLayoutInflater().inflate(R.layout.day_event_layout, parent, false);
        EventsAdapter.act = act;
    }

    public static View inflate (Task item) {

        View v = act.getLayoutInflater().inflate(R.layout.day_event_layout, null);

        TextView title = (TextView) v.findViewById(R.id.event_title);
        TextView clock = (TextView) v.findViewById(R.id.clock_event);
        GradientDrawable shape = (GradientDrawable) v.findViewById(R.id.circle).getBackground();

        title.setText(item.getTitle());
        shape.setColor(Color.parseColor(item.getColor()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getDate());
        int HOUR = Calendar.HOUR_OF_DAY;
        int MIN = Calendar.MINUTE;

        String hour = null;

        if (item.isAllDay()) {
            hour = "Dia todo";
        } else {
            hour = String.format("%02d:%02d", calendar.get(HOUR), calendar.get(MIN));
            calendar.setTimeInMillis(item.getEnd());
            hour += " - " + String.format("%02d:%02d", calendar.get(HOUR), calendar.get(MIN));
        }
        clock.setText(hour);

        return v;
    }

//    @Override
//    public View getView(int i, View convertView, ViewGroup parent) {
//
//        View view;
//        EventsViewHolder holder;
//
//        if( convertView == null) {
//            view = act.getLayoutInflater().inflate(R.layout.day_event_layout, parent, false);
//            holder = new EventsViewHolder(view);
//            view.setTag(holder);
//        } else {
//            view = convertView;
//            holder = (EventsViewHolder) view.getTag();
//        }
//
//        Task item = tasks.get(i);
//        holder.title.setText(item.getTitle());
//        holder.shape.setColor(Color.parseColor(item.getColor()));
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(item.getDate());
//        int HOUR = Calendar.HOUR_OF_DAY;
//        int MIN = Calendar.MINUTE;
//        String hour = String.format("%02d:%02d", calendar.get(HOUR), calendar.get(MIN));
//        calendar.setTimeInMillis(item.getEnd());
//        hour += " - " + String.format("%02d:%02d", calendar.get(HOUR), calendar.get(MIN));
//        holder.clock.setText(hour);
//
//        return view;
//    }

    /*
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = act.getLayoutInflater().inflate(R.layout.day_event_layout, parent, false);
        CalendarAdapter.EventsViewHolder holder = new CalendarAdapter.EventsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        CalendarAdapter.EventsViewHolder holder = (CalendarAdapter.EventsViewHolder) viewHolder;
        Task item = tasks.get(position);

        holder.title.setText(item.getTitle());
        holder.shape.setColor(Color.parseColor(item.getColor()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(item.getDate());
        int HOUR = Calendar.HOUR_OF_DAY;
        int MIN = Calendar.MINUTE;
        String hour = String.format("%02d:%02d", calendar.get(HOUR), calendar.get(MIN));
        calendar.setTimeInMillis(item.getEnd());
        hour += " - " + String.format("%02d:%02d", calendar.get(HOUR), calendar.get(MIN));
        holder.clock.setText(hour);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    } */
}