package itnl.app.com.bookreading.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Create by nguyennv on 10/13/18
 */
public class FirebaseInstance extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Tag", "Refreshed token: " + refreshToken);

    }
}
