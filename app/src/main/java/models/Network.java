package models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import database.NetworkSchema;

/**
 * Created by zerodois on 29/11/17.
 */

public class Network {
    private long _id;
    private int layer, column, row;
    private double value;
    private String dimension;

    private NetworkSchema schema;
    private Context context;

    public Network (Context context) {
        schema = new NetworkSchema(context);
        this.context = context;
    }

    public void setID (long _id) {
        this._id = _id;
    }
    public void setLayer (int layer) {
        this.layer = layer;
    }
    public void setColumn (int column) {
        this.column = column;
    }
    public void setRow (int row) {
        this.row = row;
    }
    public void setValue (double value) {
        this.value = value;
    }
    public boolean save () {
        return schema.record(this);
    }
    public static void save (Context context, double[][][] weights) {
        new NetworkSchema(context).record(weights);
    }

    public int getLayer () {
        return layer;
    }
    public int getColumn () {
        return column;
    }
    public int getRow () {
        return row;
    }
    public double getValue () {
        return value;
    }
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
    public String getDimension() {
        return dimension;
    }
}
