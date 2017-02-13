package test.mahendran.testing;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by user on 2/13/2017.
 */

public class MusicDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "MusicDb";
    private static int mDbVersion = 1;

    public MusicDB(Context context) {
        super(context, DB_NAME, null, mDbVersion);
        Log.v(MusicDB.class.getSimpleName(), "Called music db constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DbColumns.MusicDb.NAME + " ("
                + DbColumns.MusicDb.ID + " LONG NOT NULL,"
                + DbColumns.MusicDb.Lyrics + " STRING NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbColumns.MusicDb.NAME);
        onCreate(db);
    }
}
