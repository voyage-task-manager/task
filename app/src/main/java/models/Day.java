package models;

import com.example.felipe.app.CalendarAdapter;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by felipe on 11/09/17.
 */

public class Day {
    private int number;
    private String week;
    private int month;
    private List<Task> events;

    public Day(Calendar calendar) {
        this.number = calendar.get(Calendar.DAY_OF_MONTH);
        this.week = getWeek(calendar);
        this.month = calendar.get(Calendar.MONTH);
        this.events = new ArrayList<>();
    }
    public Day(Calendar calendar, List<Task> events) {
        this.number = calendar.get(Calendar.DAY_OF_MONTH);
        this.week = getWeek(calendar);
        this.month = calendar.get(Calendar.MONTH);
        this.events = events;
    }

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
        return getWeek().toUpperCase().substring(0, 3) + ", " + number + " de " + getMonth(true).toUpperCase().substring(0, 3);
    }
}
