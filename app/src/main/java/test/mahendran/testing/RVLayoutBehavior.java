package test.mahendran.testing;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by "Mahendran Sakkarai" on 4/16/2017.
 */

public class RVLayoutBehavior extends AppBarLayout.ScrollingViewBehavior {
    public RVLayoutBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency) || dependency instanceof ViewPager;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if (dyConsumed > 0) {
            List<View> dependencies = coordinatorLayout.getDependencies(child);
            for (View view : dependencies) {
                if (view instanceof ViewPager) {
                    ((ViewPager) view).setVisibility(View.GONE);
                }
            }
        } else if (dyConsumed < 0) {
            List<View> dependencies = coordinatorLayout.getDependencies(child);
            for (View view : dependencies) {
                if (view instanceof ViewPager) {
                    ((ViewPager) view).setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
