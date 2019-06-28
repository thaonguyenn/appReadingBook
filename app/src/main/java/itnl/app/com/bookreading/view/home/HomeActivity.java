package itnl.app.com.bookreading.view.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.services.FirebaseServices;
import itnl.app.com.bookreading.view.base.BaseActivity;

public class HomeActivity extends BaseActivity {
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pdfView = findViewById(R.id.pdfView);
        pdfView.setSwipeEnabled(false);
        pdfView.setMaxZoom(0);
        pdfView.setMinZoom(0);
//        FirebaseServices mFirebaseService = new FirebaseServices();
//        String tag = mFirebaseService.getFirebaseTag();
//        Log.d("nguyennv6",tag);
//        Toast.makeText(HomeActivity.this,tag,Toast.LENGTH_LONG);
        ReceivePDFView asyn = new ReceivePDFView();

        AsyncTask abc = new ReceivePDFView().execute("https://firebasestorage.googleapis.com/v0/b/livevideo-c6125.appspot.com/o/book%2Fharrypotter%2Feng%2FJ.K.%20Rowling%20-%20HP%201%20-%20Harry%20Potter%20and%20the%20Sorcerers%20Stone.pdf?alt=media&token=d742b00c-babb-4e94-b22a-e93131d3cc72");

    }
    class ReceivePDFView extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
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
            Toast.makeText(HomeActivity.this, "Loading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            super.onPostExecute(inputStream);
            Toast.makeText(HomeActivity.this, "Loading success", Toast.LENGTH_LONG).show();
            pdfView.fromStream(inputStream).load();
        }
    }
}

