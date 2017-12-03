package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import models.Network;
import models.Setting;
import models.Task;
import models.Work;
import network.Input;

/**
 * Created by felipe on 09/10/17.
 */

public class InputSchema {
    public static final String TABLE = "inputs";
    public static final String ID = "_id";
    public static final String DAY = "day";
    public static final String HOUR = "hour";
    public static final String VALUE = "value";
    private static Database db;
    private static SQLiteDatabase conn;

    public InputSchema (Context context) {
        db = Database.getInstance(context);
    }

    public boolean record (Input model) {
        conn = db.getWritableDatabase();
        ContentValues content = getContent(model);
        long res = conn.insert(TABLE, null, content);
        conn.close();
        return res != -1;
    }

    private ContentValues getContent (Input model) {
        ContentValues content = new ContentValues();
        content.put(DAY, model.getDay());
        content.put(HOUR, model.getHour());
        content.put(VALUE, model.getValue());
        return content;
    }

    public static List<Input> find(Context context, String where) {
        Cursor cursor;
        String[] campos =  {
                ID, // 0
                DAY, // 1
                HOUR, // 2
                VALUE, // 3
        };
        db = Database.getInstance(context);
        conn = db.getReadableDatabase();
        cursor = conn.query(TABLE, campos, where, null, null, null, null);
        List<Input> list = new ArrayList<>();
        while (cursor.moveToNext())
            list.add(parse(cursor, context));
        conn.close();
        db.close();
        return list;
    }

    private static Input parse(Cursor cursor, Context context) {
        Input input = new Input(context);
        input.setID(cursor.getLong(0));
        input.setDay(cursor.getInt(1));
        input.setHour(cursor.getInt(2));
        input.setValue(cursor.getInt(3));
        return input;
    }

    public static int delete(Context context, String filter) {
        conn = db.getWritableDatabase();
        int n = conn.delete(TABLE, filter, null);
        conn.close();
        return n;
    }
}
