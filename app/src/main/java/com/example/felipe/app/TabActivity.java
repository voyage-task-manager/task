package com.example.felipe.app;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.ParcelableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Day;
import models.Task;

public class TabActivity extends AppCompatActivity implements CalendarFragment.Listener {

    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private FragmentManager fm;
    Fragment atual;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    home();
                    return true;
                case R.id.navigation_calendar:
                    calendar();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        home();
    }

    public void home () {
        List<Task> t = readCalendar();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("tasks", new ArrayList<Parcelable>(t));
        MainFragment frag = new MainFragment();
        frag.setArguments(bundle);
        fm = getSupportFragmentManager();
        clear(fm, frag);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content, frag);
        ft.commit();
    }

    public void clear (FragmentManager fm, Fragment frag) {
        if (atual != null)
            fm.beginTransaction().remove(atual).commit();
        atual = frag;
    }

    public void calendar () {
        Bundle bundle = new Bundle();
        Calendar c = Calendar.getInstance();
        bundle.putInt("today", c.get(Calendar.DAY_OF_MONTH));
        bundle.putInt("month", c.get(Calendar.MONTH));
        bundle.putInt("year", c.get(Calendar.YEAR));
        bundle.putInt("last", c.getActualMaximum(Calendar.DAY_OF_MONTH));
        bundle.putParcelableArrayList("days", new ArrayList<Parcelable>( load(c.get(Calendar.MONTH), c.get(Calendar.YEAR)) ));
        CalendarFragment frag = new CalendarFragment();
        frag.setArguments(bundle);
        fm = getSupportFragmentManager();
        clear(fm, frag);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content, frag);
        ft.commit();
    }

    public List<Task> readCalendar() {
        return readCalendar(Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR));
    }
    public List<Task> readCalendar(int month) {
        return readCalendar(month, Calendar.getInstance().get(Calendar.YEAR));
    }
    public List<Task> readCalendar(int month, int year) {
        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.YEAR, year);
        long dtStart = c.getTimeInMillis();
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        long dtEnd = c.getTimeInMillis();

        ContentUris.appendId(eventsUriBuilder, dtStart);
        ContentUris.appendId(eventsUriBuilder, dtEnd);
        Uri eventsUri = eventsUriBuilder.build();

        String filter = "( " + CalendarContract.Instances.BEGIN + " >= ? )";
        String[] values = new String[]{ Long.toString(dtStart) };

        Cursor cursor = null;
        String[] fields = new String[] {
                CalendarContract.Events.TITLE,
                CalendarContract.Instances.BEGIN,
                CalendarContract.Events.DISPLAY_COLOR,
                CalendarContract.Instances.END
        };

        cursor = getContentResolver().query(eventsUri, fields, filter, values, CalendarContract.Instances.BEGIN + " ASC");
        //cursor = CalendarContract.Instances.query(getContentResolver(), fields, dtStart, dtEnd);

        return handleCalendar(cursor);
    }

    public List<Task> handleCalendar (Cursor cur) {
        List<Task> list = new ArrayList<>();
        while (cur.moveToNext()) {
            String color = String.format("#%06X", (0xFFFFFF & cur.getInt(2)));
            Task t = new Task(cur.getString(0), cur.getLong(1), color);
            t.setEnd(cur.getLong(3));
            list.add(t);
        }
        return list;
    }

    public ArrayList<Day> load () {
        Calendar c = Calendar.getInstance();
        return load(c.get(Calendar.MONTH), c.get(Calendar.YEAR));
    }

    @Override
    public ArrayList<Day> load(int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, 1);
        List<Task> tasks = readCalendar(month, year);
        List<Day> list = Day.create(c, c.getActualMaximum(Calendar.DAY_OF_MONTH), tasks);
        return new ArrayList<>(list);
    }

    @Override
    public void callEventRecord() {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }
}
