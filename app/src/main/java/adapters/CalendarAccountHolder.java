package adapters;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.TextView;

import voyage.task.zerodois.app.R;

public class CalendarAccountHolder {

    final TextView name;
    final TextView email;
    final GradientDrawable shape;


    public CalendarAccountHolder (View view, Activity act) {
        name = (TextView) view.findViewById(R.id.calendar_name);
        email = (TextView) view.findViewById(R.id.calendar_email);
        shape = (GradientDrawable) view.findViewById(R.id.circle_event).getBackground();
    }
}
