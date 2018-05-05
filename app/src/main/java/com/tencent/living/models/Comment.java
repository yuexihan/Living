package com.tencent.living.models;

/**
 * Created by doublewu on 2018/4/30.
 */

public class Comment {
    private int comment_id;
    private String comment = "";
    private int poster;
    private String poster_nickname = "";
    private String create_time = "";
    private int rspto;
    private String rspto_nickname;

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPoster() {
        return poster;
    }

    public void setPoster(int poster) {
        this.poster = poster;
    }

    public String getPoster_nickname() {
        return poster_nickname;
    }

    public void setPoster_nickname(String poster_nickname) {
        this.poster_nickname = poster_nickname;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getRspto() {
        return rspto;
    }

    public void setRspto(int rspto) {
        this.rspto = rspto;
    }

    public String getRspto_nickname() {
        return rspto_nickname;
    }

    public void setRspto_nickname(String rspto_nickname) {
        this.rspto_nickname = rspto_nickname;
    }
}
