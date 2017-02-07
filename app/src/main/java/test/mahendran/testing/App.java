package test.mahendran.testing;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by user on 2/7/2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration mConfig = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .migration(new DataMigration())
                .build();
        Realm.setDefaultConfiguration(mConfig);
    }
}
