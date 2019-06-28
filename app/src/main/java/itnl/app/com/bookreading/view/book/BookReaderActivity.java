package itnl.app.com.bookreading.view.book;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.managers.IDataSource;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.Comment;
import itnl.app.com.bookreading.utils.Constants;
import itnl.app.com.bookreading.view.base.BaseActivity;
import itnl.app.com.bookreading.view.book.comments.CommentFragment;
import itnl.app.com.bookreading.view.book.comments.CommentsListAdapter;
import itnl.app.com.bookreading.view.interfaces.IDownloadBookEventListener;
import itnl.app.com.bookreading.view.interfaces.OnBookLoadingListener;
import itnl.app.com.bookreading.view.interfaces.OnBookReaderListener;
import itnl.app.com.bookreading.view.usermanager.LoginfirebaseActivity;

public class BookReaderActivity extends BaseActivity implements OnBookLoadingListener, View.OnClickListener, CommentFragment.OnFragmentInteractionListener, IDownloadBookEventListener {

    private PDFView mBookPdfView;
    private Book mBook;
    private String bookUrl;
    private String bookTypeId;
    private ReceiveBookAsyncTask asyncTask;
    private View mTopView;
    private View mBottomView;
    private View mVoteView, mShareView, mDownloadView, mCommentView, mCommentLayout, mContainerCommentView;
    private ImageView mCloseImageView, mCloseComment, mSendComment;
    private RecyclerView mCommentList;
    private TextView mTitleCommentTextView, mTotalCommentTextView, mTotalVoteTextView, mNoCommentTextView;
    private EditText mCommentEditText;
    private ImageView iconVote;
    CommentsListAdapter commentListAdapter;
    public final String TAG = "BookReaderActivity: ";
    AlertDialog.Builder builder;

    private int totalVote, totalComment;

    private boolean mIsShowCommentView = false;
    private boolean isShowSupView = true;
    private boolean isShowSubViewTop = true;
    private boolean isShowSubViewBottom = true;
    private boolean isLoadFromOffline;
    private Intent currentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // MARK: must call before adding content view.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_book_reader);
        currentIntent = getIntent();
        mBook = (Book) currentIntent.getSerializableExtra("BookReader");
        bookTypeId = currentIntent.getStringExtra("BookType");
        isLoadFromOffline = currentIntent.getBooleanExtra("IsLoadOffline",true);
        if (mBook != null && isNetworkConnected){
            Log.d(TAG,"current book - " + mBook.getmChapterList().size());
            bookUrl = mBook.getmChapterList().get(0).getmChapterUrl();
            totalVote = mBook.getVote();
            totalComment = mBook.getmCommentList().size();
        }
        Log.d(TAG, "Is load from offline: "+ isLoadFromOffline);
        // for dummies
//        bookUrl = "https://firebasestorage.googleapis.com/v0/b/bookreading-5a305.appspot.com/o/books%2Fphieuluu_maohiem%2FSachvui.Com-nguoi-ca-alexander-romanovich-belyaev.pdf?alt=media&token=48c793cf-0bc4-4f06-adbc-2889738620c4";

        // find sub view
        mBookPdfView = findViewById(R.id.book_pdf_view);
        mTopView = findViewById(R.id.top_view);
        mBottomView = findViewById(R.id.bottom_view);
        mVoteView = findViewById(R.id.vote_view);
        mShareView = findViewById(R.id.share_view);
        mDownloadView = findViewById(R.id.download_view);
        mCommentView = findViewById(R.id.comment_view);
        mCloseImageView = findViewById(R.id.img_close);
        mCommentLayout = findViewById(R.id.comment_layout);
        mCloseComment = findViewById(R.id.img_close_commentview);
        mSendComment = findViewById(R.id.img_send_comment);
        mCommentList = findViewById(R.id.comment_recycler_view);
        mContainerCommentView = findViewById(R.id.container_comment_view);
        mTitleCommentTextView = findViewById(R.id.title_comment_view);
        mTotalCommentTextView = findViewById(R.id.total_comment);
        mTotalVoteTextView = findViewById(R.id.total_vote);
        mNoCommentTextView = findViewById(R.id.textview_no_comment);
        mCommentEditText = findViewById(R.id.text_field_comment);
        mContainerCommentView.setVisibility(View.INVISIBLE);
        iconVote = (ImageView)findViewById(R.id.image_vote);

        // set tag
        mBookPdfView.setTag(0);
        mTopView.setTag(1);
        mBottomView.setTag(2);
        mVoteView.setTag(3);
        mShareView.setTag(4);
        mDownloadView.setTag(5);
        mCommentView.setTag(6);
        mCloseImageView.setTag(7);
        mCommentLayout.setTag(8);
        mCloseComment.setTag(9);
        mSendComment.setTag(10);
        mContainerCommentView.setTag(11);

