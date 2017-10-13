package models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import database.WorkSchema;

/**
 * Created by felipe on 09/10/17.
 */

public class Work {
    private WorkSchema schema;
    private Context context;
    private long _id;
    private long task;
    private long reference;
    private int payload;
    private SQLiteDatabase db;


    public Work (Context context) {
        schema = new WorkSchema(context);
        this.context = context;
    }

    public boolean save () {
        return schema.record(this);
    }

    public long getID () { return _id; }

    public void setID (long _id) { this._id = _id; }

    public long getTask() {
        return task;
    }

    public void setTask(long task) {
        this.task = task;
    }

    public int getPayload() {
        return payload;
    }

    public void setPayload(int payload) {
        this.payload = payload;
    }

    public long getReference() {
        return reference;
    }

    public void setReference(long reference) {
        this.reference = reference;
    }

    public static Work findByTask(Context context, long id) {
        String where = WorkSchema.EVENT + " = " + id;
        List<Work> arr = WorkSchema.find(context, where);
        if (arr.size() > 0) return arr.get(0);
        return null;
    }

    public void delete() {
        String filter = WorkSchema.ID + " = " + this.getID();
        WorkSchema.delete(context, filter);
    }

    public static void deleteByEvent(Context context, long task) {
        String filter = WorkSchema.REFERENCES + " = " + task;
        List<Work> list = WorkSchema.find(context, filter);
        for (Work w : list)
            Task.delete(context, w.getTask());
        WorkSchema.delete(context, filter);
    }
}
