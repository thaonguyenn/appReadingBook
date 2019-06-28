package itnl.app.com.bookreading.view.book;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import itnl.app.com.bookreading.view.interfaces.OnBookLoadingListener;

/**
 * Create by nguyennv on 12/19/18
 */
public class ReceiveBookAsyncTask extends AsyncTask<String, Void, InputStream> {

    private OnBookLoadingListener mListenner;
    public ReceiveBookAsyncTask(OnBookLoadingListener listener) {
        this.mListenner = listener;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param strings The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected InputStream doInBackground(String... strings) {
        Log.d("ReceiveBookAsyncTask ","doInBackground");
        InputStream inputStream = null;
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                inputStream = new BufferedInputStream(connection.getInputStream());
            }
        } catch (IOException e) {

        }
        return inputStream;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        mListenner.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(InputStream inputStream) {
        super.onPostExecute(inputStream);
        mListenner.onPostExecute(inputStream);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListenner.onPreExecute();
    }
}
