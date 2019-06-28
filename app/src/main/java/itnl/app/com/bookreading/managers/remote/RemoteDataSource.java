package itnl.app.com.bookreading.managers.remote;

import java.util.ArrayList;
import java.util.List;

import itnl.app.com.bookreading.managers.IDataSource;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.models.Chapter;
import itnl.app.com.bookreading.models.Comment;
import itnl.app.com.bookreading.services.FirebaseServices;

/**
 * Create by nguyennv on 10/13/18
 */
public class RemoteDataSource implements IDataSource {
    FirebaseServices mFirebaseService;
    private static RemoteDataSource remoteDataSource;

    public RemoteDataSource(FirebaseServices mFirebaseService) {
        this.mFirebaseService = mFirebaseService;
    }
    public synchronized static RemoteDataSource getInstance(){
        if (remoteDataSource == null) {
            return remoteDataSource = new RemoteDataSource(FirebaseServices.getInstance());
        } else return remoteDataSource;
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
    public Book getBookById (String id, String type){
       return mFirebaseService.getBookById(id);
    }

    @Override
    public void getBookWithId(String id, String type, IGetBookDetailCallback callback) {
        mFirebaseService.getBookWithId(id, type,callback);
}

    public ArrayList<BookType> getListBookType() {
        return null;
    }
    @Override
    public void getListBookType(IGetBookTypeCallback callback) {
        mFirebaseService.getListBookKind(callback);
    }
    public void addComment(String comment, String idBook, String type){
        mFirebaseService.addComment(comment,idBook,type);
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
        mFirebaseService.updateRealtimeCommentList(bookId,bookTypeId,callBack);
    }
    public int vote (String bookId, String bookTypeId){
        return mFirebaseService.vote(bookId,bookTypeId);
    }
    public List<Book> search(String condition){
        return mFirebaseService.search(condition);
    }

    @Override
    public void searchWithKey(String key, IGetListBookCallback callback) {
        mFirebaseService.searchWithKey(key,callback);
    }
}
