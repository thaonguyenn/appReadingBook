package itnl.app.com.bookreading.view.book.comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.models.Comment;

/**
 * Create by nguyennv on 1/7/19
 */
public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewHolder> {
    private ArrayList<Comment> listComment;
    private Context mContext;

    public CommentsListAdapter(Context context, ArrayList<Comment> listComment){
        this.listComment = listComment;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);
        return new ViewHolder(view);
    }
    public void updateCommentsList(ArrayList<Comment> newCommentsList){
        this.listComment = newCommentsList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mItem = listComment.get(position);
        holder.userId.setText(holder.mItem.mUserId);
        holder.commentContent.setText(holder.mItem.comment);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CommentsListAdapter: ", "onClick in comment item " + position);
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
        return listComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public Comment mItem;
        public ImageView imgAvatar;
        public TextView userId, commentContent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imgAvatar = view.findViewById(R.id.user_avatar);
            userId = view.findViewById(R.id.user_id);
            commentContent = view.findViewById(R.id.comment_content);


        }


    }
}
