package test.mahendran.testing;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by user on 2/7/2017.
 */

public class DummyHolder extends RecyclerView.ViewHolder {
    private final TextView tv1;

    public DummyHolder(View itemView) {
        super(itemView);
        tv1 = (TextView) itemView.findViewById(android.R.id.text1);
    }

    public void bindView(Car car) {
        tv1.setText(car.getId() + " .. " + car.getName());
    }
}
