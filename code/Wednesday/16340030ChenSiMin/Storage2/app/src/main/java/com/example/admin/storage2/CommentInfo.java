package com.example.admin.storage2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CommentInfo {
    private int comment_id;
    private String user_name;
    private String content;
    private String date;
    private int like_sum;
    private Bitmap user_image;
    private boolean islike = false;

    public int getCommentId() { return comment_id; }
    public void setCommentId(int i) { comment_id = i; }

    public String getUserName() { return user_name; }
    public void setUserName(String u) { user_name = u; }

    public String getContent() { return content; }
    public void setContent(String c) { content = c; }

    public String getDate() { return date; }
    public void setDate(String d) { date = d; }

    public int getLikeSum() { return like_sum; }
    public void setLikeSum(int l) { like_sum = l; }

    public boolean getIsLike() { return islike; }
    public void setIsLike(boolean i) { islike = i; }

    public Bitmap getUserImage() { return user_image; }
    public void setUserImage(Bitmap b){ user_image = b; }
    public void setUserImageByByte(byte[] data) {
        user_image = BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}
