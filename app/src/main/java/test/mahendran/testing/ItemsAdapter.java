package test.mahendran.testing;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Nandakumar on 12/26/2016.
 */
public class ItemsAdapter extends RecyclerView.Adapter {
    private ArrayList<String> mItems = new ArrayList<>();

    public ArrayList<String> getItems() {
        return mItems;
    }

    public void updateItems(ArrayList<String> listItems) {
        mItems.addAll(listItems);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ItemsHolder(inflater.inflate(R.layout.test_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemsHolder) holder).bindData(getItems().get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setLoading() {
        mItems.add("Loading");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 1000);
    }

    public void removeLoading() {
        if (mItems.size() > 0)
            mItems.remove(mItems.size() - 1);
    }

    public void setThatsAll() {
        mItems.add("Thats All");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 1000);
    }
}