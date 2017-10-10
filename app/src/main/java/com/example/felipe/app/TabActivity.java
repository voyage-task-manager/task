package com.example.felipe.app;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import android.Manifest;
import java.util.List;
import java.util.TimeZone;

import models.CalendarProvider;
import models.Day;
import models.Task;

public class TabActivity extends AppCompatActivity implements CalendarFragment.Listener {

    private TextView mTextMessage;
    private BottomNavigationView navigation;
    private FragmentManager fm;
    private final int PERMISSIONS_REQUEST_WRITE_CALENDAR = 788;
    public static TabActivity prototype;
    ArrayList<ArrayList<Day>> start;
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
                case R.id.navigation_settings:
                    config();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prototype = this;
        setContentView(R.layout.activity_tab);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, PERMISSIONS_REQUEST_WRITE_CALENDAR);
            return;
        }

        start = new ArrayList<>();
        reload();
        home();
    }

    public void home () {
        List<Task> t = CalendarProvider.readCalendar(getContentResolver());
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

    public void config () {
        Bundle bundle = new Bundle();
        ConfigFragment frag = new ConfigFragment();
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
        if (atual instanceof CalendarFragment) {
            Calendar c = Calendar.getInstance();
            ((CalendarFragment) atual).init();
            return;
        }

        Bundle bundle = new Bundle();
        Calendar c = Calendar.getInstance();
        bundle.putInt("today", c.get(Calendar.DAY_OF_MONTH));
        bundle.putInt("month", c.get(Calendar.MONTH));
        bundle.putInt("year", c.get(Calendar.YEAR));
        bundle.putInt("last", c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //bundle.putParcelableArrayList("days", new ArrayList<Parcelable>( load(c.get(Calendar.MONTH), c.get(Calendar.YEAR)) ));
        CalendarFragment frag = new CalendarFragment();
        frag.setArguments(bundle);
        fm = getSupportFragmentManager();
        clear(fm, frag);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content, frag);
        ft.commit();
    }

    public ArrayList<Day> load () {
        Calendar c = Calendar.getInstance();
        return load(c.get(Calendar.MONTH), c.get(Calendar.YEAR));
    }

    @Override
    public ArrayList<Day> load (int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, 1);
        List<Task> tasks = CalendarProvider.readCalendar(month, year, getContentResolver());
        List<Day> list = Day.create(c, c.getActualMaximum(Calendar.DAY_OF_MONTH), tasks);
        return new ArrayList<>(list);
    }

    @Override
    public void callEventRecord() {
        Intent intent = new Intent(this, CreateEvent.class);
        startActivity(intent);
    }

    @Override
    public ArrayList<ArrayList<Day>> init() {
        return start;
    }

    @Override
    public void reload () {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH), year = c.get(Calendar.YEAR);
        start.clear();
        start.add( load(month - 1 < 0 ? 11 : month-1, month - 1< 0 ? year-1 : year) );
        start.add( load(month, year) );
        start.add( load( (month+1)%12, month < 11 ? year : year + 1) );

        if (atual != null && atual instanceof CalendarFragment)
            ((CalendarFragment) atual).atualize();
    }
}
