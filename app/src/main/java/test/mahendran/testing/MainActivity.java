package test.mahendran.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button aButton = findViewById(R.id.a);
        Button bButton = findViewById(R.id.b);
        Button cButton = findViewById(R.id.c);
        Button dButton = findViewById(R.id.d);

        aButton.setOnClickListener(this);
        bButton.setOnClickListener(this);
        cButton.setOnClickListener(this);
        dButton.setOnClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.a:
                openFragment(AFragment.class);
                break;
            case R.id.b:
                openFragment(BFragment.class);
                break;
            case R.id.c:
                openFragment(CFragment.class);
                break;
            case R.id.d:
                openFragment(DFragment.class);
                break;
        }
    }

    private void openFragment(Class<?> cls) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.e("TEST", "BackStack Count After before:" + fragmentManager.getBackStackEntryCount());
        boolean isPoped = fragmentManager.popBackStackImmediate(cls.getName(), 0);
        Log.e("TEST", "BackStack Count After before:" + fragmentManager.getBackStackEntryCount());
        if (!isPoped && fragmentManager.findFragmentByTag(cls.getName()) == null) {
            attachFragment(cls.getName());
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else
            super.onBackPressed();
    }

    private void attachFragment(String className) {
        Fragment fragment = null;
        if (className.equals(AFragment.class.getName())) {
            fragment = new AFragment();
        } else if (className.equals(BFragment.class.getName())) {
            fragment = new BFragment();
        } else if (className.equals(CFragment.class.getName())) {
            fragment = new CFragment();
        } else if (className.equals(DFragment.class.getName())) {
            fragment = new DFragment();
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(fragment.getClass().getName()).commit();

            Log.e("TEST", "BackStack Count After adding:" + getSupportFragmentManager().getBackStackEntryCount());
        }
    }

    public static class AFragment extends Fragment {
        public AFragment() {}

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dummy_a, container, false);
        }
    }

    public static class BFragment extends Fragment {
        public BFragment() {}

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dummy_b, container, false);
        }
    }

    public static class CFragment extends Fragment {
        public CFragment() {}

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dummy_c, container, false);
        }
    }

    public static class DFragment extends Fragment {
        public DFragment() {}

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dummy_d, container, false);
        }
    }
}
