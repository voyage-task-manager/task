package com.example.felipe.app;

import android.content.ContentUris;
import android.database.Cursor;
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
import java.util.List;

import models.Day;
import models.Task;

public class TabActivity extends AppCompatActivity {

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
        Day day = new Day(c);
        bundle.putInt("today", c.get(Calendar.DAY_OF_MONTH));
        bundle.putInt("last", Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        CalendarFragment frag = new CalendarFragment();
        frag.setArguments(bundle);
        fm = getSupportFragmentManager();
        clear(fm, frag);
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.content, frag);
        ft.commit();
    }

    public List<Task> readCalendar() {
        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();

        long dtStart = System.currentTimeMillis();
        long dtEnd = System.currentTimeMillis() + (24*3600*24*1000);

        ContentUris.appendId(eventsUriBuilder, dtStart);
        ContentUris.appendId(eventsUriBuilder, dtEnd);
        Uri eventsUri = eventsUriBuilder.build();

        String filter = "( " + CalendarContract.Instances.BEGIN + " >= ? )";
        String[] values = new String[]{ Long.toString(dtStart) };

        Cursor cursor = null;
        cursor = getContentResolver().query(eventsUri, new String[]{CalendarContract.Events.TITLE, CalendarContract.Instances.BEGIN}, filter, values, CalendarContract.Instances.BEGIN + " ASC");
        return handleCalendar(cursor);
    }

    public List<Task> handleCalendar (Cursor cur) {
        List<Task> list = new ArrayList<>();
        while (cur.moveToNext()) {
            list.add(new Task(cur.getString(0)));
        }
        return list;
    }
}
