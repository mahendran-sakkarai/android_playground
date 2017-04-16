package test.mahendran.testing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by "Mahendran Sakkarai" on 4/16/2017.
 */

public class ScreenSlideFragment extends Fragment {
    private int count;

    public static ScreenSlideFragment newInstance() {
        return new ScreenSlideFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dummy_content, container, false);

        ((TextView)v.findViewById(R.id.tv)).setText(""+count);

        return v;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
