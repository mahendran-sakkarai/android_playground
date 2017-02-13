package test.mahendran.testing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by user on 2/13/2017.
 */

public class LyricsStore {
    private static LyricsStore sInstance = null;
    private MusicDB mMusicDatabase = null;

    private LyricsStore(final Context context) {
        mMusicDatabase = new MusicDB(context);
    }

    public static final synchronized LyricsStore getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new LyricsStore(context.getApplicationContext());
        }
        return sInstance;
    }

    int result = 0;

    public boolean AddUpdateLyrics(long SongID, String SongLyrics) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        database.beginTransaction();
        Cursor c = getData(database, SongID);
        if (c == null) {
            database.endTransaction();
            return false;
        }

        if (c.getCount() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbColumns.MusicDb.ID, SongID);
            contentValues.put(DbColumns.MusicDb.Lyrics, SongLyrics);
            result = database.update(DbColumns.MusicDb.NAME, contentValues, DbColumns.MusicDb.ID + "=?", new String[]{Long.toString(SongID)});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DbColumns.MusicDb.ID, SongID);
            contentValues.put(DbColumns.MusicDb.Lyrics, SongLyrics);
            result = (int) database.insert(DbColumns.MusicDb.NAME, null, contentValues);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        return true;
    }

    public String getLyrics(long songid) {
        String lyrics = "";
        Cursor res = null;
        try {
            SQLiteDatabase db = mMusicDatabase.getReadableDatabase();

            res=  db.query(DbColumns.MusicDb.NAME,
                    new String[]{DbColumns.MusicDb.ID,DbColumns.MusicDb.Lyrics}, DbColumns.MusicDb.ID + "=?", new String[]{String.valueOf(songid)}, null, null, null);
            if (res == null)
                return "";
            if (res.getCount() > 0) {
                // This is the place you made the mistake. you need to iterate and then try to get the value.
                while (res.moveToNext()) {
                    lyrics = res.getString(res.getColumnIndex(DbColumns.MusicDb.Lyrics));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lyrics;
    }

    public Cursor getData(SQLiteDatabase database, long id) {
        Cursor res = null;
        String error = "";
        try {
            res = database.query(DbColumns.MusicDb.NAME, new String[]{DbColumns.MusicDb.ID, DbColumns.MusicDb.Lyrics}, DbColumns.MusicDb.ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        } catch (Exception ex) {
            error = ex.getMessage();
        }

        return res;
    }
}
