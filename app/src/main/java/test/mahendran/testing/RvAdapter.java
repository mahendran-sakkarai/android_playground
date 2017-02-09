package test.mahendran.testing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/9/2017.
 */
public class RvAdapter extends RecyclerView.Adapter {
    private final List<String> mItems = new ArrayList<>();

    public RvAdapter() {
        for (int i = 0; i < 100; i++)
            mItems.add(String.valueOf(i));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DummyHolder(inflater.inflate(android.R.layout.activity_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((DummyHolder)holder).bindView(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
