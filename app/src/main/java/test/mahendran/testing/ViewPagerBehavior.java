package test.mahendran.testing;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by "Mahendran Sakkarai" on 4/16/2017.
 */

public class ViewPagerBehavior extends CoordinatorLayout.Behavior<ViewPager> {
    public ViewPagerBehavior(){

    }

    public ViewPagerBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, ViewPager child, MotionEvent ev) {
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean blocksInteractionBelow(CoordinatorLayout parent, ViewPager child) {
        return true;
    }

    @Override
    public float getScrimOpacity(CoordinatorLayout parent, ViewPager child) {
        return 0.5f;
    }
}
