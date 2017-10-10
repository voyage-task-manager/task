package models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import database.WorkSchema;

/**
 * Created by felipe on 09/10/17.
 */

public class Work {
    private WorkSchema schema;
    private Context context;
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
}
