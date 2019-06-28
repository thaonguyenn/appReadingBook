package itnl.app.com.bookreading.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import itnl.app.com.bookreading.managers.IDataSource;
import itnl.app.com.bookreading.models.Book;
import itnl.app.com.bookreading.models.BookType;
import itnl.app.com.bookreading.models.Chapter;
import itnl.app.com.bookreading.models.Comment;

/**
 * Create by nguyennv on 10/12/18
 */
public class FirebaseServices {

    private static String TAG = "FirebaseInit";
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static FirebaseServices firebaseServices;
    public int tmp = 0;

    public String getFirebaseTag() {
        String a = FirebaseServices.mDatabase.getReference().getKey();
        return a;
    }

    public void addComment(final String comment, final String idBook, final String type) {
        final DatabaseReference data = FirebaseServices.mDatabase.getReference().child("book_type");
        final Comment cm = new Comment();
        cm.setmUserId("default");
        cm.setComment(comment);
        cm.setmLike("0");
        data.addListenerForSingleValueEvent(new ValueEventListener() {//listener change
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(type)) {
                        for (DataSnapshot childsnapshot : snapshot.getChildren()) {
                            if (childsnapshot.getKey().equals(idBook)) {
                                DataSnapshot bookCm = childsnapshot.child("comments");
                                data.child(type).child(idBook).child("comments").child("comment" + (bookCm.getChildrenCount() + 1)).setValue(cm);
                                Log.d(TAG, "add comment: ");
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public synchronized static FirebaseServices getInstance() {
        if (firebaseServices == null) {
            firebaseServices = new FirebaseServices();
        }
        return firebaseServices;
    }

    //sort list comment
    public static ArrayList<Comment> sortList(ArrayList<Comment> list) {
        Comment tmp;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (Integer.parseInt(list.get(j).getmCommentId().substring(7)) < Integer.parseInt(list.get(i).getmCommentId().substring(7))) {
                    tmp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, tmp);
                }
            }
        }
        return list;
    }

    public void getBookWithId(String id, String type, final IDataSource.IGetBookDetailCallback callback) {

        final String mId = id;
        final String mType = type;
        final Book book = new Book();
        DatabaseReference data = FirebaseServices.mDatabase.getReference().child("book_type");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(mType)) {
                        for (DataSnapshot childsnapshot : snapshot.getChildren()) {
                            if (childsnapshot.getKey().equals(mId)) {
                                Log.d(TAG, "snapshot onDataChange: " + snapshot.getKey() + "childsnapshot onDataChange: " + childsnapshot.getKey());
                                DataSnapshot bookCm = childsnapshot.child("comments");
                                DataSnapshot bookCt = childsnapshot.child("url");
                                book.setTitle(childsnapshot.getValue(Book.class).getTitle());
                                book.setmId(childsnapshot.getKey());
                                book.setAuthor(childsnapshot.getValue(Book.class).getAuthor());
                                book.setDiscription(childsnapshot.getValue(Book.class).getDiscription());
                                book.setPublish_year(childsnapshot.getValue(Book.class).getPublish_year());
                                book.setVote(childsnapshot.getValue(Book.class).getVote());
                                tmp = childsnapshot.getValue(Book.class).getVote();
                                book.setThumbnail_url(childsnapshot.getValue(Book.class).getThumbnail_url());
                                Log.d(TAG, "thumbnail: " + book.getThumbnail_url());
                                ArrayList<Comment> comments = new ArrayList<Comment>();
                                Comment comment;
                                if (bookCm.getChildrenCount() > 0) {
                                    for (DataSnapshot childs : bookCm.getChildren()) {
                                        if (childs.getChildrenCount() > 0) {
                                            comment = new Comment();
                                            comment.setmCommentId(childs.getKey());
                                            Log.d("iddddd", "" + comment.getmCommentId());
                                            comment.setComment(childs.child("comment").getValue(String.class));
                                            Log.d("contexttttt", "" + comment.getComment());
                                            comment.setmUserId(childs.child("mUserId").getValue(String.class));
                                            Log.d("userrrrr", "" + comment.getmUserId());
                                            comments.add(comment);
//                                            comment.setMLike(childs.child("mLike").getValue(String.class));
//                                            Log.d("likeeeee", "" + comment.getMLike());
//                                            for (DataSnapshot child : childs.getChildren()) {
////
////                                                comment.setComment(child.getValue(String.class));
////
////                                                comment.setMUserId(child.getValue(Comment.class).getMUserId());
////
////                                                comment.setMLike(""+child.getValue(Integer.class));
////
////                                                comments.add(comment);
////                                            }
                                        }
                                    }
                                }
                                ;
                                book.setmCommentList(sortList(comments));
                                ArrayList<Chapter> chapters = new ArrayList<Chapter>();
                                Chapter chapter;
                                Log.d("FirebaseServices: ", " current chapter child - " + bookCt.getKey() + " - " + bookCt.getChildrenCount());
                                if (bookCt.getChildrenCount() > 0) {
                                    for (DataSnapshot childs : bookCt.getChildren()) {
                                        chapter = new Chapter();
                                        chapter.setId(childs.getKey());
                                        Log.d("FirebaseServices: ", "getBookWithId -- chapterID: " + chapter.getId());
                                        chapter.setmChapterUrl(childs.getValue(String.class));
                                        Log.d("FirebaseServices: ", "getBookWithId -- getmChapterUrl: " + childs.getValue(String.class));
//                                            chapter.setmChapterUrlImage(childs.child("link").getValue(String.class));
//                                            Log.d("link", "" + chapter.getmChapterUrlImage());
                                        chapters.add(chapter);
                                    }
                                }
                                book.setmChapterList(chapters);
                                Log.d(TAG, "book onDataChange: " + book.getmCommentList().size());
                                callback.onLoaded(book);
                                break;
                            }
                        }
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Book getBookById(String id) {
        final String mId = id;
        final Book book = new Book();
        DatabaseReference data = FirebaseServices.mDatabase.getReference().child("book_type");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "snapshot onDataChange: " + snapshot.getKey());
                    for (DataSnapshot childsnapshot : snapshot.getChildren()) {
                        Log.d(TAG, "childsnapshot onDataChange: " + childsnapshot.getKey());
                        if (childsnapshot.getKey().equals(mId)) {
//                            for(D)
                            DataSnapshot bookSn = childsnapshot.child(childsnapshot.getKey());
                            Log.d("onDataChange TAGGGGG", "" + childsnapshot.getValue(Book.class).getTitle());
                            book.setTitle(childsnapshot.getValue(Book.class).getTitle());
                            book.setmId(childsnapshot.getKey());
                            book.setAuthor(childsnapshot.getValue(Book.class).getAuthor());
                            book.setDiscription(childsnapshot.getValue(Book.class).getDiscription());
                            book.setPublish_year(childsnapshot.getValue(Book.class).getPublish_year());
                            book.setVote(childsnapshot.getValue(Book.class).getVote());
                            book.setThumbnail_url(childsnapshot.getValue(Book.class).getThumbnail_url());
                            Log.d(TAG, "book onDataChange: " + book.toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return book;
    }

    // Get all book type and sub list book
    public void getListBookKind(final IDataSource.IGetBookTypeCallback callback) {
        final ArrayList<BookType> listBooksType = new ArrayList<>();
        DatabaseReference data = FirebaseServices.mDatabase.getReference().child("book_type");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "snapshot onDataChange: " + snapshot.getKey());
                    BookType bookType = new BookType();
                    ArrayList<Book> listBook = new ArrayList<>();
                    for (DataSnapshot childsnapshot : snapshot.getChildren()) {
                        if (childsnapshot.getKey().equals("type_name")) {
                            bookType.setmId(snapshot.getKey());
                            bookType.setType_name(snapshot.getValue(BookType.class).getType_name());
                        } else {
                            Log.d(TAG, "childsnapshot onDataChange: " + childsnapshot.getKey());
                            Book book = new Book();
                            DataSnapshot bookSn = childsnapshot.child(childsnapshot.getKey());
                            Log.d("onDataChange TAGGGGG", "" + childsnapshot.getValue(Book.class).getTitle());
                            book.setTitle(childsnapshot.getValue(Book.class).getTitle());
                            book.setmId(childsnapshot.getKey());
                            book.setThumbnail_url(childsnapshot.getValue(Book.class).getThumbnail_url());
                            Log.d(TAG, "book onDataChange: " + book.toString());
                            listBook.add(book);
                        }
                    }
                    // Add list book to book type
                    bookType.setmListBook(listBook);
                    // Add to list book type
                    listBooksType.add(bookType);
                }
                callback.onLoaded(listBooksType);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onCancel(databaseError);
            }
        });
    }

