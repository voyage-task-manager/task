package models;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by felipe on 29/09/17.
 */

public class Graph {

    private Calendar init, end;
    private List<Day> list;
    Setting setting;

    public Graph (Calendar init, Calendar end, Setting setting, Activity activity) {
        this.setting = setting;
        this.init = init;
        this.end = end;
        List<Task> l = CalendarProvider.readCalendar(init, end, activity.getContentResolver());
        list = Day.create(init, end, l, true, setting);

        Collections.sort(list, new Comparator<Day>() {
            @Override
            public int compare(Day day, Day t1) {
            return day.getFree() < t1.getFree() ? -1 : (day.getFree() > t1.getFree() ? +1 : 0);
            }
        });

        for (Iterator<Day> it = list.iterator(); it.hasNext();)
            if (it.next().getFree() <= 0) it.remove();
    }

    public List<Task> organize (Task t, int estimate) {

        int total = estimate;
        int cont = 0;
        List<Task> org = new ArrayList<>();
        for (int i = 0; i < list.size() && total > 0; i++) {
            Day item = list.get(i);
            double diff = Math.ceil( (double) estimate /  (double) (list.size() - cont) );
            if (item.getFree() < diff ) {
                cont++;
                continue;
            }
            List<int[]> pairs = item.getFreeVector();
            for (int x = 0; x < pairs.size() && diff > 0; x++) {
                int[] v = pairs.get(x);
                int disp = (int) Math.ceil( Math.min(v[1] - v[0], diff) );
                diff -= disp;
                total -= disp;
                Task task = new Task(t.getTitle() + " - Fragmento " + (char)(i + 'a'), item.getCalendar(v[0]).getTimeInMillis(), item.getCalendar(v[0] + disp).getTimeInMillis());
                org.add(task);
            }
        }

        return org;
    }
}
