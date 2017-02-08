package test.mahendran.testing.views;

import android.content.Context;
import android.util.AttributeSet;

import com.flipkart.android.proteus.view.ProteusView;
import com.flipkart.android.proteus.view.manager.ProteusViewManager;

/**
 * Created by user on 2/8/2017.
 */

public class ProteusRadioButton extends android.widget.RadioButton implements ProteusView {
    private ProteusViewManager mViewManager;

    public ProteusRadioButton(Context context) {
        super(context);
    }

    public ProteusRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProteusRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
