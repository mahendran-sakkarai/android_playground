package test.mahendran.testing.views;

import android.content.Context;
import android.util.AttributeSet;

import com.flipkart.android.proteus.view.ProteusView;
import com.flipkart.android.proteus.view.manager.ProteusViewManager;

/**
 * Created by user on 2/8/2017.
 */

public class ProteusRadioGroup extends android.widget.RadioGroup implements ProteusView {
    private ProteusViewManager mViewManager;

    public ProteusRadioGroup(Context context) {
        super(context);
    }

    public ProteusRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public ProteusViewManager getViewManager() {
        return this.mViewManager;
    }

    @Override
    public void setViewManager(ProteusViewManager proteusViewManager) {
        this.mViewManager = proteusViewManager;
    }
}
