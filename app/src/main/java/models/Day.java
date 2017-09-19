package models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by felipe on 11/09/17.
 */

public class Day implements Parcelable {
    private int number;
    private String week;
    private int month;
    private List<Task> events;
    private int year;

    public Day(Calendar calendar) {
        this.number = calendar.get(Calendar.DAY_OF_MONTH);
        this.week = getWeek(calendar);
        this.month = calendar.get(Calendar.MONTH);
        this.events = new ArrayList<>();
        this.year = calendar.get(Calendar.YEAR);
    }

    public Day(Calendar calendar, List<Task> events) {
        this.number = calendar.get(Calendar.DAY_OF_MONTH);
        this.week = getWeek(calendar);
        this.month = calendar.get(Calendar.MONTH);
        this.year = calendar.get(Calendar.YEAR);
        this.events = events;
    }

    protected Day(Parcel in) {
        number = in.readInt();
        week = in.readString();
        month = in.readInt();
        events = in.createTypedArrayList(Task.CREATOR);
        year = in.readInt();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getWeek() { return week; }
    public void setWeek(String week) { this.week = week; }
    public int getMonth() { return month; }
    public void setMonth(int mounth) { this.month = mounth; }
    public List<Task> getEvents() { return events; }
    public void setEvents(List<Task> events) { this.events = events; }

    public String getMonth(boolean name) {
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        return months[ month ];
    }

    public String getWeek(Calendar calendar) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dayFormat.format(calendar.getTime());
    }

    public String toString () {
        return getWeek().toUpperCase() + ", " + number + " de " + getMonth(true).toUpperCase().substring(0, 3);
    }

    public static List<Task> tasksByDay (List<Task> tasks, int day) {
        ArrayList<Task> arr = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        for(Task t : tasks) {
            calendar.setTimeInMillis(t.getDate());
            if (calendar.get(Calendar.DAY_OF_MONTH) == day)
                arr.add(t);
        }
        return arr;
    }

    public static List<Day> create(Calendar init, int end, List<Task> tasks) {
        List<Day> arr = new ArrayList<>();
        for(int i = init.get(Calendar.DAY_OF_MONTH); i <= end; i++) {
            init.set(Calendar.DAY_OF_MONTH, i);
            ArrayList<Task> events = new ArrayList<>(tasksByDay(tasks, i));
            if (events.size() == 0) continue;
            Day d = new Day(init, events);
            arr.add(d);
        }
        return arr;
    }

    public String getYear() {
        return "" + year;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(number);
        parcel.writeString(week);
        parcel.writeInt(month);
        parcel.writeTypedList(events);
        parcel.writeInt(year);
    }
}
