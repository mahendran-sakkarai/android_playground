package test.mahendran.testing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by "Mahendran Sakkarai" on 4/16/2017.
 */

public class CategoriesContentFragment extends Fragment {
    private int itemCount;

    public CategoriesContentFragment() {

    }

    public static CategoriesContentFragment newInstance() {
        return new CategoriesContentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.categories_content, container, false);

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new CategoriesContentAdapter());

        return v;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
