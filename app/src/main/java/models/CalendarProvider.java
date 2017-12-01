package models;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by felipe on 26/09/17.
 */

public class CalendarProvider implements Parcelable {

    private static final String[] EVENT_PROJECTION = new String[]{
            Calendars._ID, // 0
            Calendars.ACCOUNT_NAME, // 1
            Calendars.CALENDAR_DISPLAY_NAME, // 2
            Calendars.OWNER_ACCOUNT, // 3
            Calendars.CALENDAR_ACCESS_LEVEL, // 4
            Calendars.ACCOUNT_TYPE, // 5
            Calendars.CALENDAR_COLOR // 6
    };

    private long id;
    private String account, name, owner, type, level, color;

    public CalendarProvider () {}

    protected CalendarProvider(Parcel in) {
        id = in.readLong();
        account = in.readString();
        name = in.readString();
        owner = in.readString();
        type = in.readString();
        level = in.readString();
        color = in.readString();
    }

    public static final Creator<CalendarProvider> CREATOR = new Creator<CalendarProvider>() {
        @Override
        public CalendarProvider createFromParcel(Parcel in) {
            return new CalendarProvider(in);
        }

        @Override
        public CalendarProvider[] newArray(int size) {
            return new CalendarProvider[size];
        }
    };

    public static List<CalendarProvider> calendars (Activity act) {
        return calendars(act, "");
    }

    public static List<CalendarProvider> calendars (Activity act, String filter) {
        Cursor cur = null;
        ContentResolver cr = act.getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED)
            return new ArrayList<>();
        cur = cr.query(uri, EVENT_PROJECTION, filter, null, null);
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
            item.setLevel(cur.getString(4));
            item.setType(cur.getString(5));
            String color = String.format("#%06X", (0xFFFFFF & cur.getInt(6)));
            item.setColor(color);

            if (item.getName().equals(item.getAccount()))
                item.setName("Eventos");

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
        return readCalendar(init, end, resolver, -1);
    }

    public static List<Task> readCalendar(Calendar init, Calendar end, ContentResolver resolver, long _id) {
        Uri.Builder eventsUriBuilder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        init.set(Calendar.HOUR_OF_DAY, 0);
        init.set(Calendar.MINUTE, 1);

        String filter = "";
        String[] values;

        if (_id != -1) {
            filter = null;
            values = null;
            eventsUriBuilder = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, _id).buildUpon();
        } else {
            long dtStart = init.getTimeInMillis();
            if (end == null) {
                end = init;
                end.set( Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH) );
            }
            long dtEnd = end.getTimeInMillis();
            ContentUris.appendId(eventsUriBuilder, dtStart);
            ContentUris.appendId(eventsUriBuilder, dtEnd);
            filter = "( " + CalendarContract.Instances.BEGIN + " >= ? )";
            values = new String[]{ Long.toString(dtStart) };
        }

        Uri eventsUri = eventsUriBuilder.build();
        Cursor cursor = null;
        String[] fields = new String[] {
                CalendarContract.Events.TITLE, // 0
                CalendarContract.Instances.BEGIN, // 1
                CalendarContract.Events.DISPLAY_COLOR, // 2
                CalendarContract.Instances.END, // 3
                CalendarContract.Events.ALL_DAY, // 4
                CalendarContract.Events.DESCRIPTION, // 5
                "event_id", // 6
                CalendarContract.Events.CALENDAR_ID // 7
        };

        cursor = resolver.query(eventsUri, fields, filter, values, CalendarContract.Instances.BEGIN + " ASC");
        //cursor = CalendarContract.Instances.query(getContentResolver(), fields, dtStart, dtEnd);

        return handleCalendar(cursor);
    }

    public static List<Task> readCalendar(int month, int year, ContentResolver resolver) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return readCalendar(c, null, resolver);
    }

    public static List<Task> handleCalendar (Cursor cur) {
        List<Task> list = new ArrayList<>();
        while (cur.moveToNext()) {
            String color = String.format("#%06X", (0xFFFFFF & cur.getInt(2)));
            Task t = new Task(cur.getString(0), cur.getLong(1), color);
            t.setEnd(cur.getLong(3));
            t.setAllDay(cur.getInt(4));
            t.setDescription(cur.getString(5));
            t.setID(cur.getLong(6));
            t.setCalendarID(cur.getLong(7));
            list.add(t);
        }
        return list;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getId() {
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

        @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(account);
        parcel.writeString(name);
        parcel.writeString(owner);
    }

    public static CalendarProvider find(Activity act, long calendarID) {
        List<CalendarProvider> list = calendars(act, Calendars._ID + " = " + calendarID);
        return list.size() > 0 ? list.get(0) : null;
    }

    public static List<CalendarProvider> myCalendars(Activity act) {
        return calendars(act, Calendars.CALENDAR_ACCESS_LEVEL + " = 700");
    }
}
