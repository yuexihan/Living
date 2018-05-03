package com.tencent.living.models;

public class ResultData<T> {

    private T data;
    private int conn_code = -1;
    private int ret_code = -1;
    private String message;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getConn_code() {
        return conn_code;
    }

    public void setConn_code(int conn_code) {
        this.conn_code = conn_code;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }





}