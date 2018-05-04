package com.tencent.living.models;

/**
 * Created by doublewu on 2018/5/1.
 */

public class Message {
    private int emotion_id;
    private String comment;
    private int poster;
    private int type;
    private String nickname;
    private String avatar;

    public int getEmotion_id() {
        return emotion_id;
    }

    public void setEmotion_id(int emotion_id) {
        this.emotion_id = emotion_id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getContentString(){
        if(type == 1){   //点赞
            return nickname+"点赞了你的状态";
        }
        if(type == 2){   //评论
            return nickname+"评论了你发布的状态："+comment;
        }
        if(type == 3){   //回复
            return nickname+"回复了你的评论："+comment;
        }
        else{
            return null;
        }
    }


}
