package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by allanromanato on 5/27/15.
 */
public class Schema extends SQLiteOpenHelper {
    public static final String NAME = "database.db";
    public static final String TABLE = "settings";
    public static final String ID = "_id";
    public static final String DAYS = "days";
    public static final String[] LAUNCH = new String[] { "lunch_inner", "lunch_out" };
    public static final String[] WAKE = new String[] { "wake_inner", "wake_out" };
    public static final String[] SLEEP = new String[] { "sleep_inner", "sleep_out" };
    public static final String[] DINNER = new String[]{ "dinner_inner", "dinner_out" };
    private static final int VERSAO = 1;

    public Schema(Context context){
        super(context, NAME, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE+" ("
                + ID + " integer primary key autoincrement,"
                + DAYS + " text,"
                + LAUNCH[0] + " text,"
                + LAUNCH[1] + " text,"
                + DINNER[0] + " text,"
                + DINNER[1] + " text,"
                + WAKE[0] + " text,"
                + WAKE[1] + " text,"
                + SLEEP[0] + " text,"
                + SLEEP[1] + " text"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE);
        onCreate(db);
    }
}