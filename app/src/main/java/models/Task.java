package models;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Date;
import java.util.Locale;

/**
 * Created by felipe on 01/09/17.
 */

public class Task implements Parcelable {
    private String title;
    private long date;
    private long end;
    private String description;
    private String color;
    private boolean allDay;
    private long _id;
    private long calendarId;

    public Task (String title, long start, long end, String color) {
        this.title = title;
        this.date = start;
        this.end = end;
        this.color = color;
        this.description = "";
    }

    public Task (String title, long start, long end) {
        this(title, start, end, "#ff0000");
    }

    public Task (String title, long date, String color) {
        this(title, date, 0, color);
    }

    public Task (String title, long date) {
        this(title, date, 0, "#ff0000");
    }

    protected Task(Parcel in) {
        title = in.readString();
        date = in.readLong();
        end = in.readLong();
        description = in.readString();
        color = in.readString();
        allDay = in.readByte() != 0;
        _id = in.readLong();
        calendarId = in.readLong();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(date);
        dest.writeLong(end);
        dest.writeString(description);
        dest.writeString(color);
        dest.writeByte((byte) (allDay ? 1 : 0));
        dest.writeLong(_id);
        dest.writeLong(calendarId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String toString () {
        return title;
    }

    public String getTitle () {
        return title;
    }

    public long getDate() { return date; }

    public void setDate(long date) { this.date = date; }

    public String getColor() {
        return color;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getEnd() {
        return end;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(int allDay) {
        this.allDay = allDay == 1;
    }

    public long record (Activity activity) {
        ContentResolver cr = activity.getContentResolver();
        ContentValues values = new ContentValues();

        values.put(Events.DTSTART, date);
        values.put(Events.DTEND, end);
        values.put(Events.TITLE, title);
        values.put(Events.DESCRIPTION, description);
        values.put(Events.CALENDAR_ID, calendarId);
        values.put(Events.EVENT_TIMEZONE, "UTC");

        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return -1;
        }

        Uri uri = cr.insert(Events.CONTENT_URI, values);
        _id = ContentUris.parseId(uri);
        Log.d("INFO::", "INSERTED " + date + " ID " + calendarId );

        return _id;
    }

    public int drop (Context context) {
        Uri deleteUri = null;
        deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, _id);
        int rows = context.getContentResolver().delete(deleteUri, null, null);
        return rows;
    }

    public long getCalendarID() {
        return calendarId;
    }

    public long getID () {
        return _id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setID(long _id) {
        this._id = _id;
    }

    public static int delete(Context context, long _id) {
        Uri deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, _id);
        return context.getContentResolver().delete(deleteUri, null, null);
    }

    public void setCalendarID(long calendarId) {
        this.calendarId = calendarId;
    }
}
