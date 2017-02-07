package test.mahendran.testing;

import io.realm.RealmObject;

/**
 * Created by user on 2/7/2017.
 */

public class Car extends RealmObject{
    int id;

    String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
