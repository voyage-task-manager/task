package com.example.felipe.app;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import models.Setting;

public class ConfigFragment extends Fragment {

    private Activity activity;
    private List<Setting> settings;
    private Setting mySetting;
    private List<View> days;
    private boolean[] actives;
    private TextView[] inputs;
    private boolean hasChange = false;
    private Calendar[] input_hours;
    private final int[] order = new int[]{1, 2, 3, 4, 5, 6, 0};

    interface Listener {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        actives = new boolean[]{ false, false, false, false, false, false, false };
        if (bundle != null) {
            //ArrayList<Day> days = bundle.getParcelableArrayList("days");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_config, container, false);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.days_active);

        days = new ArrayList<>();
        inputs = new TextView[4];
        input_hours = new Calendar[4];

        inputs[0] = (TextView) view.findViewById(R.id.settings_wake);
        inputs[0].setHint( mySetting.getWake()[0] );

        inputs[1] = (TextView) view.findViewById(R.id.settings_lunch);
        inputs[1].setHint( mySetting.getLaunch()[0] );

        inputs[2] = (TextView) view.findViewById(R.id.settings_dinner);
        inputs[2].setHint( mySetting.getDinner()[0] );


        inputs[3] = (TextView) view.findViewById(R.id.settings_sleep);
        inputs[3].setHint( mySetting.getSleep()[0] );

        for (int i = 0; i < inputs.length; i++) {
            input_hours[i] = Calendar.getInstance();
            String[] parts = inputs[i].getHint().toString().split(":");
            input_hours[i].set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
            input_hours[i].set(Calendar.MINUTE, Integer.parseInt(parts[1]));
            inputs[i].setOnClickListener(onClick(inputs[i], input_hours[i]));
        }

        int index = 0;
        Calendar c = Calendar.getInstance();

        for (int i : order) {
            c.set(Calendar.DAY_OF_WEEK, i);
            View v = inflater.inflate(R.layout.settings_day, container, false);
            days.add(v);
            TextView t = v.findViewById(R.id.text_day);
            String text = String.format(Locale.getDefault(), "%ta", c.getTime());
            t.setText(text);
            t.setOnClickListener(onClickDay(index++));
            layout.addView(v);
        }

        List<Integer> list = mySetting.getDays();
        for (int i : list)
            if (i == 0) setActive(6);
            else setActive(i-1);

        return view;
    }

    private void setActive (int index) {
        setActive(index, false);
    }

    private void setActive (int index, boolean change) {
        if (change)
            notifyChange();
        View container = days.get(index);
        TextView text = container.findViewById(R.id.text_day);
        View border = container.findViewById(R.id.bottom_border);

        int color = actives[index] ? ContextCompat.getColor(activity, R.color.disabled) : ContextCompat.getColor(activity, R.color.colorPrimary);
        int color2 = actives[index] ? ContextCompat.getColor(activity, R.color.border) : color;
        actives[index] = !actives[index];

        text.setTextColor( color );
        border.setBackgroundColor( color2 );
    }

    private View.OnClickListener onClick (final TextView view, final Calendar calendar) {
        final Activity act = activity;
        final ConfigFragment prototype = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyChange();
                TimePickerDialog picker = new TimePickerDialog(act, prototype.getListener((TextView) view, calendar), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                picker.show();
            }
        };
    }

    private void notifyChange () {
        hasChange = true;
    }

    public TimePickerDialog.OnTimeSetListener getListener(final TextView view, final Calendar calendar) {
        return new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hasChange = true;
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), i, i1);
                view.setText(formatHour(calendar));
            }
        };
    }

    public String formatHour(Calendar calendar) {
        return String.format(Locale.getDefault(), "%tH:%tM", calendar.getTime(), calendar.getTime());
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (!hasChange)
            return;

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.wait);
        dialog.setTitle("Waiting...");
        //dialog.show();

        List<Integer> days = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            if (actives[i]) days.add(order[i]);

        mySetting.setDays(days);
        mySetting.setWake(format(0));
        mySetting.setLaunch(format(1));
        mySetting.setDinner(format(2));
        mySetting.setSleep(format(3));
        mySetting.save();

        //dialog.dismiss();
    }

    private String[] format (int index) {
        String[] vec = new String[2];
        vec[0] = String.format(Locale.getDefault(), "%tR", input_hours[index].getTime());
        input_hours[index].add(Calendar.HOUR_OF_DAY, 1);
        vec[1] = String.format(Locale.getDefault(), "%tR", input_hours[index].getTime());
        return vec;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (Activity) context;
        settings = Setting.init(context); // 0 - Default; 1 - Editable
        mySetting = settings.get(1);
    }

    public View.OnClickListener onClickDay (final int index) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActive(index, true);
            }
        };
    }
}
