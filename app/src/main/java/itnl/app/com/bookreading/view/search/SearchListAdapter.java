package itnl.app.com.bookreading.view.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.ArrayList;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.view.home.ListBookRecyclerViewAdapter;
import itnl.app.com.bookreading.view.interfaces.IDownloadBookItemClickListener;

/**
 * Create by nguyennv on 1/9/19
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder>{

    public final String TAG = "BookDownloadedListAdapter: ";
    private Context mContext;
    private ArrayList<Book> searchList;
    private IDownloadBookItemClickListener listener;

    public SearchListAdapter(Context mContext, ArrayList<Book> listDownloadedBook,IDownloadBookItemClickListener listener) {
        Log.d(TAG, "Array size: " + listDownloadedBook.size());
        this.mContext = mContext;
        this.searchList = listDownloadedBook;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.library_list_item, parent, false);
        return new ViewHolder(view);
    }
    public void setSearchList(ArrayList<Book> searchList){
        this.searchList = searchList;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder");
        holder.bookItem = searchList.get(position);
        Picasso.with(mContext)
                .load(holder.bookItem.getThumbnail_url())
                .error(R.drawable.default_img_100x200)
                .placeholder(R.drawable.default_img_100x200)
                .into(holder.bookImageView);
        holder.bookName.setText(holder.bookItem.getTitle());
        holder.author.setText(holder.bookItem.getAuthor());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBookItemClicked(holder.bookItem,position);
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView bookImageView;
        public final TextView bookName, author;
        public Book bookItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            bookImageView = view.findViewById(R.id.book_download_image);
            bookName = view.findViewById(R.id.book_download_name);
            author = view.findViewById(R.id.author_feild);
        }


    }
}
