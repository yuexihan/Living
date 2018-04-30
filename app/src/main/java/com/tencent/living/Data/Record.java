package com.tencent.living.Data;

import java.util.ArrayList;
import java.util.List;

public class Record {
    private String uuid;
    private int imageType;
    private String userName;
    private String time;
    private int emoType;
    private int emoDegree;
    private String content;
    private List<Comment> comments = new ArrayList<>();
    private int upCount;
    private int commentCount;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getEmoType() {
        return emoType;
    }

    public void setEmoType(int emoType) {
        this.emoType = emoType;
    }

    public int getEmoDegree() {
        return emoDegree;
    }

    public void setEmoDegree(int emoDegree) {
        this.emoDegree = emoDegree;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public int getUpCount() {
        return upCount;
    }

    public void setUpCount(int upCount) {
        this.upCount = upCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
