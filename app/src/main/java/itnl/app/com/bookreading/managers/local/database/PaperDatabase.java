package itnl.app.com.bookreading.managers.local.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import itnl.app.com.bookreading.managers.IDataSource;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.utils.Constants;

/**
 * Create by nguyennv on 1/6/19
 */
public class PaperDatabase {

    public ArrayList<Book> getListBookDownload(){
        ArrayList<Book> listBook = Paper.book().read(Constants.PAPER_LIST_BOOK);
        return (listBook != null) ? listBook : new ArrayList<Book>();
    }
    public void saveBook(Book book){
        ArrayList<Book> listBook = getListBookDownload();
        listBook.add(book);
        Paper.book().write(Constants.PAPER_LIST_BOOK,listBook);
    }
    public void getListBook(IDataSource.IGetListBookCallback callback){
        Log.d("PaperDatabase: ","getListBook");
        ArrayList<Book> listBook = getListBookDownload();
        if(listBook == null) {
            callback.onFailed("Data is null");
        }
        callback.onLoaded(listBook);
    }
    public void saveBook(Book book, IDataSource.IDownloadBookCallback callback){
        saveBook(book);
        ArrayList<Book> listBook = getListBookDownload();
        if (listBook.contains(book)) {
            callback.onSuccess("Save success");
        } else {
            callback.onFailed("Save fail");
        }
    }
    public void removeBook(String bookId){
        ArrayList<Book> listBook = getListBookDownload();
        if(listBook.size() != 0) {
            for (Book book : listBook) {
                if (bookId.equals(book.getmId())) {
                    listBook.remove(book);
                }
            }
            Paper.book().write(Constants.PAPER_LIST_BOOK,listBook);
        }
        Log.d("PaperDatabase: ","Remove book success");
    }
}
