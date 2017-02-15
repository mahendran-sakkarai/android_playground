package test.mahendran.testing;

import android.animation.Animator;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

/**
 * Created by user on 2/13/2017.
 */

public class CardFlipActivity extends Activity {
    private FrameLayout mFrame1;
    private FrameLayout mFrame2;
    private int mShortAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_flip_anim_lt);

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
                        tryCardFlip();
                    }
                }
            }
        });
    }

    private void tryCardFlip() {

    }
}
