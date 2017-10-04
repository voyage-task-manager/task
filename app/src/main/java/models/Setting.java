package models;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import database.Schema;

/**
 * Created by felipe on 03/10/17.
 */

public class Setting {
    private SQLiteDatabase db;
    private Schema schema;

    private int id;
    private List<Integer> days;
    private String[] launch;
    private String[] dinner;
    private String[] sleep;
    private String[] wake;
    private Context context;

    public Setting () {}
    public Setting (Context context) {
        schema = new Schema(context);
        this.context = context;
    }

    public List<Setting> init () {
        List<Setting> list = this.all();
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
        def.record();
        def.record();

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

    public List<Setting> all () {
        Cursor cursor;
        String[] campos =  {
                Schema.ID, // 0
                Schema.DAYS, // 1
                Schema.WAKE[0], // 2
                Schema.WAKE[1], // 3
                Schema.LAUNCH[0], // 4
                Schema.LAUNCH[1], // 5
                Schema.DINNER[0], // 6
                Schema.DINNER[1], // 7
                Schema.SLEEP[0], // 8
                Schema.SLEEP[1] // 9
        };

        //String where = CriaBanco.ID + "=" + id;
        db = schema.getReadableDatabase();
        cursor = db.query(Schema.TABLE, campos, null, null, null, null, null, null);

        List<Setting> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            list.add(parse(cursor));
        }

        db.close();
        return list;
    }

    public Setting parse (Cursor cursor) {
        Setting setting = new Setting(context);
        List<Integer> l = new ArrayList<>();
        String [] d = cursor.getString(1).split(";");
        for (int i = 0; i < d.length; i++)
            l.add( Integer.parseInt(d[i]) );
        setting.setId(cursor.getInt(0));
        setting.setDays(l);
        setting.setWake(new String[]{ cursor.getString(2), cursor.getString(3) });
        setting.setLaunch(new String[]{ cursor.getString(4), cursor.getString(5) });
        setting.setDinner(new String[]{ cursor.getString(6), cursor.getString(7) });
        setting.setSleep(new String[]{ cursor.getString(8), cursor.getString(9) });
        return setting;
    }

    public boolean record () {
        db = schema.getWritableDatabase();
        ContentValues content = getContent();
        long res = db.insert(Schema.TABLE, null, content);
        return res != -1;
    }

    private ContentValues getContent () {
        String d = days.size() > 0 ? "" + days.get(0) : "";
        for (int i = 1; i < days.size(); i++)
            d += ";" + days.get(i);
        ContentValues content = new ContentValues();
        content.put(Schema.DAYS, d);
        content.put(Schema.LAUNCH[0], launch[0]);
        content.put(Schema.LAUNCH[1], launch[1]);
        content.put(Schema.DINNER[0], dinner[0]);
        content.put(Schema.DINNER[1], dinner[1]);
        content.put(Schema.WAKE[0], wake[0]);
        content.put(Schema.WAKE[1], wake[1]);
        content.put(Schema.SLEEP[0], sleep[0]);
        content.put(Schema.SLEEP[1], sleep[1]);
        return content;
    }

    public void save () {
        db = schema.getWritableDatabase();
        ContentValues content = getContent();
        String where = Schema.ID + "=" + id;
        db.update(Schema.TABLE, content, where, null);
    }
}
