package test.mahendran.testing;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by "Mahendran Sakkarai" on 4/16/2017.
 */

public class CategoriesContentAdapter extends RecyclerView.Adapter<CategoriesContentAdapter.CategoriesContentHolder> {
    @Override
    public CategoriesContentAdapter.CategoriesContentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new CategoriesContentHolder(inflater.inflate(R.layout.categories_content_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(CategoriesContentAdapter.CategoriesContentHolder holder, int position) {
        holder.mTv.setText("Category " + position);
    }

    @Override
    public int getItemCount() {
        return 150;
    }

    public class CategoriesContentHolder extends RecyclerView.ViewHolder {
        public final TextView mTv;

        public CategoriesContentHolder(View view) {
            super(view);

            mTv = (TextView) view.findViewById(R.id.tv);
        }
    }
}
