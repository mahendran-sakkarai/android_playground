package test.mahendran.testing.views;

import android.content.Context;

import com.flipkart.android.proteus.view.ProteusView;
import com.flipkart.android.proteus.view.manager.ProteusViewManager;

/**
 * Created by user on 2/8/2017.
 */

public class ProteusSpinner extends android.widget.Spinner implements ProteusView {
    public ProteusSpinner(Context context) {
        super(context);
    }

    ProteusViewManager viewManager;

    @Override
    public ProteusViewManager getViewManager() {
        return viewManager;
    }

    @Override
    public void setViewManager(ProteusViewManager proteusViewManager) {
        this.viewManager = proteusViewManager;
    }
}
