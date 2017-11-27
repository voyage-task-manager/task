package models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import database.SettingSchema;

/**
 * Created by felipe on 03/10/17.
 */

public class Setting {
    private SettingSchema schema;

    private int id = -1;
    private List<Integer> days;
    private String[] launch;
    private String[] dinner;
    private String[] sleep;
    private String[] wake;
    private Context context;

    public Setting (Context context) {
        schema = new SettingSchema(context);
        this.context = context;
    }

    public static List<Setting> init (Context context) {
        SettingSchema schema = new SettingSchema(context);
        List<Setting> list = schema.all(context);
        if (list.size() > 0)
            return list;

        Setting def = new Setting(context);// Settings default
        List<Integer> d = new ArrayList<>();
        for (int i = 2; i<=6; i++) d.add(i);
        def.setDays(d);
        def.setWake(new String[] {"07:00", "08:00"});
        def.setLaunch(new String[] {"12:00", "13:00"});
        def.setDinner(new String[] {"19:00", "20:00"});
        def.setSleep(new String[] {"23:00", "23:59"});
        def.save(true);
        def.save(true);

        def.setId(1);
        list.add(def);
        def.setId(2);
        list.add(def);
        return list;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }

    public String[] getLaunch() {
        return launch;
    }

    public void setLaunch(String[] lunch) {
        this.launch = lunch;
    }

    public String[] getDinner() {
        return dinner;
    }

    public void setDinner(String[] dinner) {
        this.dinner = dinner;
    }

    public String[] getSleep() {
        return sleep;
    }

    public void setSleep(String[] sleep) {
        this.sleep = sleep;
    }

    public String[] getWake() {
        return wake;
    }

    public void setWake(String[] wake) {
        this.wake = wake;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean save (boolean insert) {
        return schema.save(this, insert);
    }
    public boolean save () {
        return schema.save(this);
    }
}
