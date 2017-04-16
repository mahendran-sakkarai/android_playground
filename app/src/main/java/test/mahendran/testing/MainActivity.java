package test.mahendran.testing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private static final String ACTIVITIES_FRAGMENT = "ACTIVITIES_FRAGMENT";
    private static final String EAT_OUT_FRAGMENT = "EAT_OUT_FRAGMENT";
    private static final String EVENTS_FRAGMENT = "EVENTS_FRAGMENT";
    private static final String YOU_FRAGMENT = "YOU_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_nv);

        openActivitiesFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.activities:
                                openActivitiesFragment();
                                break;
                            case R.id.eat_out:
                                EatOutFragment eatOutFragment = EatOutFragment.newInstance();
                                attachFragment(eatOutFragment, EAT_OUT_FRAGMENT);
                                break;
                            case R.id.events:
                                EventsFragment eventsFragment = EventsFragment.newInstance();
                                attachFragment(eventsFragment, EVENTS_FRAGMENT);
                                break;
                            case R.id.you:
                                YouFragment youFragment = YouFragment.newInstance();
                                attachFragment(youFragment, YOU_FRAGMENT);
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void openActivitiesFragment() {
        ActivitiesFragment activitiesFragment = ActivitiesFragment.newInstance();
        attachFragment(activitiesFragment, ACTIVITIES_FRAGMENT);
    }

    private void attachFragment(Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, tag)
                .disallowAddToBackStack()
                .commit();
    }
}
