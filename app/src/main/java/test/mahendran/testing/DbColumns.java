package test.mahendran.testing;

import android.provider.BaseColumns;

/**
 * Created by user on 2/13/2017.
 */

public class DbColumns {
    public abstract class MusicDb implements BaseColumns {
        public static final String NAME = "lyricsstore";
        public static final String ID = "songid";
        public static final String Lyrics = "lyrics";
    }
}
