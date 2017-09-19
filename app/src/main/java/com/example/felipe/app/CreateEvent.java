package com.example.felipe.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Locale;

public class CreateEvent extends AppCompatActivity implements View.OnClickListener {

    Button date_input;
    Calendar calendar;

    public DatePickerDialog.OnDateSetListener getListener (final View v) {
        return new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            private void updateLabel(Button b) {
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                b.setText(sdf.format(calendar.getTime()));
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Utils.setSystemBarLight(this, ContextCompat.getColor(this, R.color.colorPrimaryDarker));
        date_input = (Button) findViewById(R.id.event_date);
        calendar = Calendar.getInstance();
        date_input.setOnClickListener(this);
    }

    public void close (View v) {
        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        DatePickerDialog picker = new DatePickerDialog(this, getListener(view), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        picker.show();
    }
}
