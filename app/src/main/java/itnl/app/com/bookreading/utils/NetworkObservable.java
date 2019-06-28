package itnl.app.com.bookreading.utils;

import android.util.Log;

import java.util.Observable;

/**
 * Create by nguyennv on 12/4/18
 */
public class NetworkObservable extends Observable {
    private boolean isNetworkConnected;
    public final String TAG = "NetworkObservable: ";

    public void setConnected(boolean connected) {
        Log.d(TAG,"setConnected " + connected);
        synchronized (this) {
            isNetworkConnected = connected;
        }
        setChanged();
        notifyObservers();
    }

    public synchronized boolean isConnected() {
        Log.d(TAG,"isConnected " + isNetworkConnected);
        return isNetworkConnected;
    }
}
