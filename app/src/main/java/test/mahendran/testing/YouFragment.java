package test.mahendran.testing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by "Mahendran Sakkarai" on 4/16/2017.
 */

public class YouFragment extends Fragment {
    public YouFragment() {

    }

    public static YouFragment newInstance() {
        return new YouFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.you, container, false);

        return v;
    }
}
