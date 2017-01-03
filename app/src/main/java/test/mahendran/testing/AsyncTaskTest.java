package test.mahendran.testing;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ThreadPoolExecutor;

public class AsyncTaskTest extends AppCompatActivity {
    private TextView mTv;
    private WeakReference<DownloadTask> mAsyncTaskContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task_test);

        mTv = (TextView) findViewById(R.id.tv);

        this.mAsyncTaskContext = (WeakReference<DownloadTask>) getLastCustomNonConfigurationInstance();

        if (mAsyncTaskContext != null && mAsyncTaskContext.get() != null) {
            mAsyncTaskContext.get().attach(this);
        } else {
            DownloadTask downloadTask = new DownloadTask(this);
            mAsyncTaskContext = new WeakReference<>(downloadTask);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // AsyncTask.THREAD_POOL_EXECUTOR - Execute multiple asynctask at once
                // AsyncTask.SERIAL_EXECUTOR - Execute asynctask in queue. Once at a time.
                downloadTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, "https://github.com/nickbutcher/plaid/archive/master.zip");
            } else {
                downloadTask.execute("https://github.com/nickbutcher/plaid/archive/master.zip");
            }
        }
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        WeakReference<DownloadTask> weakReference = null;

        if (this.mAsyncTaskContext != null &&
                this.mAsyncTaskContext.get() != null &&
                !this.mAsyncTaskContext.get().getStatus().equals(AsyncTask.Status.FINISHED)) {
            weakReference = this.mAsyncTaskContext;
        }

        return weakReference;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mAsyncTaskContext.get().cancel(true);
    }

    static class DownloadTask extends AsyncTask<String, Integer, String> {
        private PowerManager.WakeLock mWakeLock;
        private WeakReference<AsyncTaskTest> mActivityInstance;

        public DownloadTask(AsyncTaskTest activity) {
            this.mActivityInstance = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            PowerManager pm = (PowerManager) mActivityInstance.get().getSystemService(POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            mWakeLock.acquire();
        }

        @Override
        protected String doInBackground(String... urls) {
            InputStream inputStream = null;
            OutputStream outputStream = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                inputStream = connection.getInputStream();
                String directoryString = Environment.DIRECTORY_DOWNLOADS + "/testing/";
                File directory = Environment.getExternalStoragePublicDirectory(directoryString);
                if (!directory.exists())
                    directory.mkdir();

                outputStream = new FileOutputStream(Environment.getExternalStoragePublicDirectory(directoryString) + "/abc.zip");

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
                        publishProgress((int) (total * 100 / fileLength));

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
            AsyncTaskTest activity = this.mActivityInstance.get();
            activity.mTv.setText("" + values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            if (result != null)
                Toast.makeText(mActivityInstance.get(), "Download error: " + result, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(mActivityInstance.get(), "File downloaded", Toast.LENGTH_SHORT).show();

            AsyncTaskTest activity = this.mActivityInstance.get();
            activity.mTv.setText("Finished");

            super.onPostExecute(result);
        }

        public void attach(AsyncTaskTest activity) {
            this.mActivityInstance = new WeakReference<>(activity);
        }
    }
}
