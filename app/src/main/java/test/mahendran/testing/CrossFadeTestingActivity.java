package test.mahendran.testing;

import android.animation.Animator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by user on 2/13/2017.
 */

public class CrossFadeTestingActivity extends Activity {
    private FrameLayout mFrame1;
    private FrameLayout mFrame2;
    private int mShortAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cross_fade_lt);

        mShortAnimation = getResources().getInteger(
                android.R.integer.config_shortAnimTime
        );
        mFrame1 = (FrameLayout) findViewById(R.id.frame1);
        mFrame2 = (FrameLayout) findViewById(R.id.frame2);

        mFrame1.setVisibility(View.GONE);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
                        tryCrossFading();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR1)
    private void tryCrossFading() {
        mFrame1.setAlpha(0f);
        mFrame1.setVisibility(View.VISIBLE);

        mFrame1.animate()
                .alpha(1f)
                .setDuration(mShortAnimation)
                .setListener(null);

        mFrame2.animate()
                .alpha(0f)
                .setDuration(mShortAnimation)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mFrame2.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }
}
