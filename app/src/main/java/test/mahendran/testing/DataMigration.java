package test.mahendran.testing;

import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by user on 2/7/2017.
 */
public class DataMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 1) {
            RealmObjectSchema carSchema = schema.get("Car");

            carSchema.addField("id_tmp", int.class);
            carSchema.transform(new RealmObjectSchema.Function() {
                @Override
                public void apply(DynamicRealmObject obj) {
                    // Functionality to change the type
                    String id = obj.getString("id");

                    obj.setInt("id_tmp", Integer.valueOf(id));
                }
            });

            carSchema.removeField("id");
            carSchema.renameField("id_tmp", "id");

            oldVersion++;
        }
    }
}
