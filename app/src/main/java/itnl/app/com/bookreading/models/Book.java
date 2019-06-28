package itnl.app.com.bookreading.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Create by nguyennv on 11/10/18
 */
public class Book extends Models implements Serializable {
    public String mId;
    public String mBookTypeId;
    public String title;
    public String author;
    public String discription;
    public int vote;
    public int publish_year;
    public int total_chapter;
    public String thumbnail_url;
    public ArrayList<Chapter> mChapterList;
    public ArrayList<Comment> mCommentList;

    public Book() {
    }

    public Book(String id, String title, String discription) {
        this.mId = id;
        this.title = title;
        this.discription = discription;
    }

    public Book(String mId, String mTitle, String author, String mDiscription, int mVote, int mPublishYear, int mTotalChapter, String mThumbnailUrl, ArrayList<Chapter> mChapterList, ArrayList<Comment> mCommentList) {
        this.mId = mId;
        this.title = mTitle;
        this.discription = mDiscription;
        this.vote = mVote;
        this.publish_year = mPublishYear;
        this.total_chapter = mTotalChapter;
        this.thumbnail_url = mThumbnailUrl;
        this.mChapterList = mChapterList;
        this.mCommentList = mCommentList;
        this.author = author;
    }

    public String getmBookTypeId() {
        return mBookTypeId;
    }

    public void setmBookTypeId(String mBookTypeId) {
        this.mBookTypeId = mBookTypeId;
    }
    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getPublish_year() {
        return publish_year;
    }

    public void setPublish_year(int publish_year) {
        this.publish_year = publish_year;
    }

    public int getTotal_chapter() {
        return total_chapter;
    }

    public void setTotal_chapter(int total_chapter) {
        this.total_chapter = total_chapter;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public ArrayList<Chapter> getmChapterList() {
        return mChapterList;
    }

    public void setmChapterList(ArrayList<Chapter> mChapterList) {
        this.mChapterList = mChapterList;
    }

    public ArrayList<Comment> getmCommentList() {
        return mCommentList;
    }

    public void setmCommentList(ArrayList<Comment> mCommentList) {
        this.mCommentList = mCommentList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mId='" + mId + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", discription='" + discription + '\'' +
                ", vote=" + vote +
                ", publish_year='" + publish_year + '\'' +
                ", total_chapter=" + total_chapter +
                ", thumbnail_url='" + thumbnail_url + '\'' +
                ", mChapterList=" + mChapterList +
                ", mCommentList=" + mCommentList +
                '}';
    }
}
