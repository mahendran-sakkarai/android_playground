package test.mahendran.testing;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private float number = 1;
    private boolean isEnalbed = true;
    private RelativeLayout mContainer;
    private Handler mHandler;
    private Runnable mChangeBackgroundRunnable;
    private ValueAnimator colorAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContainer = (RelativeLayout) findViewById(R.id.container);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEnalbed) {
                    startBackgroundChangeAnimation();
                } else {
                    stopBackgroundChangeAnimation();
                }
                isEnalbed = !isEnalbed;
            }
        });
    }

    private void startBackgroundChangeAnimation() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            executeBackgroundChangeUsingValueAnimator();
        } else {
            executeBackgroundChange();
        }
    }

    private void stopBackgroundChangeAnimation() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            if (colorAnimation != null && colorAnimation.isRunning())
                colorAnimation.end();
        } else {
            if (mHandler != null && mChangeBackgroundRunnable != null)
                mHandler.removeCallbacks(mChangeBackgroundRunnable);
        }
    }

    private void executeBackgroundChange() {
        // Handler and runnable to run the animation in devices sdk below honeycomb.
        mHandler = new Handler();
        mChangeBackgroundRunnable = new Runnable() {
            @Override
            public void run() {
                number++;

                float[] hsvColor = {0, 1, 1};
                hsvColor[0] = 360f * number / 100;
                mContainer.setBackgroundColor(Color.HSVToColor(hsvColor));

                mHandler.postDelayed(this, 500);
                if (number == 100)
                    number = 0;
            }
        };
        number = 0;
        mHandler.removeCallbacks(mChangeBackgroundRunnable);
        mHandler.postDelayed(mChangeBackgroundRunnable, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void executeBackgroundChangeUsingValueAnimator() {
        colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), getResources().getColor(R.color.red), getResources().getColor(R.color.blue));
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animator) {
                mContainer.setBackgroundColor((Integer) animator.getAnimatedValue());
            }
        });
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimation.setDuration(10 * 1000);
        colorAnimation.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
