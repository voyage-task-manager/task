package com.example.felipe.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import adapters.PersonalAdapter;
import adapters.RotineAdapter;
import models.Task;

public class MainFragment extends Fragment {

    private LinearLayout linear;
    private ArrayList<Task> tasks;
    private Activity activity;
    private ImageView rotine_icon;
    private TextView rotine_name, rotine_hour;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        linear = activity.findViewById(R.id.linear);
        rotine_icon = (ImageView) activity.findViewById(R.id.rotine_icon);
        rotine_hour = (TextView) activity.findViewById(R.id.rotine_hour);
        rotine_name = (TextView) activity.findViewById(R.id.rotine_name);
        Task now = null;
        Task next = null;
        long min = 3600000 * 24;

        Bundle bundle = getArguments();
        if (bundle != null) {
            tasks = bundle.getParcelableArrayList("tasks");

            Calendar instance = Calendar.getInstance();
            long time = instance.getTimeInMillis();
            for (Task t : tasks) {
                if (time >= t.getDate() && time <= t.getEnd()) now = t;
                else if (time > t.getDate()) continue;

                if (t.getDate() - time < min) {
                    next = t;
                    min = t.getDate() - time;
                }
            }

            if (now == null) {
                rotine_name.setText("Este horário é livre para fazer o que quiser");
                if (next != null)
                    rotine_hour.setText( String.format(Locale.getDefault(), "Próxima tarefa às %tR", next.getDate()) );
                else {
                    String periodo = "o dia";
                    if (instance.get(Calendar.HOUR_OF_DAY) > 18)
                        periodo = "a noite";
                    else if (instance.get(Calendar.HOUR_OF_DAY) > 12)
                        periodo = "a tarde";
                    rotine_hour.setText( "Você tem " + periodo + " livre, aproveite!");
                }
                rotine_icon.setImageDrawable( ContextCompat.getDrawable(getActivity(), R.drawable.ic_very_happy_black_24px) );
            } else {
                rotine_name.setText("Agora é hora de trabalhar na tarefa " + now.getTitle());
                rotine_hour.setText( String.format(Locale.getDefault(), "Das %tR até %tR", now.getDate(), now.getEnd()) );
                rotine_icon.setImageDrawable( ContextCompat.getDrawable(getActivity(), R.drawable.ic_work_black_24px) );
            }

            /*PersonalAdapter adapter = new PersonalAdapter(getActivity(), tasks);
            for (int i=0; i<tasks.size(); i++)
                if (linear != null)
                    linear.addView(adapter.getView(i, null, linear));
                else
                    Log.d("INFO::", "NOOOOO!");*/
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity)
            activity = (Activity) context;
    }
}
