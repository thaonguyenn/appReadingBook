package itnl.app.com.bookreading.view.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.managers.IDataSource;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.utils.AppLogger;
import itnl.app.com.bookreading.view.book.BookDetailActivity;
import itnl.app.com.bookreading.view.book.BookReaderActivity;
import itnl.app.com.bookreading.view.interfaces.OnBookItemClickListener;
import itnl.app.com.bookreading.view.base.BaseFragment;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BookFragment extends BaseFragment implements OnBookItemClickListener {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private OnBookItemClickListener onBookItemClickListener;

    public static BookFragment getInstance(){
        return new BookFragment();
    }
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BookFragment() {
        onBookItemClickListener = this;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BookFragment newInstance(int columnCount) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppLogger.d("debug onCreateView","");
        showProgressDialog();
        View view = inflater.inflate(R.layout.fragment_listbook_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            final Context context = view.getContext();
            final RecyclerView recyclerView = (RecyclerView) view;
            // Add divider for recycler view
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            // Todo to get list from firebase service
            this.mRemoteDatasource.getListBookType(new IDataSource.IGetBookTypeCallback() {
                @Override
                public void onLoaded(ArrayList<BookType> bookTypes) {
                    closeProgressDialog();
                    Log.d("TAG","list count = "+ bookTypes.size());
                    recyclerView.setAdapter(new ListBookRecyclerViewAdapter(bookTypes, mListener, onBookItemClickListener,getContext()));
                }

                @Override
                public void onCancel(DatabaseError databaseError) {

                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onBookItemClicked(Book bookItem, String bookTypeId, int position) {
        Log.d("CLICK LISTENNER","onBookItemClicked: " + bookItem.getmId() + " - " + bookTypeId + " in position: " + position);
//        Toast.makeText(getContext(),"Will move to book details activity",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getContext(),BookDetailActivity.class);
        intent.putExtra("idBook",bookItem.getmId());
        intent.putExtra("typeBook",bookTypeId);
        getActivity().startActivity(intent);
    }

//    @Override
//    public void onBookItemClicked(Book book, int position) {
//        Log.d("LISTENNER","onBookItemClicked at position: " + position);
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(BookType item);
    }
    public interface OnBookTypeItemClickListener {
        void onBookTypeItemClicked(Book book, int position);
    }
}
