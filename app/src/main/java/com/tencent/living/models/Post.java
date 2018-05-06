package com.tencent.living.models;

/**
 * Created by doublewu on 2018/5/1.
 */

public class Post {
    private String token;
    private int num_msg;

    public int getNum_msg() {
        return num_msg;
    }

    public void setNum_msg(int num_msg) {
        this.num_msg = num_msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
