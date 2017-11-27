package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import models.Day;
import models.Setting;
import models.Work;

/**
 * Created by allanromanato on 5/27/15.
 */
public class SettingSchema {
    private Database db;
    private SQLiteDatabase conn;

    public static final String TABLE = "settings";
    public static final String ID = "_id";
    public static final String DAYS = "days";
    public static final String[] LAUNCH = new String[] { "lunch_inner", "lunch_out" };
    public static final String[] WAKE = new String[] { "wake_inner", "wake_out" };
    public static final String[] SLEEP = new String[] { "sleep_inner", "sleep_out" };
    public static final String[] DINNER = new String[]{ "dinner_inner", "dinner_out" };

    public SettingSchema (Context context) {
        db = Database.getInstance(context);
    }

    public static List<Setting> all (Context context) {
        Cursor cursor;
        String[] campos =  {
                ID, // 0
                DAYS, // 1
                WAKE[0], // 2
                WAKE[1], // 3
                LAUNCH[0], // 4
                LAUNCH[1], // 5
                DINNER[0], // 6
                DINNER[1], // 7
                SLEEP[0], // 8
                SLEEP[1] // 9
        };
        Database db = Database.getInstance(context);
        SQLiteDatabase conn = db.getReadableDatabase();
        cursor = conn.query(SettingSchema.TABLE, campos, null, null, null, null, null, null);

        List<Setting> list = new ArrayList<>();
        while (cursor.moveToNext())
            list.add(parse(cursor, context));

        db.close();
        return list;
    }

    public static Setting parse (Cursor cursor, Context context) {
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

    public boolean save (Setting model) {
        return save(model, false);
    }

    public boolean save (Setting model, boolean insert) {
        conn = db.getWritableDatabase();
        ContentValues content = getContent(model);
        if (model.getId() == -1 || insert)
            return conn.insert(TABLE, null, content) != -1;
        String where = SettingSchema.ID + "=" + model.getId();
        return conn.update(TABLE, content, where, null) != -1;
    }

    private ContentValues getContent (Setting model) {
        List<Integer> days = model.getDays();
        String d = days.size() > 0 ? "" + days.get(0) : "";
        for (int i = 1; i < days.size(); i++)
            d += ";" + days.get(i);
        ContentValues content = new ContentValues();
        content.put(SettingSchema.DAYS, d);
        content.put(SettingSchema.LAUNCH[0], model.getLaunch()[0]);
        content.put(SettingSchema.LAUNCH[1], model.getLaunch()[1]);
        content.put(SettingSchema.DINNER[0], model.getDinner()[0]);
        content.put(SettingSchema.DINNER[1], model.getDinner()[1]);
        content.put(SettingSchema.WAKE[0], model.getWake()[0]);
        content.put(SettingSchema.WAKE[1], model.getWake()[1]);
        content.put(SettingSchema.SLEEP[0], model.getSleep()[0]);
        content.put(SettingSchema.SLEEP[1], model.getSleep()[1]);
        return content;
    }
}