package com.example.felipe.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import components.Waiting;
import models.CalendarProvider;
import models.Graph;
import models.Setting;
import models.Task;

public class CreateEvent extends AppCompatActivity implements View.OnClickListener {

    Button date_input;
    Button event_hour;
    EditText estimate_picker;
    EditText name_input;
    Calendar calendar;
    Spinner period_spinner;
    Setting setting;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String format(Calendar calendar) {
        Date t = calendar.getTime();
        String f = String.format(Locale.getDefault(), "%ta, %td de %tB de %tY", t, t, t, t);
        String[] w = f.split("\\s+");
        f = w[0].toUpperCase().charAt(0) + w[0].substring(1, w[0].length());
        for (int i = 1; i < w.length; i++)
            f += " " + (w[i].equals("de") ? w[i] : w[i].toUpperCase().charAt(0) + w[i].substring(1, w[i].length()));
        return f;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String formatHour(Calendar calendar) {
        //return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        return String.format(Locale.getDefault(), "%tH:%tM", calendar.getTime(), calendar.getTime());
    }

    public DatePickerDialog.OnDateSetListener getListener(final View v) {
        return new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            private void updateLabel(Button b) {
                b.setText(format(calendar));
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel((Button) v);
            }
        };
    }

    public TimePickerDialog.OnTimeSetListener getListenerHour(final View view) {
        return new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), i, i1);
                ((Button) view).setText(formatHour(calendar));
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Utils.setSystemBarLight(this, ContextCompat.getColor(this, R.color.colorPrimaryDarker));
        calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("America/Sao_Paulo"));

        setting = new Setting(this);
        setting = setting.init().get(1);


        //pager = (ViewPager) findViewById(R.id.diff);
        //pager.setAdapter(new DifficultyAdapter(this));
        name_input = (EditText) findViewById(R.id.event_name);
        date_input = (Button) findViewById(R.id.event_date);
        event_hour = (Button) findViewById(R.id.event_hour);
        event_hour.setText(formatHour(calendar));
        event_hour.setOnClickListener(listenerHour());
        estimate_picker = (EditText) findViewById(R.id.estimate_picker);
        //estimate_picker.setTypeface(null, Typeface.NORMAL);
        date_input.setTypeface(null, Typeface.NORMAL);
        event_hour.setTypeface(null, Typeface.NORMAL);
        date_input.setText(format(calendar));
        date_input.setOnClickListener(this);
        period_spinner = (Spinner) findViewById(R.id.period_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        period_spinner.setAdapter(adapter);
        period_spinner.setSelection(0);
    }

    public void close(View v) {
        this.finish();
    }

    public View.OnClickListener listenerHour() {
        final CreateEvent act = this;
        return new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                TimePickerDialog picker = new TimePickerDialog(act, getListenerHour(view), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                picker.show();
            }
        };
    }

    /*  */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createEvent(View view) {

        Waiting w = new Waiting(this, "Configurando seu tempo");

        List<CalendarProvider> calendars = CalendarProvider.calendars(this);
        if (calendars.size() == 0)
            return;

        Graph graph = new Graph(Calendar.getInstance(), calendar, setting, this);
        Task task = new Task(name_input.getText().toString(), calendar.getTimeInMillis(), calendar.getTimeInMillis() + 3600000);
        List<Task> l = graph.organize(task, Integer.parseInt(estimate_picker.getText().toString()));
        if (l != null)
            Log.d("INFO::", "Ate aqui ta tudo bem -> " + l.size());

        Calendar c = Calendar.getInstance();
        for (Task t : l) {
            c.setTimeInMillis(t.getDate());
            String start = String.format(Locale.getDefault(), "%tB, %td %tH:%tM até ", c.getTime(), c.getTime(), c.getTime(), c.getTime());
            c.setTimeInMillis(t.getEnd());
            start += String.format(Locale.getDefault(), "%tH:%tM", c.getTime(), c.getTime());
            Log.d("INFO::", t.getTitle() + " " + start);
        }
        /*if (task.record(this,calendars.get(0).getId()) == null);
            return;*/
        /*
        CalendarProvider c = calendars.get(0);

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, calendar.getTimeInMillis());
        values.put(Events.DTEND, calendar.getTimeInMillis() + 3600000);
        values.put(Events.TITLE, name_input.getText().toString());
        values.put(Events.DESCRIPTION, "Group workout");
        values.put(Events.CALENDAR_ID, c.getId());
        values.put(Events.EVENT_TIMEZONE, "UTC");

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Uri uri = cr.insert(Events.CONTENT_URI, values);
        Toast.makeText(this, "Tarefa cadastrada :)", Toast.LENGTH_LONG).show();
        TabActivity.prototype.reload();
        finish();
        */
        w.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        DatePickerDialog picker = new DatePickerDialog(this, getListener(view), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }
}
