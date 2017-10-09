package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by felipe on 09/10/17.
 */

public class WorkSchema extends SQLiteOpenHelper {
    public static final String NAME = "database.db";
    public static final String TABLE = "works";
    public static final String ID = "_id";
    public static final String PAYLOAD = "payload";
    public static final String EVENT = "event_id";
    public static final String REFERENCES = "reference_id";
    private static final int VERSAO = 1;

    public WorkSchema(Context context){
        super(context, NAME, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE+" ("
                + ID + " integer primary key autoincrement,"
                + PAYLOAD + " integer,"
                + REFERENCES + " integer NULL,"
                + EVENT+ " integer"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE);
        onCreate(db);
    }
}
