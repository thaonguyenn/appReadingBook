package itnl.app.com.bookreading.view.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.view.interfaces.OnBookItemClickListener;

/**
 * Create by nguyennv on 12/3/18
 */
public class BookTypeAdapter extends RecyclerView.Adapter<BookTypeAdapter.ViewHolder> {
    public final List<Book> mListBook;
    public Context mContext;
    private OnBookItemClickListener mItemClickListener;
    private BookType mBookType;

    public BookTypeAdapter(BookType bookType, OnBookItemClickListener itemClickListener, Context context) {
        this.mListBook = bookType.mListBook;
        this.mContext = context;
        this.mItemClickListener = itemClickListener;
        mBookType = bookType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_type_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.mBookItem = this.mListBook.get(position);
        Log.d("getThumbnail_url()",mListBook.get(position).getThumbnail_url());
        if (mListBook.get(position).getThumbnail_url() == null || mListBook.get(position).getThumbnail_url().equals("")){
            holder.mBookImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_img_100x200));
        } else {
            Log.d("getThumbnail_url()",mListBook.get(position).getThumbnail_url());
            Picasso.with(mContext).load(mListBook.get(position).getThumbnail_url())
                .error(R.drawable.default_img_100x200)
                .placeholder(R.drawable.default_img_100x200)
                .into(holder.mBookImage);
        }
        holder.mBookNameTextView.setText(mListBook.get(position).getTitle());
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Book item clicked: ","Pos: " + position + " Book: " + mListBook.get(position).getTitle());
                mItemClickListener.onBookItemClicked(mListBook.get(position), mBookType.mId ,position);
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
        return mListBook.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Book mBookItem;
        public final ImageView mBookImage;
        public final TextView mBookNameTextView;
        public final View mContentView;

        public ViewHolder(View view){
            super(view);
            this.mContentView = view;
            this.mBookImage = view.findViewById(R.id.book_image_id);
            this.mBookNameTextView = view.findViewById(R.id.book_name_id);
        }

    }
}