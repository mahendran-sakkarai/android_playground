package test.mahendran.testing;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Nandakumar on 12/26/2016.
 */
public class ItemHolder extends RecyclerView.ViewHolder {
    private final TextView mTextView;

    public ItemHolder(View view) {
        super(view);
        mTextView = (TextView) view.findViewById(android.R.id.text1);
    }

    public void bindData(String s) {
        mTextView.setText(s);
    }
}
