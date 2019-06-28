package itnl.app.com.bookreading.models;

import java.io.Serializable;

/**
 * Create by nguyennv on 11/16/18
 */
public class Comment extends Models implements Serializable{
    public String mCommentId;
    public String mUserId;
    public String comment;
    public String mLike;

    public Comment(){}
    public Comment(String mCommentId, String mUserId, String mComment, String mLike) {
        this.mCommentId = mCommentId;
        this.mUserId = mUserId;
        this.comment = mComment;
        this.mLike = mLike;
    }

    public String getmCommentId() {
        return mCommentId;
    }

    public void setmCommentId(String mCommentId) {
        this.mCommentId = mCommentId;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getmLike() {
        return mLike;
    }

    public void setmLike(String mLike) {
        this.mLike = mLike;
    }
}
