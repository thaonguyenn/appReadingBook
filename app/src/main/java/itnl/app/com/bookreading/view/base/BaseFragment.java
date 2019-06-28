package itnl.app.com.bookreading.view.base;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.application.BookApplication;
import itnl.app.com.bookreading.global.Global;
import itnl.app.com.bookreading.managers.local.LocalDataSource;
import itnl.app.com.bookreading.managers.remote.RemoteDataSource;
import itnl.app.com.bookreading.utils.CustomProgressDialog;
import itnl.app.com.bookreading.utils.NetworkObservable;


/**
 * Create by nguyennv on 12/4/18
 */
public abstract class BaseFragment extends Fragment implements Observer {
    private BookApplication mBookApplication;
    protected RemoteDataSource mRemoteDatasource;
    protected LocalDataSource mLocalDatasource;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBookApplication = (BookApplication) this.getActivity().getApplication();
        mRemoteDatasource = mBookApplication.getRemoteDataSource();
        mLocalDatasource = mBookApplication.getLocalDataSource();
    }

    @Override
    public void onResume() {
        super.onResume();
        Global.isConnected.addObserver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Global.isConnected.deleteObserver(this);
    }
    public void onNetworkChange(boolean isConnected){}

    @Override
    public void update(Observable o, Object arg) {
        boolean isConnected = ((NetworkObservable) o).isConnected();
        onNetworkChange(isConnected);
        Toast.makeText(BaseFragment.this.getActivity(), "Network changed", Toast.LENGTH_LONG).show();
    }

    protected void showProgressDialog() {
//        this.mProgressBar.setVisibility(View.VISIBLE);
//        this.mProgressDialog.show();
        CustomProgressDialog.show(getActivity(),null,"Loading...",true);
    }
    protected void closeProgressDialog() {
//        this.mProgressBar.setVisibility(View.INVISIBLE);
//        this.mProgressDialog.hide();
        Log.d("TAGGG","closeProgressDialog()");
        CustomProgressDialog.dismissDialog();
    }

}
