package itnl.app.com.bookreading.models;

import java.util.ArrayList;

/**
 * Create by nguyennv on 12/3/18
 */
public class BookType extends Models{
    public String mId;
    public String type_name;
    public ArrayList<Book> mListBook;

    public BookType(){

    }
    public BookType(String mId, String type_name, ArrayList<Book> mListBook) {
        this.mId = mId;
        this.type_name = type_name;
        this.mListBook = mListBook;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public ArrayList<Book> getmListBook() {
        return mListBook;
    }

    public void setmListBook(ArrayList<Book> mListBook) {
        this.mListBook = mListBook;
    }
}
