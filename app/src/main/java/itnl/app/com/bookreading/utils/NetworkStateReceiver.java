package itnl.app.com.bookreading.utils;

import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import itnl.app.com.bookreading.global.Global;

/**
 * Create by nguyennv on 1/7/19
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    public final String TAG = "NetworkStateReceiver: ";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Utils.getConnectionStatus(context)) {
            Log.d(TAG,"onReceive " + true);
            Global.isConnected.setConnected(true);
        } else {
            Log.d(TAG,"onReceive " + false);
            Global.isConnected.setConnected(false);
        }
    }
}
