package test.mahendran.testing;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.POWER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {
    private TextView mTextView;

    public TestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * This is the main use for this branch.
         * This one is to keep the fragment instance when the orientation change.
         * So the asynctask which is attached to the fragment is still update the ui after the
         * orientation is changed.
         */
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_test, container, false);
        mTextView = (TextView) v.findViewById(R.id.textview);
        return v;
    }

    public void startDownload() {
        new DownloadTask(getContext()).execute("https://github.com/nickbutcher/plaid/archive/master.zip");
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
