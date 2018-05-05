package com.tencent.living.dataHelper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.living.models.ResultData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LivingServerAgent {
    public static final String HOST_URL = "https://iliving.name";
    public static final String ACTION_LOGIN =  "/api/user/login";
    public static final String ACTION_REGISTER =  "/api/user";
    public static final String ACTION_GET_USER =   "/api/user";
    public static final String ACTION_POST_RECORD = "/api/emotion";
    public static final String ACTION_SELF_RECORD = "/api/emotion/self";
    public static final String ACTION_ALL_RECORD = "/api/emotion";
    public static final String ACTION_POST_COMMENT = "/api/comment";
    public static final String ACTION_GET_COMMENT = "/api/comment";
    public static final String ACTION_POST_LIKE = "/api/like";
    public static final String ACTION_GET_MESSAGE = "/api/message";
    public static final String ACTION_LOGOUT = "/api/user/logout";
    public static final int DATA_DATA_PER_PAGE = 10;

    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_GET= "GET";

    private HttpURLConnection connection;
    private boolean isErr = false;
    private Map<String, Object> bodyParm = new HashMap<String, Object>();
    private String action;
    private String method = HTTP_METHOD_POST;
    private Map<String, String> httpParm = new HashMap<String, String>();

    public void setAction(String action){
        this.action = action;
    }
    //放入body发送的参数
    public void putData(String key, Object val){
        bodyParm.put(key, val);
    }
    //跟在URL后面的参数
    public void putParam(String key, String val){
        httpParm.put(key, val);
    }
    public void setHttpsMethod(String m){
        this.method = m;
    }
    private String getParamString(){
        if (httpParm.size() == 0)
            return "";
        String ret = "?";
        boolean isFirst = true;
        Iterator<Map.Entry<String, String>> it = httpParm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry =  it.next();
            if (!isFirst)
                ret += "&";
            ret += entry.getKey() + "=" + entry.getValue();
            isFirst = false;
        }
        return ret;
    }
    public <T> ResultData<T> execAndGetResult(Type objectType){
        ResultData<T> resultData = new ResultData<>();
        try{
            //初始化连接
            URL url = new URL(HOST_URL + action+ getParamString()) ;
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod(method);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (method.equals(LivingServerAgent.HTTP_METHOD_POST))
                connection.setDoOutput(true);
            connection.setDoInput(true);
            Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();

            //添加参数
            if (method.equals(LivingServerAgent.HTTP_METHOD_POST)) {
                String param = gson.toJson(bodyParm);
                OutputStream out = connection.getOutputStream();
                out.write(param.getBytes("utf-8"));
                out.close();
            }
            //获得结果
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) { //请求成功 获得返回的流
                String json = getResponseBody(connection.getInputStream()) ;
                resultData = gson.fromJson(json, objectType);
            }
            if (resultData != null)
                resultData.setConn_code(responseCode);
            return resultData;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private  String getResponseBody(InputStream is) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
