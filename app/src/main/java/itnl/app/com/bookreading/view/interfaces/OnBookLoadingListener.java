package itnl.app.com.bookreading.view.interfaces;

import java.io.InputStream;

/**
 * Create by nguyennv on 12/19/18
 */
public interface OnBookLoadingListener {
    void onProgressUpdate(Void... values);
    void onPostExecute(InputStream inputStream);
    void onPreExecute();
}
