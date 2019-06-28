package itnl.app.com.bookreading.view.book;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.view.interfaces.IDownloadBookEventListener;

/**
 * Create by nguyennv on 1/7/19
 */
public class DownloadBookAsyncTask extends AsyncTask<Book, Void, Book> {

    public final String TAG = "DownloadBookAsyncTask: ";
    private Context context;
    private DownloadManager downloadManager;
    private DownloadManager.Request request, request1;
    private long downloadReference, downloadReference1;
    private BroadcastReceiver onComplete;
    private Book mCurrentBook;
    private String bookSrcUrl, fileNameBook, filenameThumnail, thumbSrcUrl, thumbPathDownloaded, bookPathDownloaded;
    private IDownloadBookEventListener mListener;

    public DownloadBookAsyncTask(Context context, IDownloadBookEventListener listener) {
        this.context = context;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "Start downloading.......", Toast.LENGTH_SHORT).show();
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param books The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Book doInBackground(Book... books) {
        // Sync data
        this.mCurrentBook = books[0];
        bookSrcUrl = mCurrentBook.getmChapterList().get(0).getmChapterUrl();
        thumbSrcUrl = mCurrentBook.getThumbnail_url();
        fileNameBook = mCurrentBook.getmId() + ".pdf";
        filenameThumnail = "thumbnail" + mCurrentBook.getmId() + ".jpg";

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        DownloadManager downloadManager1 = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        //Download Thumbnail
        request1 = new DownloadManager.Request(Uri.parse(thumbSrcUrl));
        request1.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request1.setTitle(filenameThumnail);
        request1.setDescription("Download using Download Manager");
        request1.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_PICTURES, filenameThumnail);
        //get url thumbnail video in External Storage
        thumbPathDownloaded = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + filenameThumnail;
        Log.d(TAG, "Link src thumbPathDownloaded: " + thumbPathDownloaded);

        // Download video
        request = new DownloadManager.Request(Uri.parse(bookSrcUrl));
        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(fileNameBook);
        request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOCUMENTS, fileNameBook);
        //Enqueue download and save into referenceID
        downloadReference = downloadManager.enqueue(request);
        downloadReference1 = downloadManager.enqueue(request1);
        // get url video in External Storage
        bookPathDownloaded = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath() + "/" + fileNameBook;
        Log.d(TAG,"Link src bookPathDownloaded: " + bookPathDownloaded);

        return mCurrentBook;
    }

    @Override
    protected void onPostExecute(Book book) {
        super.onPostExecute(book);
        onComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long referenceID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (referenceID == downloadReference1) {
                    Toast.makeText(context, "Download Image Done!", Toast.LENGTH_LONG).show();
                } else if (referenceID == downloadReference) {
                    Toast.makeText(context, "Download PDF Done!))))", Toast.LENGTH_LONG).show();
                }
                Log.d(TAG, "Download Complete!");

            }
        };
        context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        mCurrentBook.setThumbnail_url(thumbPathDownloaded);
        mCurrentBook.getmChapterList().get(0).setmChapterUrl(bookPathDownloaded);
        mListener.onDownloadBookSuccess(mCurrentBook);
    }
}
