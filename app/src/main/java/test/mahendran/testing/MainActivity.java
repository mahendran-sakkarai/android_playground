package test.mahendran.testing;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ItemsAdapter mAdapter;
    private boolean loading;
    private int finalCount = 89;
    private int previousTotal = 0;
    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ItemsAdapter();
        mRecyclerView.setAdapter(mAdapter);

        new UpdateRecyclerViewTask().execute(1);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int listItemCount = recyclerView.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!loading && (totalItemCount - listItemCount) <= (firstVisibleItemPosition + 5) && totalItemCount < finalCount) {
                    mAdapter.setLoading();
                    currentPage++;
                    new UpdateRecyclerViewTask().execute(mAdapter.getItemCount());
                    loading = true;
                }

                if (totalItemCount == finalCount) {
                    mAdapter.setThatsAll();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

    public class UpdateRecyclerViewTask extends AsyncTask<Integer, String, ArrayList<String>> {

        @Override
        protected ArrayList<String> doInBackground(Integer... value) {
            ArrayList<String> items = new ArrayList<>();
            int sizeToCheck = value[0] + 20;
            for (int i = value[0]; i < sizeToCheck; i++) {
                if (i <= finalCount)
                    items.add(String.valueOf(i));
            }
            SystemClock.sleep(5 * 1000);
            return items;
        }

        @Override
        protected void onPostExecute(ArrayList<String> items) {
            super.onPostExecute(items);
            loading = false;
            mAdapter.removeLoading();
            mAdapter.updateItems(items);
            if (mAdapter.getItems().size() == finalCount)
                mAdapter.setThatsAll();
        }
    }
}
