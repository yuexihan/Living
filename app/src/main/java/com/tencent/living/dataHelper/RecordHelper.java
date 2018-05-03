package com.tencent.living.dataHelper;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tencent.living.models.Post;
import com.tencent.living.models.Record;
import com.tencent.living.models.ResultData;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RecordHelper {
    private String ROOTPATH = "https://iliving.name";
    public ResultData<Post> postRecord(String token, int poster, String content, int label_id, int strong, int visiable) {
        String path = ROOTPATH + "/api/emotion? token="+token;
        ResultData<Post> resultData = new ResultData<>();
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("POST");
            //传入参数
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("content",content);
            paramMap.put("label_id",label_id);
            paramMap.put("strong",strong);
            paramMap.put("visiable",visiable);
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

    public ResultData<ArrayList<Record>> getRecordsByUserId(String token, int pageno) {
        ResultData<ArrayList<Record>> resultData = new ResultData<>();
        String path = ROOTPATH + "/api/emotion?token="+token+"&pageno="+pageno;
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
                Type objectType = new TypeToken<ResultData<ArrayList<Record>>>() {
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

//    public RecordInGround getRecordInGroundById(int recordId) {
//        String path = "http://172.16.168.111:1010/login.php?id=" + recordId;
//        try {
//            URL url = new URL(path);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setConnectTimeout(5000);
//            connection.setRequestMethod("GET"); //获得结果码
//            int responseCode = connection.getResponseCode();
//            if (responseCode == 200) { //请求成功 获得返回的流
//                InputStream is = connection.getInputStream();
//                byte[] bytes = new byte[0];
//                bytes = new byte[is.available()];
//                is.read(bytes);
//                String json = new String(bytes);
//                Gson gson = new Gson();
//                //把json字符串转化为对象
//                RecordInGround record = gson.fromJson(json, RecordInGround.class);
//                return record;
//
//            } else { //请求失败
//                return null;
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public ResultData<ArrayList<Record>> getRecordsInGround(int label_id, int pageno, String token) {
        ResultData<ArrayList<Record>> resultData = new ResultData<>();
        String path = ROOTPATH + "/api/emotion?label_id="+label_id+"&token="+token+"&pageno="+pageno;
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
                Type objectType = new TypeToken<ResultData<ArrayList<Record>>>() {
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
