package models;

import android.os.Parcel;
import android.os.Parcelable;

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

    public Task (String title, long date, String color) {
        this.title = title;
        this.date = date;
        this.color = color;
    }

    public Task (String title, long date) {
        this.title = title;
        this.date = date;
        this.color = "#ff0000";
    }

    protected Task(Parcel in) {
        title = in.readString();
        description = in.readString();
        color = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(color);
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
}