    public void updateRealtimeCommentList(String bookId, String bookTypeId, final IDataSource.IUpdateCommentListCallBack callBack) {

        DatabaseReference data = FirebaseServices.mDatabase.getReference().child("book_type");
        DatabaseReference bookTypeRef = data.child(bookTypeId);
        DatabaseReference bookRef = bookTypeRef.child(bookId);
        bookRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Comment> comments = new ArrayList<>();
                Log.d("FirebaseServices", "updateRealtimeCommentList onDataChange: " + dataSnapshot.getKey());
                DataSnapshot bookCm = dataSnapshot.child("comments");
                Comment comment;
                if (bookCm.getChildrenCount() > 0) {
                    Log.d("FirebaseServices", "bookCm.getChildrenCount() onDataChange: " + bookCm.getChildrenCount());
                    for (DataSnapshot childs : bookCm.getChildren()) {
                        if (childs.getChildrenCount() > 0) {
                            comment = new Comment();
                            comment.setmCommentId(childs.getKey());
                            Log.d("FirebaseServices", "bookCm: " + comment.getmCommentId());
                            comment.setComment(childs.child("comment").getValue(String.class));
                            Log.d("FirebaseServices", "bookCm: " + comment.getComment());
                            comment.setmUserId(childs.child("mUserId").getValue(String.class));
                            Log.d("FirebaseServices", "bookCm: " + comment.getmUserId());
                            comments.add(comment);
                        }
                    }
                }

                callBack.onLoaded(comments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onFailed("Load list comments fail");
            }
        });
    }

    public int vote(final String bookId, final String bookTypeId) {
        final DatabaseReference data = FirebaseServices.mDatabase.getReference().child("book_type");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(bookTypeId)) {
                        for (DataSnapshot childsnapshot : snapshot.getChildren()) {
                            if (childsnapshot.getKey().equals(bookId)) {
                                data.child(bookTypeId).child(bookId).child("vote").setValue(childsnapshot.getValue(Book.class).getVote() + 1);
                                tmp = childsnapshot.getValue(Book.class).getVote() + 1;
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return tmp;
    }

    public List<Book> search(final String condition) {
        final List<Book> list = new ArrayList<Book>();
        final DatabaseReference data = FirebaseServices.mDatabase.getReference().child("book_type");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childsnapshot : snapshot.getChildren()) {
                        if (childsnapshot.getValue(Book.class).getTitle().contains(condition)
                                || childsnapshot.getValue(Book.class).getAuthor().contains(condition)
                                || childsnapshot.getValue(Book.class).getDiscription().contains(condition)) {
                            Book book = new Book();
                            DataSnapshot bookCm = childsnapshot.child("comments");
                            DataSnapshot bookCt = childsnapshot.child("url");
                            book.setTitle(childsnapshot.getValue(Book.class).getTitle());
                            book.setmId(childsnapshot.getKey());
                            book.setAuthor(childsnapshot.getValue(Book.class).getAuthor());
                            book.setDiscription(childsnapshot.getValue(Book.class).getDiscription());
                            book.setPublish_year(childsnapshot.getValue(Book.class).getPublish_year());
                            book.setVote(childsnapshot.getValue(Book.class).getVote());
                            tmp = childsnapshot.getValue(Book.class).getVote();
                            book.setThumbnail_url(childsnapshot.getValue(Book.class).getThumbnail_url());
                            ArrayList<Comment> comments = new ArrayList<Comment>();
                            Comment comment;
                            if (bookCm.getChildrenCount() > 0) {
                                for (DataSnapshot childs : bookCm.getChildren()) {
                                    if (childs.getChildrenCount() > 0) {
                                        comment = new Comment();
                                        comment.setmCommentId(childs.getKey());
                                        comment.setComment(childs.child("comment").getValue(String.class));
                                        comment.setmUserId(childs.child("mUserId").getValue(String.class));
                                        comments.add(comment);
                                    }
                                }
                            }
                            ;
                            book.setmCommentList(sortList(comments));
                            ArrayList<Chapter> chapters = new ArrayList<Chapter>();
                            Chapter chapter;
                            if (bookCt.getChildrenCount() > 0) {
                                for (DataSnapshot childs : bookCt.getChildren()) {
                                    chapter = new Chapter();
                                    chapter.setId(childs.getKey());
                                    chapter.setmChapterUrl(childs.getValue(String.class));
                                    chapters.add(chapter);
                                }
                            }
                            book.setmChapterList(chapters);
                            list.add(book);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return list;
    }

    public void searchWithKey(final String key, final IDataSource.IGetListBookCallback callback) {
        final ArrayList<Book> list = new ArrayList<Book>();
        final String keySearch = key.toLowerCase();
        final DatabaseReference data = FirebaseServices.mDatabase.getReference().child("book_type");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("FirebaseServices: ", "search snapshot: " + snapshot.getKey());
                    for (DataSnapshot childsnapshot : snapshot.getChildren()) {
                        if (!childsnapshot.getKey().equals("type_name")) {
                            Log.d("FirebaseServices: ", "search bookChildSn: " + childsnapshot.getKey());
                            if (!key.equals("") && (childsnapshot.getValue(Book.class).getTitle().toLowerCase().contains(keySearch)
                                    || childsnapshot.getValue(Book.class).getAuthor().toLowerCase().contains(keySearch))) {
                                Book book = new Book();
                                book.setmBookTypeId(snapshot.getKey());
                                DataSnapshot bookCm = childsnapshot.child("comments");
                                DataSnapshot bookCt = childsnapshot.child("url");
                                book.setTitle(childsnapshot.getValue(Book.class).getTitle());
                                book.setmId(childsnapshot.getKey());
                                book.setAuthor(childsnapshot.getValue(Book.class).getAuthor());
                                book.setDiscription(childsnapshot.getValue(Book.class).getDiscription());
                                book.setPublish_year(childsnapshot.getValue(Book.class).getPublish_year());
                                book.setVote(childsnapshot.getValue(Book.class).getVote());
                                tmp = childsnapshot.getValue(Book.class).getVote();
                                book.setThumbnail_url(childsnapshot.getValue(Book.class).getThumbnail_url());
                                ArrayList<Comment> comments = new ArrayList<Comment>();
                                Comment comment;
                                if (bookCm.getChildrenCount() > 0) {
                                    for (DataSnapshot childs : bookCm.getChildren()) {
                                        if (childs.getChildrenCount() > 0) {
                                            comment = new Comment();
                                            comment.setmCommentId(childs.getKey());
                                            comment.setComment(childs.child("comment").getValue(String.class));
                                            comment.setmUserId(childs.child("mUserId").getValue(String.class));
                                            comments.add(comment);
                                        }
                                    }
                                }
                                ;
                                book.setmCommentList(sortList(comments));
                                ArrayList<Chapter> chapters = new ArrayList<Chapter>();
                                Chapter chapter;
                                if (bookCt.getChildrenCount() > 0) {
                                    for (DataSnapshot childs : bookCt.getChildren()) {
                                        chapter = new Chapter();
                                        chapter.setId(childs.getKey());
                                        chapter.setmChapterUrl(childs.getValue(String.class));
                                        chapters.add(chapter);
                                    }
                                }
                                book.setmChapterList(chapters);
                                list.add(book);
                            }
                        }
                    }
                }
                callback.onLoaded(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailed(databaseError.getMessage());
            }
        });
    }
}
