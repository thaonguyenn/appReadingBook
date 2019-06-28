package itnl.app.com.bookreading.view.book;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.models.Comment;

public class CommentAdapter extends ArrayAdapter<Comment> {
    Activity context = null;
    int idLayout;
    static ArrayList<Comment> listComments = null;

    public CommentAdapter(Activity context, int idLayout, ArrayList<Comment> listComments) {
        super(context, idLayout, listComments);
        this.context = context;
        this.idLayout = idLayout;
        this.listComments = listComments;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(idLayout,null);
        //show context
        if(listComments.size()>0 && position>=0)
        {
            final TextView comment=(TextView)
                    convertView.findViewById(R.id.comment);
            final Comment cm=listComments.get(position);
            comment.setText(cm.getComment());
            final ImageView img=(ImageView)
                    convertView.findViewById(R.id.avatar);
                img.setImageResource(R.drawable.avatar2);
        }
        return convertView;
    }

}


