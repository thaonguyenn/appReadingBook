package itnl.app.com.bookreading.managers;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.models.Chapter;
import itnl.app.com.bookreading.models.Comment;

/**
 * Create by nguyennv on 10/13/18
 */
public interface IDataSource {
    // TODO: Func to implement in Local and remote datasource
    // Get all book from FirebaseDB
    public ArrayList<Book> getAllBooks();

    // Get list chapter of book with bookId
    public ArrayList<Chapter> getListChapter(String bookId);

    // Get list comment of book with bookId
    public ArrayList<Comment> getListComment(String bookId);

    //set Vote for book to FirebaseDB
    public void vote(String bookId);

    // get list favourites book of user with userID
    public ArrayList<Book> getFavouritesBook(String userId);


    //get book by id
    public Book getBookById (String id, String type);
    public void getBookWithId (String id, String type, IGetBookDetailCallback callback);
    void getListBookType(IGetBookTypeCallback callback);
    void getListBook(IGetListBookCallback callback);
    void saveBook(Book book, IDownloadBookCallback callback);
    void saveBook(Book book);
    void removeBook(String bookId);
    void updateRealtimeCommentList(String bookId, String bookTypeId, IDataSource.IUpdateCommentListCallBack callBack);
    void searchWithKey(String key, IGetListBookCallback callback);


    interface IGetBookDetailCallback {
        public void onLoaded (Book book);

        public void onError(String error);


    }
    interface  IGetBookTypeCallback {
        void onLoaded(ArrayList<BookType> bookTypes);
        void onCancel(DatabaseError databaseError);
    }
    interface  IGetListBookCallback {
        void onLoaded(ArrayList<Book> books);
        void onFailed(String error);
    }
    interface IDownloadBookCallback {
        void onSuccess(String data);
        void onFailed(String error);
    }
    interface IUpdateCommentListCallBack {
        void onLoaded(ArrayList<Comment> comments);
        void onFailed(String error);
    }
}

