package itnl.app.com.bookreading.view.library;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.managers.IDataSource;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.view.base.BaseFragment;
import itnl.app.com.bookreading.view.book.BookReaderActivity;
import itnl.app.com.bookreading.view.home.BookFragment;
import itnl.app.com.bookreading.view.interfaces.IDownloadBookItemClickListener;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;
import static android.support.v7.widget.RecyclerView.VERTICAL;

/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment extends BaseFragment implements IDownloadBookItemClickListener{

    public final String TAG = "LibraryFragment: ";
    private ArrayList<Book> mDownloadedBookList;
    private RecyclerView mListBookView;
    private TextView mNoItemDownloadView;
    private final IDownloadBookItemClickListener mListener = this;
    BookDownloadedListAdapter mAdapter;
    AlertDialog.Builder builder;

    public LibraryFragment() {
        // Required empty public constructor
    }
    public static LibraryFragment getInstance(){
        return new LibraryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        mListBookView = view.findViewById(R.id.list_book_download);
        mNoItemDownloadView = view.findViewById(R.id.no_download_data);
        mLocalDatasource.getListBook(new IDataSource.IGetListBookCallback() {
            @Override
            public void onLoaded(ArrayList<Book> books) {
                Log.d(TAG,"onLoaded with listbook: " + books.size());
                mDownloadedBookList = new ArrayList<>(books);
                if (mDownloadedBookList == null || mDownloadedBookList.size() == 0){
                    mListBookView.setVisibility(View.INVISIBLE);
                    mNoItemDownloadView.setVisibility(View.VISIBLE);
                    mNoItemDownloadView.setText("Không có sách được tải về");
                } else {
                    mListBookView.setVisibility(View.VISIBLE);
                    mNoItemDownloadView.setVisibility(View.INVISIBLE);
                    if (mListBookView instanceof RecyclerView) {
                        Context context = mListBookView.getContext();
                        mListBookView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                        mListBookView.setLayoutManager(new LinearLayoutManager(context));
                        mListBookView.setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));
                        //Set adapter for recycler view
                        mAdapter = new BookDownloadedListAdapter(getContext(),mDownloadedBookList,mListener);
                        mListBookView.setAdapter(mAdapter);
                    }

                }

            }

            @Override
            public void onFailed(String error) {
                Log.d(TAG,"onFailed with error " + error);
                mListBookView.setVisibility(View.GONE);
                mNoItemDownloadView.setVisibility(View.VISIBLE);
            }
        });

        builder = new AlertDialog.Builder(getContext(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);


        return view;
    }

    @Override
    public void onBookItemClicked(Book book, int position) {
        Log.d(TAG,"onBookItemClicked: " + position);
        // move to bookreader with offline mode
        Log.d("BookDetailActivity: ","click btRead");
            Intent intent = new Intent(getContext(),BookReaderActivity.class);
            intent.putExtra("BookReader",book);
            intent.putExtra("IsLoadOffline",true);
            startActivity(intent);
    }

    @Override
    public void onBookItemLongPress(final Book book, final int position) {
        Log.d(TAG,"onBookItemLongPress: " + position);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //clicked OK button
                deleteBook(book, position);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //cancelled the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Warning");
        dialog.setMessage("Bạn có muốn xoá cuốn sách này? /n" + book.getTitle());
        dialog.show();
    }
    private void deleteBook(Book book, int position){
        // To delete this book and reload data
        mLocalDatasource.removeBook(book.getmId());
        mDownloadedBookList.remove(book);
        mAdapter.notifyDataSetChanged();
        if (mDownloadedBookList.size() == 0){
            mListBookView.setVisibility(View.INVISIBLE);
            mNoItemDownloadView.setVisibility(View.VISIBLE);
            mNoItemDownloadView.setText("Không có sách được tải về");
        }
    }
}
