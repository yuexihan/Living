package com.tencent.living.dataHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.living.models.Comment;
import com.tencent.living.models.Message;
import com.tencent.living.models.Post;
import com.tencent.living.models.Record;
import com.tencent.living.models.ResultData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CommentHelper {
    private String ROOTPATH = "https://iliving.name";
    public ResultData<Post> postComment(String token, int emotion_id, int rspto, String content){
        String path = ROOTPATH + "/api/comment? token="+token;
        ResultData<Post> resultData = new ResultData<>();
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            //传入参数
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("content",content);
            paramMap.put("emotion_id",emotion_id);
            paramMap.put("rspto",rspto);
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String param = gson.toJson(paramMap);
            OutputStream out = connection.getOutputStream();
            out.write(param.getBytes());
            out.flush(); //清空缓冲区,发送数据
            out.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { //请求成功 获得返回的流
                InputStream is = connection.getInputStream();
                byte[] bytes = new byte[0];
                bytes = new byte[is.available()];
                is.read(bytes);
                String json = new String(bytes);
                Type objectType = new TypeToken<ResultData<Post>>() {
                }.getType();
                resultData = gson.fromJson(json, objectType);
            }
            resultData.setConn_code(responseCode);
            return resultData;
        }catch (Exception e){
            e.printStackTrace();
            return resultData;
        }

    }

    public ResultData<ArrayList<Comment>> getCommentsByEmotionId(int emotion_id, int pageno, String token){
        ResultData<ArrayList<Comment>> resultData = new ResultData<>();
        String path = ROOTPATH + "/api/comment?token="+token+"&pageno="+pageno+"&emotion_id="+emotion_id;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { //请求成功 获得返回的流
                InputStream is = connection.getInputStream();
                byte[] bytes = new byte[0];
                bytes = new byte[is.available()];
                is.read(bytes);
                String json = new String(bytes);
                Gson gson = new Gson();
                Type objectType = new TypeToken<ResultData<ArrayList<Comment>>>() {
                }.getType();
                resultData = gson.fromJson(json, objectType);
            }
            resultData.setConn_code(responseCode);
            return resultData;
        }catch (Exception e){
            e.printStackTrace();
            return resultData;
        }
    }

    public ResultData<Post> postLike(String token, int emotion_id){
        String path = ROOTPATH + "/api/like? token="+token;
        ResultData<Post> resultData = new ResultData<>();
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            //传入参数
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("emotion_id",emotion_id);
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
            String param = gson.toJson(paramMap);
            OutputStream out = connection.getOutputStream();
            out.write(param.getBytes());
            out.flush(); //清空缓冲区,发送数据
            out.close();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { //请求成功 获得返回的流
                InputStream is = connection.getInputStream();
                byte[] bytes = new byte[0];
                bytes = new byte[is.available()];
                is.read(bytes);
                String json = new String(bytes);
                Type objectType = new TypeToken<ResultData<Post>>() {
                }.getType();
                resultData = gson.fromJson(json, objectType);
            }
            resultData.setConn_code(responseCode);
            return resultData;
        }catch (Exception e){
            e.printStackTrace();
            return resultData;
        }

    }

    public ResultData<ArrayList<Message>> getMessagesByUserId(String token,int pageno){
        ResultData<ArrayList<Message>> resultData = new ResultData<>();
        String path = ROOTPATH + "/api/message?token="+token+"&pageno="+pageno;
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { //请求成功 获得返回的流
                InputStream is = connection.getInputStream();
                byte[] bytes = new byte[0];
                bytes = new byte[is.available()];
                is.read(bytes);
                String json = new String(bytes);
                Gson gson = new Gson();
                Type objectType = new TypeToken<ResultData<ArrayList<Message>>>() {
                }.getType();
                resultData = gson.fromJson(json, objectType);
            }
            resultData.setConn_code(responseCode);
            return resultData;
        }catch (Exception e){
            e.printStackTrace();
            return resultData;
        }
    }


}

