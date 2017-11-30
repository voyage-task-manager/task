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

/**
 * Created by felipe on 09/10/17.
 */

public class NetworkSchema {
    public static final String TABLE = "networks";
    public static final String ID = "_id";
    public static final String VALUE = "value";
    public static final String LAYER = "layer";
    public static final String COLUMN = "column";
    public static final String ROW = "row";
    public static final String DIMENSION = "dimension";
    private static Database db;
    private static SQLiteDatabase conn;

    public NetworkSchema (Context context) {
        db = Database.getInstance(context);
    }

    public boolean record (Network model) {
        conn = db.getWritableDatabase();
        ContentValues content = getContent(model);
        long res = conn.insert(TABLE, null, content);
        conn.close();
        return res != -1;
    }

    public boolean record (double[][][] models) {
        conn = db.getWritableDatabase();
        conn.beginTransaction();
        for (int i=0; i<models.length; i++) {
            for (int j=0; j<models[i].length; j++) {
                for (int k=0; k<models[i][j].length; k++) {
                    ContentValues content = getContent(i, j, k, models[i][j][k], models[i].length + "x" + models[i][j].length);
                    long res = conn.insert(TABLE, null, content);
                }
            }
        }
        conn.setTransactionSuccessful();
        conn.endTransaction();
        conn.close();
        return true;
    }

    private ContentValues getContent (int layer, int row, int column, double val, String dimension) {
        ContentValues content = new ContentValues();
        content.put(VALUE, val);
        content.put(LAYER, layer);
        content.put(COLUMN, column);
        content.put(ROW, row);
        content.put(DIMENSION, dimension);
        return content;
    }

    private ContentValues getContent (Network model) {
        return getContent(model.getLayer(), model.getRow(), model.getColumn(), model.getValue(), model.getDimension());
    }

    public static List<Network> find(Context context, String where) {
        Cursor cursor;
        String[] campos =  {
                ID, // 0
                LAYER, // 1
                ROW, // 2
                COLUMN, // 3
                VALUE, // 4
                DIMENSION // 5
        };
        db = Database.getInstance(context);
        conn = db.getReadableDatabase();
        cursor = conn.query(TABLE, campos, where, null, null, null, LAYER);
        List<Network> list = new ArrayList<>();
        while (cursor.moveToNext())
            list.add(parse(cursor, context));
        conn.close();
        db.close();
        return list;
    }

    private static Network parse(Cursor cursor, Context context) {
        Network network = new Network(context);
        network.setID(cursor.getLong(0));
        network.setLayer(cursor.getInt(1));
        network.setRow(cursor.getInt(2));
        network.setColumn(cursor.getInt(3));
        network.setValue(cursor.getDouble(4));
        network.setDimension(cursor.getString(5));
        return network;
    }

    public static int delete(Context context, String filter) {
        conn = db.getWritableDatabase();
        int n = conn.delete(TABLE, filter, null);
        conn.close();
        return n;
    }
}
