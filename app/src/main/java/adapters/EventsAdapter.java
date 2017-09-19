package adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.felipe.app.R;

import java.util.Calendar;
import java.util.List;

import models.Day;
import models.Task;

/**
 * Created by felipe on 14/09/17.
 */

public class EventsAdapter extends BaseAdapter {

    private List<Task> tasks;
    private Activity act;

    public EventsAdapter (List<Task> tasks, Activity act) {
        this.tasks = tasks;
        this.act = act;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view;
        EventsViewHolder holder;

        if( convertView == null) {
            view = act.getLayoutInflater().inflate(R.layout.day_event_layout, parent, false);
            holder = new EventsViewHolder(view);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (EventsViewHolder) view.getTag();
        }

        Task item = tasks.get(i);

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

        return view;
    }

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