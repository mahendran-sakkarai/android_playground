package test.mahendran.testing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by user on 2/7/2017.
 */
public class RvAdapter extends RecyclerView.Adapter {
    private RealmResults<Car> mCars;
    Realm realm = Realm.getDefaultInstance();

    public RvAdapter() {
        if (realm != null)
            mCars = realm.where(Car.class).findAll();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DummyHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DummyHolder) holder).bindView(mCars.get(position));
    }

    @Override
    public int getItemCount() {
        return mCars != null ? mCars.size() : 0;
    }
}
