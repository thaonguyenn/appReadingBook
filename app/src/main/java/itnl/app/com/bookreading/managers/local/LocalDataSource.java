package itnl.app.com.bookreading.managers.local;

import java.util.ArrayList;

import itnl.app.com.bookreading.managers.IDataSource;
import itnl.app.com.bookreading.managers.local.database.PaperDatabase;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.models.Chapter;
import itnl.app.com.bookreading.models.Comment;

/**
 * Create by nguyennv on 10/13/18
 */
public class LocalDataSource implements IDataSource {

    public static final LocalDataSource mLocalDatasource = new LocalDataSource();
    private PaperDatabase mPaperDatabase;

    public LocalDataSource(){
        mPaperDatabase = new PaperDatabase();
    }

    public static LocalDataSource getInstance(){
        return mLocalDatasource;
    }

    @Override
    public ArrayList<Book> getAllBooks() {
        return null;
    }

    @Override
    public ArrayList<Chapter> getListChapter(String bookId) {
        return null;
    }

    @Override
    public ArrayList<Comment> getListComment(String bookId) {
        return null;
    }

    @Override
    public void vote(String bookId) {

    }

    @Override
    public ArrayList<Book> getFavouritesBook(String userId) {
        return null;
    }

    @Override

    public Book getBookById(String id, String type) {
        return null;
    }

    @Override
    public void getBookWithId(String id, String type, IGetBookDetailCallback callback) {

    }

    public ArrayList<BookType> getListBookType() {
        return null;
    }

    @Override
    public void getListBookType(IGetBookTypeCallback callback) {

    }

    @Override
    public void getListBook(IGetListBookCallback callback) {
        this.mPaperDatabase.getListBook(callback);
    }

    @Override
    public void saveBook(Book book, IDownloadBookCallback callback) {
        this.mPaperDatabase.saveBook(book, callback);
    }

    @Override
    public void saveBook(Book book) {
        this.mPaperDatabase.saveBook(book);
    }

    @Override
    public void removeBook(String bookId) {
        this.mPaperDatabase.removeBook(bookId);
    }

    @Override
    public void updateRealtimeCommentList(String bookId, String bookTypeId, IUpdateCommentListCallBack callBack) {

    }

    @Override
    public void searchWithKey(String key, IGetListBookCallback callback) {

    }
}
