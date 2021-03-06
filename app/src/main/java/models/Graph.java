package models;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import network.Input;
import zerodois.neuralnetwork.NeuralNetwork;

/**
 * Created by felipe on 29/09/17.
 */

public class Graph {

    private List<Day> list;
    Setting setting;

    public Graph (Calendar init, Calendar end, Setting setting, Activity activity) {
        this.setting = setting;
        List<Task> l = CalendarProvider.readCalendar(init, end, activity.getContentResolver());
        list = Day.create(init, end, l, true, setting);
        Collections.sort(list, new Comparator<Day>() {
            @Override
            public int compare(Day day, Day t1) {
                return day.getNumber() < t1.getNumber() ? -1  : (day.getNumber() > t1.getNumber() ? +1 : 0);
            }
        });
        for (Iterator<Day> it = list.iterator(); it.hasNext();)
            if (it.next().getFree() <= 0) it.remove();
    }

    public List<Task> organize (Task t, int estimate, NeuralNetwork network) {
        int total = estimate;
        int lim = -1;
        int today = -1;
        Calendar instance = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(t.getDate());
        List<Task> org = new ArrayList<>();
        List<Integer> days = setting.getDays();

        for (int i = 0; i < list.size() && total > 0; i++) {
            lim = -1;
            today = -1;
            Day item = list.get(i);
            Calendar itemCalendar = item.getCalendar(3);
            int day = itemCalendar.get(Calendar.DAY_OF_WEEK);
            if (!days.contains(itemCalendar.get(Calendar.DAY_OF_WEEK)%7)) continue;
            if (Day.compare(item.getCalendar(), calendar))
                lim = calendar.get(Calendar.HOUR_OF_DAY);
            if (Day.compare(item.getCalendar(), instance))
                today = instance.get(Calendar.HOUR_OF_DAY) + 1;
            List<int[]> pairs = item.getFreeVector();
            for (int v[] : pairs) {
                if (lim > 0 && v[0] > lim) break;
                if (today > 0 && v[1] < today) continue;
                int init = -1;
                for (int hour = v[0]; hour <= v[1] && total > 0; hour++) {
                    if (today > 0 && hour < today) continue;
                    if (lim > 0 && hour > lim) break;
                    Log.d("INFERNO", day + " >> " + hour + " :: " + network.predict(new Input(day, hour, -1).toArray())[0]);
                    if ((init > 0 && (int) hour == v[1]) || network.predict(new Input(day, hour, -1).toArray())[0] <= 0.8 || hour == lim) {
                        analyze(init, org, (int) hour, t, item);
                        init = -1;
                        continue;
                    }
                    if (hour == v[1]) continue;
                    total--;
                    if (total == 0) analyze(init < 0 ? (int) hour : init, org, hour+1, t, item);
                    if (init < 0) init = hour;
                }
                if (total <= 0) break;
            }
        }
        if (total > 0) org.clear();
        return org;
    }

    private void analyze(int init, List<Task> org, int hour, Task t, Day item) {
        if (init < 0) return;
        Task task = new Task(t.getTitle(), item.getCalendar(init).getTimeInMillis(), item.getCalendar(hour).getTimeInMillis());
        task.setDescription("Atividade programada para planejamento e execução da tarefa \"" + task.getTitle() + "\"\nUse esse tempo com sabedoria :)");
        task.setCalendarID(t.getCalendarID());
        org.add(task);
    }
}
