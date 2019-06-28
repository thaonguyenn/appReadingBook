package itnl.app.com.bookreading.view.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import itnl.app.com.bookreading.R;

import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.view.interfaces.OnBookItemClickListener;
import itnl.app.com.bookreading.view.home.BookFragment.OnListFragmentInteractionListener;

import java.util.List;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ListBookRecyclerViewAdapter extends RecyclerView.Adapter<ListBookRecyclerViewAdapter.ViewHolder> {

    private final List<BookType> mBookTypeList;
    private final OnListFragmentInteractionListener mListener;
    private final OnBookItemClickListener mBookItemClickListener;
    private Context mContext;

    public ListBookRecyclerViewAdapter(List<BookType> items, OnListFragmentInteractionListener listener, OnBookItemClickListener bookItemClickListener, Context context) {
        mBookTypeList = items;
        mListener = listener;
        mContext = context;
        mBookItemClickListener = bookItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_listbook_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mBookTypeList.get(position);
        holder.mIdView.setText(mBookTypeList.get(position).type_name);
        // Set the adapter
        if (holder.mListView instanceof RecyclerView) {
            Context context = holder.mListView.getContext();
            holder.mListView.setLayoutManager(new LinearLayoutManager(this.mContext, HORIZONTAL, false));
            // go to background to request get list book
            holder.mListView.setAdapter(new BookTypeAdapter(mBookTypeList.get(position), mBookItemClickListener, mContext));
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    Log.d("Item click","in pos: " + position);
                    showToast();
                }
            }
        });
    }
    public void showToast() {
        Toast.makeText(mContext,"show toast", Toast.LENGTH_SHORT);
    }
    @Override
    public int getItemCount() {
        return mBookTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final RecyclerView mListView;
        public BookType mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_book_kind);
            mListView = view.findViewById(R.id.list_book_kind);
        }


    }
}
