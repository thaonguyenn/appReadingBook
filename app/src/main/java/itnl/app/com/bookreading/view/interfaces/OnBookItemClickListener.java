package itnl.app.com.bookreading.view.interfaces;

import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.BookType;

/**
 * Create by nguyennv on 12/18/18
 */
public interface OnBookItemClickListener {
    void onBookItemClicked(Book bookItem, String bookTypeId, int position);
}
