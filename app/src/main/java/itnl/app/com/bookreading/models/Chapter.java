package itnl.app.com.bookreading.models;

import java.io.Serializable;

/**
 * Create by nguyennv on 11/16/18
 */
public class Chapter extends Models implements Serializable{
    public String Id;
    public String mChapterUrl;
    public String mChapterUrlImage;
    public boolean mIsPublish;

    public Chapter() {
    }

    public Chapter(String id, String mChapterUrl, String mChapterUrlImage, boolean mIsPublish) {
        Id = id;
        this.mChapterUrl = mChapterUrl;
        this.mChapterUrlImage = mChapterUrlImage;
        this.mIsPublish = mIsPublish;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getmChapterUrl() {
        return mChapterUrl;
    }

    public void setmChapterUrl(String mChapterUrl) {
        this.mChapterUrl = mChapterUrl;
    }

    public String getmChapterUrlImage() {
        return mChapterUrlImage;
    }

    public void setmChapterUrlImage(String mChapterUrlImage) {
        this.mChapterUrlImage = mChapterUrlImage;
    }

    public boolean ismIsPublish() {
        return mIsPublish;
    }

    public void setmIsPublish(boolean mIsPublish) {
        this.mIsPublish = mIsPublish;
    }
}
