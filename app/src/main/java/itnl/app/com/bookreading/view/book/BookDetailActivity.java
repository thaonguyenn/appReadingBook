package itnl.app.com.bookreading.view.book;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.BinaryOperator;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.managers.IDataSource;
import itnl.app.com.bookreading.managers.remote.RemoteDataSource;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.Chapter;
import itnl.app.com.bookreading.models.Comment;
import itnl.app.com.bookreading.view.base.BaseActivity;
import itnl.app.com.bookreading.view.interfaces.IDownloadBookEventListener;
import itnl.app.com.bookreading.view.interfaces.OnBookReaderListener;
import itnl.app.com.bookreading.view.usermanager.LoginfirebaseActivity;

public class BookDetailActivity extends BaseActivity implements View.OnClickListener, IDownloadBookEventListener {

    public final String TAG = "BookDetailActivity: ";
    OnBookReaderListener mOnBookReaderListener;
    TextView listChapter, description, title, author, vote, textViewLoading;
    LinearLayout mLinearLayout;
    Button btSeeMore, btBrief, btAdd, btRead, btdownFull;
    ListView listViewComment, listViewChapter;
    CommentAdapter commentAdapter;
    ChapterAdapter chapterAdapter;
    ArrayList<Comment> listComments = null;
    ArrayList<Chapter> listChapters = null;
    ImageView img_title;
    EditText commentText ;
    Intent intent;
    String idBook, typeBook;
    Book bookD;
    DownloadBookAsyncTask downloadAsync;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        //get data intent
        intent = getIntent();
        idBook = intent.getStringExtra("idBook");
        typeBook = intent.getStringExtra("typeBook");

        // create data

        mLinearLayout = findViewById(R.id.book_detail_linear);
        textViewLoading = findViewById(R.id.txtLoading);
        listChapter = (TextView)findViewById(R.id.listChapter);
        btSeeMore = (Button)findViewById(R.id.seeMore);
        btRead = (Button)findViewById(R.id.read);
        btBrief = (Button)findViewById(R.id.brief);
        btAdd = (Button)findViewById(R.id.add);
        btdownFull = (Button)findViewById(R.id.downloadBook);
        description = (TextView)findViewById(R.id.des);
        title = (TextView)findViewById(R.id.theme);
        author = (TextView)findViewById(R.id.author);
        vote = (TextView)findViewById(R.id.vote);
        img_title = (ImageView)findViewById(R.id.image_title);
        commentText = (EditText)findViewById(R.id.commentText);
        commentText.setTextColor(getResources().getColor(R.color.text_color));
        btBrief.setVisibility(View.INVISIBLE);
        mLinearLayout.setVisibility(View.INVISIBLE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.showProgressDialog();

        //listComments
        listComments = new ArrayList<Comment>();
        listViewComment = (ListView)findViewById(R.id.listComments);
        commentAdapter = new CommentAdapter(this,R.layout.list_view_comment,listComments);
        listViewComment.setAdapter(commentAdapter);
        //listChapter
        listChapters = new ArrayList<Chapter>();
        listViewChapter = (ListView)findViewById(R.id.listChapters);
        chapterAdapter = new ChapterAdapter(this,R.layout.list_view_chapter,listChapters);
        listViewChapter.setAdapter(chapterAdapter);
        btRead.setTag(1);


        mRemoteDatasource.getBookWithId(idBook, typeBook, new IDataSource.IGetBookDetailCallback() {
            @Override
            public void onLoaded(Book book) {
                bookD = book;
                textViewLoading.setVisibility(View.INVISIBLE);
                mLinearLayout.setVisibility(View.VISIBLE);
                Drawable drawable = LoadImageFromWebOperations(book.getThumbnail_url());
                img_title.setImageDrawable(drawable);
                title.setText(book.getTitle());
                author.setText(book.getAuthor());
                vote.setText(""+book.getVote());
                description.setText(book.getDiscription().substring(0,50));
                listComments.clear();
                listChapters.clear();
                listComments.addAll(book.getmCommentList());
                commentAdapter.notifyDataSetChanged();
                listChapters = book.getmChapterList();
                Log.d("BookDetailActivity: "," " + book.getmChapterList().size());
                chapterAdapter.notifyDataSetChanged();
                if (listChapters.size() == 0 || listChapters.size() == 1){
                    listChapter.setVisibility(View.INVISIBLE);
                    TextView tmp1 = (TextView)findViewById(R.id.tmp1);
                    tmp1.setY(700);
                    TableLayout tmp2 = (TableLayout)findViewById(R.id.tmp2);
                    tmp2.setY(750);
                    listViewComment.setY(850);
                }
               // Log.d("FirebaseInit", "comment size: " + listComments.get(0).getComment() + listComments.get(1).getComment());
                closeProgressDialog();
            }

            @Override
            public void onError(String error) {

            }
        });
        btSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description.setText(bookD.getDiscription());
                btBrief.setVisibility(View.VISIBLE);
                btSeeMore.setVisibility(View.INVISIBLE);
            }
        });
        btBrief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                description.setText(bookD.getDiscription().substring(0,50));
                btBrief.setVisibility(View.INVISIBLE);
                btSeeMore.setVisibility(View.VISIBLE);
            }
        });
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(commentText.getText().toString().equals(""))
                    Toast.makeText(BookDetailActivity.this,"Nhập comment trước khi add !", Toast.LENGTH_LONG).show();
                else {
                mRemoteDatasource.addComment(commentText.getText().toString(),idBook,typeBook);
                listComments.add(new Comment(listComments.size()+1+"","defaut",commentText.getText().toString(),"0"));
                commentAdapter.notifyDataSetChanged();}
            }
        });
        btRead.setOnClickListener(this);


        // Download new
        downloadAsync = new DownloadBookAsyncTask(getApplicationContext(),this);
        builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //clicked OK button
                downloadAsync.execute(bookD);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //cancelled the dialog
                dialog.dismiss();
            }
        });

        btdownFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser() == null){
                    Intent intent = new Intent(getApplicationContext() , LoginfirebaseActivity.class);
                    startActivity(intent);
                } else {
                    // Create the AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.setTitle("Download book");
                    dialog.setMessage("Bạn có muốn download?");
                    dialog.show();
                }
            }
        });

    }
    //sort list comment
    public static ArrayList<Comment> sortList (ArrayList<Comment>list){
        Comment tmp;
        for (int i = 0; i < list.size()-1; i++) {
            for (int j = i+1; j < list.size(); j++) {
                if(Integer.parseInt(list.get(j).getmCommentId().substring(7)) < Integer.parseInt(list.get(i).getmCommentId().substring(7))) {
                    tmp = list.get(i);
                    list.set(i,list.get(j));
                    list.set(j, tmp);
                }
            }
        }
        return list;
    }
    private Drawable LoadImageFromWebOperations(String url)
    {
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e) {
            Log.d("abc", "error: " + "Exc="+e);
            return null;
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v == btRead){
            Log.d("BookDetailActivity: ","click btRead");
            if (bookD != null) {
                Intent intent = new Intent(this,BookReaderActivity.class);
                intent.putExtra("BookReader",bookD);
                intent.putExtra("BookType", typeBook);
                intent.putExtra("IsLoadOffline",false);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onDownloadBookSuccess(Book book) {
        Log.d(TAG,"onDownloadBookSuccess: " + book.getTitle());
        mLocalDatasource.saveBook(book);
    }
}
