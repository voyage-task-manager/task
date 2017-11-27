package adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import voyage.task.zerodois.app.R;

import java.util.List;

import models.CalendarProvider;

/**
 * Created by felipe on 13/10/17.
 */

public class CalendarAccountAdapter extends BaseAdapter {

    private List<CalendarProvider> calendars;
    private Activity act;
    private Listener listener;

    public interface Listener {
        void onClick(CalendarProvider calendarProvider);
    }

    public CalendarAccountAdapter(Activity act, List<CalendarProvider> calendars, Listener listener) {
        this.calendars = calendars;
        this.act = act;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return calendars.size();
    }

    @Override
    public Object getItem(int i) {
        return calendars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view;
        CalendarAccountHolder holder;

        if( convertView == null) {
            view = act.getLayoutInflater().inflate(R.layout.account_display, parent, false);
            holder = new CalendarAccountHolder(view, act);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (CalendarAccountHolder) view.getTag();
        }

        final CalendarProvider cal = calendars.get(i);
        holder.name.setText(cal.getName());
        holder.email.setText(cal.getAccount());
        holder.shape.setColor(Color.parseColor(cal.getColor()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(cal);
            }
        });

        return view;
    }
}