        // Disable zoom in pdf view
        mBookPdfView.setSwipeEnabled(false);
        mBookPdfView.setMidZoom(1);
        mBookPdfView.setMaxZoom(1);
        mBookPdfView.setMinZoom(1);

        // Load pdf data to PDFView
        if (isLoadFromOffline){
            String path;
            File file = new File(mBook.getmChapterList().get(0).getmChapterUrl());
            Log.d(TAG,file.getAbsolutePath());
            mBookPdfView.fromFile(file).onPageScroll(new OnPageScrollListener() {
                @Override
                public void onPageScrolled(int page, float positionOffset) {
                    if (((isShowSubViewTop && isShowSubViewBottom) || !isShowSubViewTop && isShowSubViewBottom)
                            && positionOffset != 0.0f) {
                        Log.d("onPageScrolled", "aaa: " + positionOffset);
                        // Set anim for bottom view to hide
                        fadeOutAndHideView(mBottomView);
                        // Set anim for top view to hide
                        fadeOutAndHideView(mTopView);
                        setShowSupView(false);
                        isShowSubViewTop = false;
                        isShowSubViewBottom = false;
                    }
                }
            }).load();
        } else {
            if (isNetworkConnected) {
                // Load data from url
                asyncTask = new ReceiveBookAsyncTask(this);
                asyncTask.execute(bookUrl);
            } else {
                //TODO: Network not connect. Show the dialog
            }
        }

