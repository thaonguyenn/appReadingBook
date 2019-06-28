package itnl.app.com.bookreading.view.book;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import itnl.app.com.bookreading.R;
import itnl.app.com.bookreading.models.Chapter;
import itnl.app.com.bookreading.models.Comment;

public class ChapterAdapter extends ArrayAdapter<Chapter> {
    Activity context = null;
    int idLayout;
    static ArrayList<Chapter> listChapters = null;

    public ChapterAdapter(Activity context, int idLayout, ArrayList<Chapter> listChapters) {
        super(context, idLayout, listChapters);
        this.context = context;
        this.idLayout = idLayout;
        this.listChapters = listChapters;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(idLayout, null);
        //show context
        if (listChapters.size() > 0 && position >= 0) {
            final TextView id = (TextView)
                    convertView.findViewById(R.id.idChapter);
            final Chapter ct = listChapters.get(position);
            id.setText(ct.getId());
            final TextView link = (TextView)
                    convertView.findViewById(R.id.linkChapter);
            link.setText(ct.getmChapterUrl());
            final Button bt = (Button)
                    convertView.findViewById(R.id.download);
            bt.setBackgroundResource(R.drawable.down);
        }
        return convertView;
    }
}
