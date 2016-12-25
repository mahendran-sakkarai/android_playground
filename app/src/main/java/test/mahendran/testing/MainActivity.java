package test.mahendran.testing;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextView = (TextView) findViewById(R.id.textview);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadTask(MainActivity.this).execute("https://github.com/nickbutcher/plaid/archive/master.zip");
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

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        private final Context mContext;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) mContext.getSystemService(POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
        }

        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection connection = null;
            try{
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " +connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                inputStream = connection.getInputStream();
                String directoryString = Environment.DIRECTORY_DOWNLOADS + "/testing/";
                File directory = Environment.getExternalStoragePublicDirectory(directoryString);
                if (!directory.exists())
                    directory.mkdir();

                outputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(directoryString)+"/abc.zip");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    if (isCancelled()) {
                        inputStream.close();
                        return null;
                    }
                    total += count;
                    if (fileLength > 0)
                        publishProgress((int)(total * 100 / fileLength));

                    outputStream.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (outputStream != null)
                        outputStream.close();
                    if (inputStream != null)
                        inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mTextView.setText(""+values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            if (result != null)
                Toast.makeText(mContext,"Download error: "+result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(mContext,"File downloaded", Toast.LENGTH_SHORT).show();

            mTextView.setText("Finished");
            super.onPostExecute(result);
        }
    }
}
