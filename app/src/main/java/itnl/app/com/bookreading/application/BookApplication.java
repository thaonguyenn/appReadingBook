package itnl.app.com.bookreading.application;

import android.app.Application;

import io.paperdb.Paper;
import itnl.app.com.bookreading.managers.local.LocalDataSource;
import itnl.app.com.bookreading.managers.remote.RemoteDataSource;
import itnl.app.com.bookreading.services.FirebaseServices;

/**
 * Create by nguyennv on 12/16/18
 */
public class BookApplication extends Application {
    FirebaseServices firebaseServices;
    RemoteDataSource remoteDataSource;
    LocalDataSource localDataSource;

    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(this);
        remoteDataSource = RemoteDataSource.getInstance();
        localDataSource = LocalDataSource.getInstance();
        firebaseServices = FirebaseServices.getInstance();
    }

    public FirebaseServices getFirebaseServices() {
        return firebaseServices;
    }

    public void setFirebaseServices(FirebaseServices firebaseServices) {
        this.firebaseServices = firebaseServices;
    }

    public RemoteDataSource getRemoteDataSource() {
        return remoteDataSource;
    }

    public void setRemoteDataSource(RemoteDataSource remoteDataSource) {
        this.remoteDataSource = remoteDataSource;
    }

    public LocalDataSource getLocalDataSource() {
        return localDataSource;
    }

    public void setLocalDataSource(LocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }
}
