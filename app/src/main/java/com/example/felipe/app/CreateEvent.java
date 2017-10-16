package com.example.felipe.app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

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
import models.Work;

public class CreateEvent extends AppCompatActivity implements View.OnClickListener, AccountDialog.Listener {

    private List<CalendarProvider> calendars;
    private Button date_input;
    private Button event_calendar;
    private Button event_hour;
    private EditText event_description;
    private EditText estimate_picker;
    private Switch active_plan;
    private EditText name_input;
    private Calendar calendar;
    private Spinner period_spinner;
    private Setting setting;
    private Waiting w;
    private CalendarProvider calendarProvider;
    Runnable onClose;

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
        setting = Setting.init(this).get(1);
        //pager = (ViewPager) findViewById(R.id.diff);
        //pager.setAdapter(new DifficultyAdapter(this));
        name_input = (EditText) findViewById(R.id.event_name);
        event_description = (EditText) findViewById(R.id.event_description);
        date_input = (Button) findViewById(R.id.event_date);
        event_calendar = (Button) findViewById(R.id.event_calendar);
        event_hour = (Button) findViewById(R.id.event_hour);
        event_hour.setText(formatHour(calendar));
        event_hour.setOnClickListener(listenerHour());
        estimate_picker = (EditText) findViewById(R.id.estimate_picker);
        //estimate_picker.setTypeface(null, Typeface.NORMAL);
        event_calendar.setTypeface(null, Typeface.NORMAL);
        date_input.setTypeface(null, Typeface.NORMAL);
        event_hour.setTypeface(null, Typeface.NORMAL);
        date_input.setText(format(calendar));
        date_input.setOnClickListener(this);
        period_spinner = (Spinner) findViewById(R.id.period_spinner);
        active_plan = (Switch) findViewById(R.id.active_plan);
        active_plan.setOnCheckedChangeListener(toggle());
        calendars = CalendarProvider.myCalendars(this);
        event_calendar.setOnClickListener(showModal());
        toggle(false);
        setCalendar(calendars.get(0));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.period, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        period_spinner.setAdapter(adapter);
        period_spinner.setSelection(0);
    }

    private void setSelected () {
    }

    private View.OnClickListener showModal() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("calendars", new ArrayList<Parcelable>(calendars));
                FragmentManager fm = getSupportFragmentManager();
                AccountDialog dialog = new AccountDialog();
                dialog.setArguments(bundle);
                dialog.show(fm, "dialog_fragment");
            }
        };
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

    public CompoundButton.OnCheckedChangeListener toggle () {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                toggle(b);
            }
        };
    }

    /*  */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void createEvent(View view) throws InterruptedException {
        w = new Waiting(this, active_plan.isChecked() ? "Configurando seu tempo" : "Salvando evento na agenda");
        if (calendars.size() == 0)
            return;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 1);
        Graph graph = new Graph(c, calendar, setting, this);
        Task task = new Task(name_input.getText().toString(), calendar.getTimeInMillis(), calendar.getTimeInMillis() + 3600000);
        task.setDescription(event_description.getText() != null ? event_description.getText().toString() : "");
        task.setCalendarID(calendarProvider.getId());

        // Não planeja a agenda do usuário
        if (!active_plan.isChecked()) {
            long _id = task.record(this);
            if (_id == -1)
                Log.d("INFO::", "Erro ao salvar no calendar");
            close();
            return;
        }

        String estimateText = "0";
        if (estimate_picker.getText() != null);
            estimateText = estimate_picker.getText().toString();
        int estimative = Integer.parseInt(estimateText);
        int estimate = Work.translatePayload(estimative, (int) period_spinner.getSelectedItemId(), setting);
        List<Task> l = graph.organize(task, estimate);

        if (l.size() == 0) {
            final Activity act = this;
            onClose = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(act, "Não há tempo na sua agenda para executar esta tarefa :(", Toast.LENGTH_LONG).show();
                }
            };
            close();
            return;
        }

        long _id = task.record(this);
        if (_id == -1) {
            Log.d("INFO::", "Erro ao salvar no calendar");
            close();
            return;
        }

        Work work = new Work(this);
        work.setPayload(estimative);
        work.setPayloadType( (int) period_spinner.getSelectedItemId() );
        work.setTask(task.getID());
        work.setReference(-1);
        if (!work.save()) {
            Log.d("INFO::", "Erro ao salvar do DB :((");
            close();
            return;
        }

        work.setReference(task.getID());
        for (Task t : l) {
            _id = t.record(this);
            if (_id == -1) {
                Log.d("INFO::", "Erro ao salvar o fragmento no calendar :((");
                close();
                return;
            }
            work.setTask(t.getID());
            if (!work.save()) {
                Log.d("INFO::", "Erro ao salvar o fragmento no DB :((");
                close();
                return;
            }
        }

        close();
    }

    private void close() {
        TabActivity.prototype.reload();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        DatePickerDialog picker = new DatePickerDialog(this, getListener(view), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }

    public void toggle (boolean active) {
        if (!active) {
            estimate_picker.setTextColor(ContextCompat.getColor(this, R.color.inactive));
            estimate_picker.setHintTextColor(ContextCompat.getColor(this, R.color.inactive));
        }
        else {
            estimate_picker.setTextColor(ContextCompat.getColor(this, R.color.text));
            estimate_picker.setHintTextColor(ContextCompat.getColor(this, R.color.disabled));
        }

        estimate_picker.setEnabled(active);
        period_spinner.setEnabled(active);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (w != null)
            w.close();
        if (onClose != null)
            (new Handler()).post(onClose);
    }

    @Override
    public void setCalendar(CalendarProvider calendar) {
        this.calendarProvider = calendar;
        event_calendar.setText(calendar.getName());
    }
}
