package test.mahendran.testing;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 2/9/2017.
 */
public class DummyHolder extends RecyclerView.ViewHolder {
    private final TextView tv1;

    public DummyHolder(View view) {
        super(view);
        tv1 = (TextView) view.findViewById(android.R.id.text1);
    }

    public void bindView(String msg) {
        tv1.setText(msg);
    }
}
