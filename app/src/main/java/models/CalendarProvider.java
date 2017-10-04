package models;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by felipe on 26/09/17.
 */

public class CalendarProvider {

    private static final String[] EVENT_PROJECTION = new String[]{
            Calendars._ID,
            Calendars.ACCOUNT_NAME,
            Calendars.CALENDAR_DISPLAY_NAME,
            Calendars.OWNER_ACCOUNT
    };

    private int id;
    private String account, name, owner;

    public static List<CalendarProvider> calendars (Activity act) {
        Cursor cur = null;
        ContentResolver cr = act.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return new ArrayList<>();

        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
        return readCalendar(cur);
    }

    private static List<CalendarProvider> readCalendar (Cursor cur) {
        List<CalendarProvider> arr = new ArrayList<>();
        while (cur.moveToNext()) {
            CalendarProvider item = new CalendarProvider();
            item.setId(cur.getInt(0));
            item.setAccount(cur.getString(1));
            item.setName(cur.getString(2));
            item.setOwner(cur.getString(3));
            arr.add(item);
        }
        return arr;
    }

    public static List<Task> readCalendar(ContentResolver resolver) {
        return readCalendar(Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.YEAR), resolver);
    }

    public static List<Task> readCalendar(int month, ContentResolver resolver) {
        return readCalendar(month, Calendar.getInstance().get(Calendar.YEAR), resolver);
    }

    public static List<Task> readCalendar(Calendar init, Calendar end, ContentResolver resolver) {
        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        long dtStart = init.getTimeInMillis();
        if (end == null) {
            end = init;
            end.set( Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH) );
        }
        long dtEnd = end.getTimeInMillis();

        ContentUris.appendId(eventsUriBuilder, dtStart);
        ContentUris.appendId(eventsUriBuilder, dtEnd);
        Uri eventsUri = eventsUriBuilder.build();

        String filter = "( " + CalendarContract.Instances.BEGIN + " >= ? )";
        String[] values = new String[]{ Long.toString(dtStart) };

        Cursor cursor = null;
        String[] fields = new String[] {
                CalendarContract.Events.TITLE, // 0
                CalendarContract.Instances.BEGIN, // 1
                CalendarContract.Events.DISPLAY_COLOR, // 2
                CalendarContract.Instances.END, // 3
                CalendarContract.Events.ALL_DAY // 4
        };

        cursor = resolver.query(eventsUri, fields, filter, values, CalendarContract.Instances.BEGIN + " ASC");
        //cursor = CalendarContract.Instances.query(getContentResolver(), fields, dtStart, dtEnd);

        return handleCalendar(cursor);
    }

    public static List<Task> readCalendar(int month, int year, ContentResolver resolver) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.YEAR, year);

        return readCalendar(c, null, resolver);
    }

    public static List<Task> handleCalendar (Cursor cur) {
        List<Task> list = new ArrayList<>();
        while (cur.moveToNext()) {
            String color = String.format("#%06X", (0xFFFFFF & cur.getInt(2)));
            Task t = new Task(cur.getString(0), cur.getLong(1), color);
            t.setEnd(cur.getLong(3));
            t.setAllDay(cur.getInt(4));
            list.add(t);
        }
        return list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
