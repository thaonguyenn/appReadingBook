package itnl.app.com.bookreading.view.base;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.application.BookApplication;
import itnl.app.com.bookreading.managers.local.LocalDataSource;
import itnl.app.com.bookreading.managers.remote.RemoteDataSource;
import itnl.app.com.bookreading.services.FirebaseServices;
import itnl.app.com.bookreading.utils.CustomProgressDialog;
import itnl.app.com.bookreading.utils.NetworkStateChangeReceiver;
import itnl.app.com.bookreading.utils.Utils;


/**
 * Create by nguyennv on 11/4/18
 */
public abstract class BaseActivity extends AppCompatActivity {
    ProgressBar mProgressBar;
    ProgressDialog mProgressDialog;
    protected RemoteDataSource mRemoteDatasource;
    protected LocalDataSource mLocalDatasource;
    private BookApplication mBookApplication;
    protected boolean isNetworkConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookApplication = (BookApplication) this.getApplication();
        mRemoteDatasource = mBookApplication.getRemoteDataSource();
        mLocalDatasource = mBookApplication.getLocalDataSource();

//        mProgressBar.setProgressDrawable();
        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE, false);
                String networkStatus = isNetworkAvailable ? "connected" : "disconnected";

                Toast.makeText(context,"Network was change",Toast.LENGTH_LONG);
            }
        }, intentFilter);

        isNetworkConnected = Utils.getConnectionStatus(getApplicationContext());
        Log.d("BaseActivity: ","isNetworkConnected: " + isNetworkConnected);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void showProgressDialog() {
//        this.mProgressBar.setVisibility(View.VISIBLE);
//        this.mProgressDialog.show();
        CustomProgressDialog.show(this,null,"Loading...",true);
    }
    protected void closeProgressDialog() {
//        this.mProgressBar.setVisibility(View.INVISIBLE);
//        this.mProgressDialog.hide();
        CustomProgressDialog.dismissDialog();
    }
    /**
     * Hides the soft keyboard
     */
    protected void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * Shows the soft keyboard
     */
    public void showSoftKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        view.requestFocus();
        inputMethodManager.showSoftInput(view, 0);
    }
}
