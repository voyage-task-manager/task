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
    private Task task;
    private Task reference;
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

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getPayload() {
        return payload;
    }

    public void setPayload(int payload) {
        this.payload = payload;
    }

    public Task getReference() {
        return reference;
    }

    public void setReference(Task reference) {
        this.reference = reference;
    }

    public static Work findByTask(Context context, long id) {
        String where = WorkSchema.EVENT + " = " + id;
        List<Work> arr = WorkSchema.find(context, where);
        if (arr.size() > 0) return arr.get(0);
        return null;
    }
}
