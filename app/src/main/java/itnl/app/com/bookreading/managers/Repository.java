package itnl.app.com.bookreading.managers;

import java.util.ArrayList;

import itnl.app.com.bookreading.managers.local.LocalDataSource;
import itnl.app.com.bookreading.managers.remote.RemoteDataSource;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.models.Chapter;
import itnl.app.com.bookreading.models.Comment;

/**
 * Create by nguyennv on 10/13/18
 */

public class Repository implements IDataSource {
    RemoteDataSource mRemoteData;
    LocalDataSource mLocalData;

    public Repository(RemoteDataSource mRemoteData, LocalDataSource mLocalData) {
        this.mRemoteData = mRemoteData;
        this.mLocalData = mLocalData;
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

    public Book getBookById (String id, String type)
    {return null;}

    @Override
    public void getBookWithId(String id, String type, IGetBookDetailCallback callback) {

    }

    @Override
    public void getListBookType(IGetBookTypeCallback callback) {

    }

    @Override
    public void getListBook(IGetListBookCallback callback) {

    }

    @Override
    public void saveBook(Book book, IDownloadBookCallback callback) {

    }

    @Override
    public void saveBook(Book book) {

    }

    @Override
    public void removeBook(String bookId) {

    }

    @Override
    public void updateRealtimeCommentList(String bookId, String bookTypeId, IUpdateCommentListCallBack callBack) {

    }

    @Override
    public void searchWithKey(String key, IGetListBookCallback callback) {

    }
}
