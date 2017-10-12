package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import models.Setting;
import models.Work;

/**
 * Created by felipe on 09/10/17.
 */

public class WorkSchema {
    public static final String TABLE = "works";
    public static final String ID = "_id";
    public static final String PAYLOAD = "payload";
    public static final String EVENT = "event_id";
    public static final String REFERENCES = "reference_id";
    private Database db;
    private SQLiteDatabase conn;

    public WorkSchema (Context context) {
        db = Database.getInstance(context);
    }

    public boolean record (Work model) {
        conn = db.getWritableDatabase();
        ContentValues content = getContent(model);
        long res = conn.insert(TABLE, null, content);
        return res != -1;
    }

    private ContentValues getContent (Work model) {
        ContentValues content = new ContentValues();
        content.put(WorkSchema.EVENT, model.getTask().getID());
        content.put(WorkSchema.PAYLOAD, model.getPayload());
        content.put(WorkSchema.REFERENCES, model.getReference() == null ? null : model.getReference().getID());
        return content;
    }

    public static List<Work> find(Context context, String where) {
        Cursor cursor;
        String[] campos =  {
                ID, // 0
                PAYLOAD, // 1
                EVENT, // 2
                REFERENCES // 3
        };

        Database db = Database.getInstance(context);
        SQLiteDatabase conn = db.getReadableDatabase();
        cursor = conn.query(TABLE, campos, where, null, null, null, null, null);

        List<Work> list = new ArrayList<>();
        while (cursor.moveToNext())
            list.add(parse(cursor, context));

        db.close();
        return list;
    }

    private static Work parse(Cursor cursor, Context context) {
        Work work = new Work(context);
        work.setID(cursor.getLong(0));
        work.setPayload(cursor.getInt(1));
        //work.setTask();
        //work.setReference();
        return work;
    }
}
