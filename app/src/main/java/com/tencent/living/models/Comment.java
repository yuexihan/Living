package com.tencent.living.models;

/**
 * Created by doublewu on 2018/4/30.
 */

public class Comment {
    private int id;
    private int emotionId;
    private String content;
    private int posterId;
    private String createTime;
    private int rsptoId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getRsptoId() {
        return rsptoId;
    }

    public void setRsptoId(int rsptoId) {
        this.rsptoId = rsptoId;
    }


}
