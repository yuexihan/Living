package com.tencent.living.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by doublewu on 2018/4/30.
 */
public class Record {
    private String content;
    private int label_id;
    private String label_name;
    private int strong;
    private String create_time;
    private int poster;
    private String nickname;
    private String avatar;
    private int like_cnt;
    private int comment_cnt;
    private int is_like;
    private String userID;
    private int emotion_id;

    private List<Comment> comments = new ArrayList<>();

    public List<Comment> getComments() {
        return comments;
    }

    public int getEmotion_id() {
        return emotion_id;
    }

    public void setEmotion_id(int emotion_id) {
        this.emotion_id = emotion_id;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String id) {
        this.userID = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLabel_id() {
        return label_id;
    }

    public void setLabel_id(int label_id) {
        this.label_id = label_id;
    }

    public String getLabel_name() {
        return label_name;
    }

    public void setLabel_name(String label_name) {
        this.label_name = label_name;
    }

    public int getStrong() {
        return strong;
    }

    public void setStrong(int strong) {
        this.strong = strong;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getPoster() {
        return poster;
    }

    public void setPoster(int poster) {
        this.poster = poster;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLike_cnt() {
        return like_cnt;
    }

    public void setLike_cnt(int like_cnt) {
        this.like_cnt = like_cnt;
    }

    public int getComment_cnt() {
        return comment_cnt;
    }

    public void setComment_cnt(int comment_cnt) {
        this.comment_cnt = comment_cnt;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }
}
