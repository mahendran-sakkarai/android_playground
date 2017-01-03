package test.mahendran.testing;

/**
 * Created by Udhaya Kumar on 03-01-2017.
 */

public class WeakReference<T> {
    private final T mObject;

    public WeakReference(T object){
        this.mObject = object;
    }

    public T get() {
        return mObject;
    }
}
