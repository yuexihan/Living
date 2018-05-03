package com.tencent.living.dataHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.living.models.Comment;
import com.tencent.living.models.Post;
import com.tencent.living.models.ResultData;
import com.tencent.living.models.User;
import com.tencent.living.tools.Md5;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class UserHelper {
    private String ROOTPATH = "https://iliving.name";
    public ResultData<Post> postLogin(String phone_number, String password){
        String path = ROOTPATH + "/api/login";
        ResultData<Post> resultData = new ResultData<>();
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            //传入参数
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("phone_number",phone_number);
            paramMap.put("password",Md5.md5(password));
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

    public ResultData<Post> postRegister(String phone_number, String nickname, String password){
        String path = ROOTPATH + "/api/user";
        ResultData<Post> resultData = new ResultData<>();
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            //传入参数
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("phone_number",phone_number);
            paramMap.put("nickname",nickname);
            paramMap.put("password",Md5.md5(password));
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

    public ResultData<User> getUserInfo(String token) {
        String path = ROOTPATH + "/api/user?token="+token;
        ResultData<User> resultData = new ResultData<>();
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
                Type objectType = new TypeToken<ResultData<User>>() {
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
