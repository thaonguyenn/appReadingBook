package itnl.app.com.bookreading.view.interfaces;

import itnl.app.com.bookreading.models.Book;

/**
 * Create by nguyennv on 1/8/19
 */
public interface IDownloadBookItemClickListener {
    void onBookItemClicked(Book book, int position);
    void onBookItemLongPress(Book book, int position);
}
