package test.mahendran.testing;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        AdapterRecylerView mAdapter = new AdapterRecylerView(this);
        recyclerView.setAdapter(mAdapter);
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
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private class AdapterRecylerView extends RecyclerView.Adapter<AdapterRecylerView.ViewHolder> {
        private static final int TYPE_HEADER = 1;
        private static final int TYPE_BANNER = 2;
        private static final int TYPE_FIRST_LIST_ITEM = 3;
        private static final int TYPE_HEADER_AFTER_FIRST_LIST_ITEM = 4;
        private static final int TYPE_SECOND_LIST_ITEM = 5;
        private Context context;

        public AdapterRecylerView(Context context) {
            this.context = context;
        }

        @Override
        public AdapterRecylerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.activity_list_item, parent, false);
                ViewHolder vh = new ViewHolder(v, viewType);

                return vh;
            } else if (viewType == TYPE_BANNER) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_checked, parent, false);
                ViewHolder vh = new ViewHolder(v, viewType);

                return vh;
            } else if (viewType == TYPE_FIRST_LIST_ITEM) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                LinearLayout lv = new LinearLayout(parent.getContext());
                lv.setLayoutParams(layoutParams);
                View v = new TextView(parent.getContext());
                v.setLayoutParams(layoutParams);
                v.setId(R.id.reservedId);
                lv.addView(v);

                ViewHolder vh = new ViewHolder(lv, viewType);
                return vh;
            } else if (viewType == TYPE_HEADER_AFTER_FIRST_LIST_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_checked, parent, false);
                ViewHolder vh = new ViewHolder(v, viewType);

                return vh;
            } else if (viewType == TYPE_SECOND_LIST_ITEM) {
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.two_line_list_item, parent, false);
                ViewHolder vh = new ViewHolder(v, viewType);

                return vh;
            }

            throw new RuntimeException("No View Found for view type");
        }

        @Override
        public void onBindViewHolder(AdapterRecylerView.ViewHolder holder, int position) {
            if (isPositionHeader(position)) {
                holder.icon.setImageResource(R.mipmap.ic_launcher);
                holder.mTextView.setText("header " + (position + 1));
            } else if (isPositionBanner(position)) {
                holder.mTextView.setText("banner " + (position + 1));
            } else if (isPositionFirstListItems(position)) {
                holder.mTextView.setText("list item " + (position + 1));
            } else if (isPositionHeaderAfterFirstListItem(position)) {
                holder.mTextView.setText("second header " + (position + 1));
            } else if (isPositionSecondListItem(position)) {
                holder.mTextView.setText("second list first line " + (position + 1));
                holder.mTextView1.setText("second list second line " + (position + 1));
                holder.mTextView1.setBackgroundResource(android.R.drawable.divider_horizontal_bright);
            }
        }

        @Override
        public int getItemCount() {
            return 100;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            } else if (isPositionBanner(position)) {
                return TYPE_BANNER;
            } else if (isPositionFirstListItems(position)) {
                return TYPE_FIRST_LIST_ITEM;
            } else if (isPositionHeaderAfterFirstListItem(position)) {
                return TYPE_HEADER_AFTER_FIRST_LIST_ITEM;
            } else if (isPositionSecondListItem(position)) {
                return TYPE_SECOND_LIST_ITEM;
            }
            return TYPE_SECOND_LIST_ITEM;
        }

        private boolean isPositionSecondListItem(int position) {
            if(position > 50 && position <= 100)
                return true;
            return false;
        }

        private boolean isPositionHeaderAfterFirstListItem(int position) {
            if(position == 50)
                return true;

            return false;
        }

        private boolean isPositionFirstListItems(int position) {
            if(position > 1 && position <= 49)
                return true;
            return false;
        }

        private boolean isPositionBanner(int position) {
            if(position == 1)
                return true;

            return false;
        }

        private boolean isPositionHeader(int position) {
            if(position == 0)
                return true;
            return false;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView icon;
            public TextView mTextView;
            public TextView mTextView1;

            public ViewHolder(View itemView, int viewType) {
                super(itemView);
                if (viewType == TYPE_HEADER) {
                    icon = (ImageView) itemView.findViewById(android.R.id.icon);
                    mTextView = (TextView) itemView.findViewById(android.R.id.text1);
                } else if (viewType == TYPE_BANNER) {
                    mTextView = (TextView) itemView.findViewById(android.R.id.text1);
                } else if (viewType == TYPE_FIRST_LIST_ITEM) {
                    mTextView = (TextView) itemView.findViewById(R.id.reservedId);
                } else if (viewType == TYPE_HEADER_AFTER_FIRST_LIST_ITEM) {
                    mTextView = (TextView) itemView.findViewById(android.R.id.text1);
                } else if (viewType == TYPE_SECOND_LIST_ITEM) {
                    mTextView = (TextView) itemView.findViewById(android.R.id.text1);
                    mTextView1 = (TextView) itemView.findViewById(android.R.id.text2);
                }
            }
        }
    }
}