        //Listener
        mBookPdfView.setOnClickListener(this);
        mVoteView.setOnClickListener(this);
        mShareView.setOnClickListener(this);
        mDownloadView.setOnClickListener(this);
        mCommentView.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);
        mCommentLayout.setOnClickListener(this);
        mCloseComment.setOnClickListener(this);
        mSendComment.setOnClickListener(this);
        mContainerCommentView.setOnClickListener(this);

        // Set data for book detail
        mTotalCommentTextView.setText("" + totalComment);
        mTotalVoteTextView.setText("" + totalVote);
        mTitleCommentTextView.setText(totalComment + " Comments");
        if (!isLoadFromOffline) {
            // Set adapter for comment list
            if (totalComment > 0) {
                if (mCommentList instanceof RecyclerView) {
                    setAdapterCommentList(mBook.getmCommentList());
                }
            } else {
                // no comment hidden recycler view and show no comment
                mCommentList.setVisibility(View.INVISIBLE);
                mNoCommentTextView.setVisibility(View.VISIBLE);
            }
        } else {
            // no comment hidden recycler view and show no comment
            mCommentList.setVisibility(View.INVISIBLE);
            mNoCommentTextView.setVisibility(View.VISIBLE);
            Toast.makeText(this,"You're reading book from offline book!", Toast.LENGTH_LONG);
        }

        if (isNetworkConnected && !isLoadFromOffline) {
            mRemoteDatasource.updateRealtimeCommentList(mBook.mId, bookTypeId, new IDataSource.IUpdateCommentListCallBack() {
                @Override
                public void onLoaded(ArrayList<Comment> comments) {
                    Log.d("BookReaderActivity: ", "comments total: " + comments.size());
                    if (commentListAdapter == null) {
                        setAdapterCommentList(sortList(mBook.getmCommentList()));
                    }
                    if (comments != null && !comments.isEmpty()) {
                        mBook.setmCommentList(sortList(comments));
                        commentListAdapter.updateCommentsList(sortList(comments));
                        totalComment = comments.size();
                        reloadData();
                    }
                }

                @Override
                public void onFailed(String error) {
                    Log.d("BookReaderActivity: ", "error - " + error);
                }
            });
        } else {
            //TODO: Network disconnect
            // no comment hidden recycler view and show no comment
            mCommentList.setVisibility(View.INVISIBLE);
            mNoCommentTextView.setVisibility(View.VISIBLE);
            setClickBottomViewItem(false);
            mNoCommentTextView.setText("You are reading from offline!");
        }
        //download process
        final DownloadBookAsyncTask downloadAsync = new DownloadBookAsyncTask(getApplicationContext(),this);
        builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //clicked OK button
                downloadAsync.execute(mBook);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //cancelled the dialog
                dialog.dismiss();
            }
        });
    }

    public void reloadData(){
        if (commentListAdapter == null) {
            setAdapterCommentList(sortList(mBook.getmCommentList()));
        } else {
            Log.d("BookReaderActivity: ","reloadData: notifyDataSetChanged");
            commentListAdapter.notifyDataSetChanged();
            //Update view
            // Set data for book detail
            mTotalCommentTextView.setText("" + totalComment);
            mTitleCommentTextView.setText(totalComment + " Comments");
            mCommentList.setVisibility(View.VISIBLE);
            mNoCommentTextView.setVisibility(View.INVISIBLE);
        }
    }
    private void setClickBottomViewItem(boolean clickEnable){
        Log.d(TAG,"setClickBottomViewItem: "+ clickEnable);
        mVoteView.setClickable(clickEnable);
        mCommentView.setClickable(clickEnable);
        mShareView.setClickable(clickEnable);
        mDownloadView.setClickable(clickEnable);
    }
    private void setAdapterCommentList(ArrayList<Comment> comments){
        final Context context = mCommentList.getContext();
        final RecyclerView recyclerView = (RecyclerView) mCommentList;
        // Add divider for recycler view
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        commentListAdapter = new CommentsListAdapter(context, comments);
        recyclerView.setAdapter(commentListAdapter);
    }

    @Override
    public void onProgressUpdate(Void... values) {
        Log.d("ReceiveBookAsyncTask ", "onProgressUpdate");
    }

    @Override
    public void onPostExecute(InputStream inputStream) {
        Log.d("ReceiveBookAsyncTask ", "onPostExecute");
        mBookPdfView.fromStream(inputStream).onPageScroll(new OnPageScrollListener() {
            @Override
            public void onPageScrolled(int page, float positionOffset) {
                if (((isShowSubViewTop && isShowSubViewBottom) || !isShowSubViewTop && isShowSubViewBottom)
                        && positionOffset != 0.0f) {
                    Log.d("onPageScrolled", "aaa: " + positionOffset);
                    // Set anim for bottom view to hide
                    fadeOutAndHideView(mBottomView);
                    // Set anim for top view to hide
                    fadeOutAndHideView(mTopView);
                    setShowSupView(false);
                    isShowSubViewTop = false;
                    isShowSubViewBottom = false;
                }
            }
        }).load();
        // close progress dialog
        closeProgressDialog();
    }

    @Override
    public void onPreExecute() {
        Log.d("ReceiveBookAsyncTask ", "onPreExecute");
        showProgressDialog();
    }

    public void setShowSupView(boolean showSupView) {
        isShowSupView = showSupView;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        Log.d("onClick", "tagggg view: " + tag);
        switch (tag) {
            case 0: //mBookPdfView
                if (!mIsShowCommentView) {
                    Log.d("BookReaderActivity: ", "mBookPdfView" + "isShowSubViewTop: " + isShowSubViewTop + " --isShowSubViewBottom: "+ isShowSubViewBottom);
                    if (isShowSubViewTop && isShowSubViewBottom) {
                        // Set anim for bottom view to hide
                        fadeOutAndHideView(mBottomView);
                        // Set anim for top view to hide
                        fadeOutAndHideView(mTopView);
                        setShowSupView(false);
                        isShowSubViewTop = false;
                        isShowSubViewBottom = false;
                    } else if (!isShowSubViewTop && isShowSubViewBottom) {
                        // Set anim for top view to show
                        fadeInAndShowView(mTopView);
                        isShowSubViewTop = true;
                    } else {
                        // Set anim for bottom view to show
                        fadeInAndShowView(mBottomView);
                        // Set anim for top view to show
                        fadeInAndShowView(mTopView);
                        setShowSupView(true);
                        isShowSubViewTop = true;
                        isShowSubViewBottom = true;
                    }
                }
                break;
            case 1: // mTopView
            case 2: // mBottomView
                break;
            case 3: // mVoteView
                Log.d("BookReaderActivity: ", "mVoteView");
                vote();
                iconVote.setImageResource(R.drawable.vote_green_ic3);
                mVoteView.setEnabled(false);
                break;
            case 4: // mShareView
                Log.d("BookReaderActivity: ", "mShareView");
                break;
            case 5: // mDownloadView
                Log.d("BookReaderActivity: ", "mDownloadView");
                // Download new
                        if(FirebaseAuth.getInstance().getCurrentUser() == null){
                            Intent intent = new Intent(getApplicationContext() , LoginfirebaseActivity.class);
                            startActivity(intent);
                        } else {
                            Log.d("BookReaderActivity: ", "user current " + FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            // Create the AlertDialog
                            AlertDialog dialog = builder.create();
                            dialog.setTitle("Download book");
                            dialog.setMessage("Bạn có muốn download?");
                            dialog.show();
                        }
                break;
            case 6: // mCommentView
                Log.d("BookReaderActivity: ", "mCommentView");
                // show comment view
                fadeInAndShowView(mContainerCommentView);
//                mContainerCommentView.setVisibility(View.VISIBLE);
                hidenSubCommentView(View.VISIBLE);
                //set show comment view = true
                mIsShowCommentView = true;
                hideSubView();
//                slideWithPosition(mContainerCommentView,0,0,mContainerCommentView.getHeight(),0);
                bottomViewSlideUp(mCommentLayout);
                setClickableForCommentView(true);

                break;
            case 7: // mCloseImageView
                Log.d("BookReaderActivity: ", "mCloseImageView");
                onBackPressed();
                break;
            case 8: // mCommentLayout
                Log.d("BookReaderActivity: ", "mCommentLayout");
                break;
            case 9: // mCloseCommentView
                Log.d("BookReaderActivity: ", "mCloseCommentView");
                //Close comment view
                closeCommentView();
                break;
            case 10: // mSendComment
                Log.d("BookReaderActivity: ", "mSendComment");
                addNewComment();
                break;
            case 11: // mContainerCommentView
                Log.d("BookReaderActivity: ", "mContainerCommentView");
                // Close comment view when click in container
                closeCommentView();
                break;
            default:
                Log.d("", "view tag: " + tag);
                break;
        }
    }
    /*
    * Func to add new comment to list
    */
    private void addNewComment(){
       String newComment =  mCommentEditText.getText().toString();
       if(newComment.equals("")){
           return;
       }
       mRemoteDatasource.addComment(newComment,mBook.getmId(),bookTypeId);
       mCommentEditText.setText("");
       hideSoftKeyboard();
    }
    //vote process
    private void vote(){
        mTotalVoteTextView.setText(mRemoteDatasource.vote(mBook.getmId(),bookTypeId)+ 1+"");
        hideSoftKeyboard();
    }

    private void closeCommentView(){
        // set show sub view bottom = true
        isShowSubViewBottom = true;
        //Set show comment view = false
        mIsShowCommentView = false;
        // hide comment view
        bottomViewSlideDown(mCommentLayout);
        hidenSubCommentView(View.INVISIBLE);
        fadeOutAndHideView(mContainerCommentView);
//        mContainerCommentView.setVisibility(View.GONE);
        // disable clickable
        setClickableForCommentView(false);
        // Still show bottom sub view
        mBottomView.setVisibility(View.VISIBLE);
    }
    private void hidenSubCommentView(int isHidden){
        mCommentList.setVisibility(isHidden);
        mCloseComment.setVisibility(isHidden);
        mSendComment.setVisibility(isHidden);
        mCommentEditText.setVisibility(isHidden);
        mNoCommentTextView.setVisibility(isHidden);
        mTitleCommentTextView.setVisibility(isHidden);
        mCommentLayout.setVisibility(isHidden);
    }
    private void setClickableForCommentView(boolean clickable){
        mCommentList.setClickable(clickable);
        mContainerCommentView.setClickable(clickable);
        mCommentLayout.setClickable(clickable);
        mCloseComment.setClickable(clickable);
        mSendComment.setClickable(clickable);
        mCommentEditText.setClickable(clickable);
        mNoCommentTextView.setClickable(clickable);
        mTitleCommentTextView.setClickable(clickable);
    }
    private void hideSubView(){
        if (isShowSubViewTop) {
            // Set anim for top view to hide
            fadeOutAndHideView(mTopView);
            isShowSubViewTop = false;
        }
        // Set anim for bottom view to hide
        fadeOutAndHideView(mBottomView);
        setShowSupView(false);
        isShowSubViewBottom = false;

    }
    private void fadeOutAndHideView(final View view) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(500);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        view.startAnimation(fadeOut);
    }

    private void fadeInAndShowView(final View view) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(500);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });

        view.startAnimation(fadeIn);
    }

    // slide the view from below itself to the current position
    public void bottomViewSlideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(600);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void bottomViewSlideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(600);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }


    // slide the view from its current position to below itself
    public void slideWithPosition(View view, float fromX, float toX, float fromY, float toY) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                fromX,                 // fromXDelta
                toX,                 // toXDelta
                fromY,                 // fromYDelta
                toY); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    //sort list comment
    public static ArrayList<Comment> sortList (ArrayList<Comment>list) {
        Comment tmp;
        for (int i = 0; i < list.size()-1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (Integer.parseInt(list.get(j).getmCommentId().substring(7)) < Integer.parseInt(list.get(i).getmCommentId().substring(7))) {
                    tmp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, tmp);
                }
            }
        }
        return list;
    }
    @Override
    public void onBackPressed() {
        if (mContainerCommentView.getVisibility() == View.VISIBLE){
            closeCommentView();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onDownloadBookSuccess(Book book) {
        Log.d(TAG,"onDownloadBookSuccess: " + book.getTitle());
    }
}
