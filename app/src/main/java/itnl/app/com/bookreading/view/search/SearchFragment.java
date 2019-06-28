package itnl.app.com.bookreading.view.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.managers.IDataSource;
import itnl.app.com.bookreading.managers.remote.RemoteDataSource;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.view.base.BaseActivity;
import itnl.app.com.bookreading.view.base.BaseFragment;
import itnl.app.com.bookreading.view.book.BookReaderActivity;
import itnl.app.com.bookreading.view.interfaces.IDownloadBookItemClickListener;
import itnl.app.com.bookreading.view.library.BookDownloadedListAdapter;

import static android.support.v7.widget.RecyclerView.VERTICAL;


public class SearchFragment extends BaseFragment implements IDownloadBookItemClickListener{
    public final String TAG = "SearchFragment: ";
    private ArrayList<Book> mSearchBookList = new ArrayList<>();
    private RecyclerView mListBookView;
    private TextView mNoItemSearchView;
    private EditText searchEditText;
    SearchListAdapter mAdapter;
    AlertDialog.Builder builder;
    private final IDownloadBookItemClickListener mListener = this;


    public SearchFragment() {
        // Required empty public constructor
    }
    public static SearchFragment getInstance(){
        return new SearchFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mListBookView = view.findViewById(R.id.search_recycler_view);
        mNoItemSearchView = view.findViewById(R.id.no_search_data);
        searchEditText = view.findViewById(R.id.text_feild_search);

        if (mSearchBookList.size() == 0){
            mListBookView.setVisibility(View.INVISIBLE);
            mNoItemSearchView.setVisibility(View.VISIBLE);
        }
        if (mListBookView instanceof RecyclerView) {
            Context context = mListBookView.getContext();
            mListBookView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            mListBookView.setLayoutManager(new LinearLayoutManager(context));
            mListBookView.setLayoutManager(new LinearLayoutManager(getContext(), VERTICAL, false));
            //Set adapter for recycler view
            mAdapter = new SearchListAdapter(getContext(),mSearchBookList,mListener);
            mListBookView.setAdapter(mAdapter);
        }

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG,"beforeTextChanged" + s);
                // not to do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG,"onTextChanged" + s);

                    mRemoteDatasource.searchWithKey(s.toString(), new IDataSource.IGetListBookCallback() {
                        @Override
                        public void onLoaded(ArrayList<Book> books) {
                            Log.d(TAG, "search onLoaded: " + books.size());
                            if (books.size() == 0) {
                                mListBookView.setVisibility(View.INVISIBLE);
                                mNoItemSearchView.setVisibility(View.VISIBLE);
                            } else {
                                mAdapter.setSearchList(books);
                                mAdapter.notifyDataSetChanged();
                                mListBookView.setVisibility(View.VISIBLE);
                                mNoItemSearchView.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onFailed(String error) {

                        }
                    });

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG,"afterTextChanged" + s);
            }
        });



        return view;
    }

    @Override
    public void onBookItemClicked(Book book, int position) {
        Log.d(TAG,"onBookItemClicked: " + position);
        Intent intent = new Intent(getContext(),BookReaderActivity.class);
        intent.putExtra("BookReader",book);
        intent.putExtra("BookType", book.getmBookTypeId());
        intent.putExtra("IsLoadOffline",false);
        startActivity(intent);
    }

    @Override
    public void onBookItemLongPress(Book book, int position) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
