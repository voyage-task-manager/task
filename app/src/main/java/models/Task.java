package models;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.CalendarContract.Events;
import android.support.v4.app.ActivityCompat;

import java.util.Date;

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

    public Task (String title, long start, long end, String color) {
        this.title = title;
        this.date = start;
        this.end = end;
        this.color = color;
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
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(date);
        dest.writeLong(end);
        dest.writeString(description);
        dest.writeString(color);
        dest.writeByte((byte) (allDay ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Uri record (Activity activity, int calendarID) {
        ContentResolver cr = activity.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, date);
        values.put(Events.DTEND, end);
        values.put(Events.TITLE, title);
        values.put(Events.DESCRIPTION, "Group workout");
        values.put(Events.CALENDAR_ID, calendarID);
        values.put(Events.EVENT_TIMEZONE, "UTC");

        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Uri uri = cr.insert(Events.CONTENT_URI, values);
        return uri;
    }
}
